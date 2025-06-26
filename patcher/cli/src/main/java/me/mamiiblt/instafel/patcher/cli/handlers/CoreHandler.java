package me.mamiiblt.instafel.patcher.cli.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import me.mamiiblt.instafel.patcher.cli.utils.Log;
import me.mamiiblt.instafel.patcher.cli.utils.Utils;
import me.mamiiblt.instafel.patcher.cli.utils.modals.UpdateInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CoreHandler {
    public static URLClassLoader coreClassLoader;
    public static String CORE_PACKAGE_NAME = "me.mamiiblt.instafel.patcher.core";
    public static File CORE_DATA_FOLDER, CORE_INFO_FILE, CORE_JAR_FILE;
    public static JSONObject INFO_DATA;

    public CoreHandler() {
        try {
            Path coreDir = Paths.get(Utils.getPatcherFolder(), "core_data");
            CORE_DATA_FOLDER = coreDir.toFile();
            CORE_INFO_FILE = coreDir.resolve("info.json").toFile();
            CORE_JAR_FILE = coreDir.resolve("core.jar").toFile();
            createOrReadCoreDatas();
            checkDebugCoreJAR();
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while loading core");
            System.exit(-1);
        }
    }

    private void checkDebugCoreJAR() throws Exception {
        File debugCore = new File(Paths.get(Utils.USER_DIR, "ifl-pcore-" + Utils.PROP_CLI_COMMIT_HASH + ".jar").toString());
        if (debugCore.exists()) {
            Log.info("Using debug core");
            CORE_JAR_FILE = debugCore;
            loadCoreJAR();
        } else {
            fetchCore();
        }
    }

    private void createOrReadCoreDatas() throws IOException {
        if (!CORE_DATA_FOLDER.exists() || !CORE_INFO_FILE.exists()) {
            CORE_DATA_FOLDER.mkdirs();
            if (!CORE_INFO_FILE.createNewFile()) {
                throw new IOException("Information file cannot be created, ask to dev...");
            }
            INFO_DATA = new JSONObject();
            INFO_DATA.put("lc_ts", System.currentTimeMillis());
            FileUtils.writeStringToFile(CORE_INFO_FILE, INFO_DATA.toString(4), StandardCharsets.UTF_8);
        } else {
            INFO_DATA = new JSONObject(FileUtils.readFileToString(CORE_INFO_FILE, StandardCharsets.UTF_8));
        }
    }

    private void loadCoreJAR() throws MalformedURLException {
        coreClassLoader = new URLClassLoader(new URL[]{CORE_JAR_FILE.toURI().toURL()}, CoreHandler.class.getClassLoader());
        loadCoreInfo();
    }

    public static void downloadCoreJAR(UpdateInfo updateInfo, File CORE_JAR_FILE) {
        String URL = "https://github.com/mamiiblt/instafel/raw/refs/heads/ft-releases/p-core/dist/ifl-pcore-" + updateInfo.commit + ".jar";
    
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                try (ResponseBody body = response.body();
                    InputStream inputStream = body.byteStream();
                    OutputStream outputStream = new FileOutputStream(CORE_JAR_FILE)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    Log.info("Latest core succesfuly downloaded");
                }
            } else {
                Log.severe("Error while downloading core..." + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchCore() throws Exception {
        if (CORE_JAR_FILE.exists()) {
            long last_ts = INFO_DATA.getLong("lc_ts");
            long elapsed_time = (System.currentTimeMillis() - last_ts) / 1000;
            if (elapsed_time >= 14400) {
                UpdateInfo uInfo = getLatestCoreUpdateInfo();
                try (JarFile jarFile = new JarFile(CORE_JAR_FILE)) {
                    Manifest manifest = jarFile.getManifest();
                    Attributes attrs = manifest.getMainAttributes();

                    String currentCommit = attrs.getValue("Patcher-Core-Commit");
                    if (currentCommit.equals(uInfo.commit)) {
                        loadCoreJAR();
                    } else {
                        if (!uInfo.supported_pversion.equals(Utils.PROP_CLI_VERSION)) {
                            Log.severe("Latest core isn't compatible with your patcher, please update patcher for use latest core");
                            System.exit(-1);
                        } else {
                            downloadCoreJAR(uInfo, CORE_JAR_FILE);
                            loadCoreJAR();
                            Log.info("Core updated (" + uInfo.commit + ")");
                        }
                    }
                }
                updateInfoTS();
            } else {
                loadCoreJAR();
            }
        } else {
            Log.info("Core JAR not found, downloading...");
            UpdateInfo uInfo = getLatestCoreUpdateInfo();
            if (!Utils.PROP_CLI_VERSION.equals(uInfo.supported_pversion)) {
                Log.severe("Latest core isn't compatible with your patcher, please update patcher for use latest core");
                System.exit(-1);
            } else {
                downloadCoreJAR(uInfo, CORE_JAR_FILE);
                loadCoreJAR();
            }
        }
    }

    private void updateInfoTS() throws JSONException, IOException {
        INFO_DATA = new JSONObject();
        INFO_DATA.put("lc_ts", System.currentTimeMillis());
        FileUtils.writeStringToFile(CORE_INFO_FILE, INFO_DATA.toString(4), StandardCharsets.UTF_8);
    }

    private void loadCoreInfo() {
        try {
            JarFile jarFile = new JarFile(CORE_JAR_FILE);
            Manifest manifest = jarFile.getManifest();
            Attributes attr = manifest.getMainAttributes();
            
            Utils.PROP_CORE_COMMIT = attr.getValue("Patcher-Core-Commit");
            Utils.PROP_CORE_BRANCH = attr.getValue("Patcher-Core-Branch");
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while loading sources from core: " + e.getMessage());
            System.exit(-1);
        }
    }

    public static UpdateInfo getLatestCoreUpdateInfo() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/mamiiblt/instafel/refs/heads/ft-releases/p-core/latest.json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                UpdateInfo updateInfo = new UpdateInfo();
                updateInfo.commit = jsonObject.getString("commit");
                updateInfo.supported_pversion = jsonObject.getString("supported_pversion");
                return updateInfo;
            } else {
                throw new Exception("An error occured while reading / sending API request");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("An error has occured while checking core updates...");
        }
    }
    
    public static Object invokeNonParamMethod(String className, String methodName, Class<?>[] paramTypes, Object... args) {
        try {
            Class<?> clazz = coreClassLoader.loadClass(CORE_PACKAGE_NAME + "." + className);
            Method method = clazz.getMethod(methodName, paramTypes);
            return method.invoke(null, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
