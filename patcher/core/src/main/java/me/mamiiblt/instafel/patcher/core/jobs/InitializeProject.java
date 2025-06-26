package me.mamiiblt.instafel.patcher.core.jobs;

import java.io.File;

import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.core.source.SourceManager;
import me.mamiiblt.instafel.patcher.core.source.SourceUtils;
import me.mamiiblt.instafel.patcher.core.source.WorkingDir;
import me.mamiiblt.instafel.patcher.core.utils.Env;
import me.mamiiblt.instafel.patcher.core.utils.Log;
import me.mamiiblt.instafel.patcher.core.utils.Utils;

public class InitializeProject {

    public static void runInitProject(File PDIR, File apkFile) {
        try {
            Env.PROJECT_DIR = WorkingDir.createWorkingDir(PDIR.getAbsolutePath(), apkFile.getName());
            SourceManager sourceManager = new SourceManager();
            sourceManager.setConfig(SourceUtils.getDefaultIflConfigDecoder(sourceManager.getConfig()));
            sourceManager.getConfig().setFrameworkDirectory(SourceUtils.getDefaultFrameworkDirectory());
            sourceManager.decompile(new ExtFile(
                Utils.mergePaths(apkFile.getAbsolutePath())
            ));

            File dwBin = new File(Utils.mergePaths(Env.PROJECT_DIR, "sources", "assets", "drawables.bin"));
            if (dwBin.exists()) {
                Log.info("drawables.bin deleted.");
                dwBin.delete();
            }

            sourceManager.createConfigAndEnvFile();
            Log.info("Project succesfully created");
        } catch (Exception e) {
            Log.severe("Failed to run job: " + e.getMessage());
        }
    }
}
