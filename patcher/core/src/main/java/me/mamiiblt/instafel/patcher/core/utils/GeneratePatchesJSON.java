package me.mamiiblt.instafel.patcher.core.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import me.mamiiblt.instafel.patcher.core.PatchInfoGenerator;
import me.mamiiblt.instafel.patcher.core.utils.models.PatchGroupInfoVar;
import me.mamiiblt.instafel.patcher.core.utils.models.PatchInfoVar;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.core.utils.patch.PInfos.PatchInfo;

public class GeneratePatchesJSON {

    public static String PATCHES_FILE_PATH = null;
    public static JSONObject patchesJSON;
    public static List<PatchInfoVar> patchInfos;

    public static void main(String[] args) throws Exception {
        PATCHES_FILE_PATH = args[0];
        System.out.println("Generating JSON...");
        patchesJSON = new JSONObject();
        patchInfos = PatchInfoGenerator.getPatchInfos();
        patchesJSON.put("singles", generateSingles());
        patchesJSON.put("groups", generateGroups());
        patchesJSON.put("total_size", patchInfos.size());
        FileUtils.writeStringToFile(new File(PATCHES_FILE_PATH), patchesJSON.toString(2), StandardCharsets.UTF_8);
    }

    public static JSONArray generateSingles() {
        JSONArray singles = new JSONArray();
        for (PatchInfoVar pInfo : patchInfos) {
            if (pInfo.isSingle) {
                singles.put(new JSONObject()
                    .put("class", pInfo.className)
                    .put("shortname", pInfo.shortname)
                    .put("name", pInfo.name)
                    .put("desc", pInfo.desc)
                    .put("author", pInfo.author)
                    .put("isSingle", pInfo.isSingle));
            }
        }
        return singles;
    }

    public static JSONArray generateGroups() throws Exception {
        JSONArray groups = new JSONArray();
        List<PatchGroupInfoVar> groupInfos = PatchInfoGenerator.getPatchGroupInfos();
        for (PatchGroupInfoVar gInfo : groupInfos) {
            JSONObject groupData = new JSONObject();
            InstafelPatchGroup group = PatchInfoGenerator.findPatchGroupByShortname(gInfo.shortName);
            group.loadPatches();
            JSONArray infos = new JSONArray();
            for (Class<? extends InstafelPatch> patch : group.patches) {
                for (PatchInfoVar pInfo : patchInfos) {
                    if (patch.getAnnotation(PatchInfo.class).shortname().equals(pInfo.shortname)) {
                        infos.put(new JSONObject()
                            .put("class", pInfo.className)
                            .put("shortname", pInfo.shortname)
                            .put("name", pInfo.name)
                            .put("desc", pInfo.desc)
                            .put("author", pInfo.author)
                            .put("isSingle", pInfo.isSingle));
                    }
                }
            }

            groupData.put("name", gInfo.name);
            groupData.put("shortname", gInfo.shortName);
            groupData.put("author", gInfo.author);
            groupData.put("infos", infos);
            groups.put(groupData);
        }
        return groups;
    }
}
