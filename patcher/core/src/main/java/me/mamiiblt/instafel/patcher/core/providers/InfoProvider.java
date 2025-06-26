package me.mamiiblt.instafel.patcher.core.providers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import me.mamiiblt.instafel.patcher.core.PatchLoader;
import me.mamiiblt.instafel.patcher.core.utils.Log;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.core.utils.patch.PInfos;

public class InfoProvider {
    public static JSONObject getPatchesList() {
        try {
            JSONObject resp = new JSONObject();
            JSONArray singles = new JSONArray();
            JSONArray groups = new JSONArray();

            List<PInfos.PatchInfo> patchInfos = PatchLoader.getPatchInfos();
            for (PInfos.PatchInfo pInfo : patchInfos) {
                if (pInfo.isSingle()) {
                    singles.put(new JSONObject()
                        .put("shortname", pInfo.shortname())
                        .put("name", pInfo.name())
                        .put("desc", pInfo.desc())
                        .put("author", pInfo.author())
                        .put("isSingle", pInfo.isSingle()));
                }
            }

            List<PInfos.PatchGroupInfo> patchGroupInfos = PatchLoader.getPatchGroupInfos();
            for (PInfos.PatchGroupInfo patchGroupInfo : patchGroupInfos) {
                InstafelPatchGroup group = PatchLoader.findPatchGroupByShortname(patchGroupInfo.shortname());
                JSONObject groupData = new JSONObject();
                groupData.put("shortname", group.shortname);
                groupData.put("name", group.name);
                groupData.put("desc", group.description);
                groupData.put("author", group.author);

                JSONArray gPatchInfos = new JSONArray();
                group.loadPatches();
                for (Class<? extends InstafelPatch> patch : group.patches) {
                    PInfos.PatchInfo info = patch.getAnnotation(PInfos.PatchInfo.class);
                    gPatchInfos.put(new JSONObject()
                        .put("shortname", info.shortname())
                        .put("name", info.name())
                        .put("desc", info.desc())
                        .put("author", info.author())
                        .put("isSingle", info.isSingle()));
                }
                groupData.put("infos", gPatchInfos);
                groups.put(groupData);
            }
            
            resp.put("total_size", patchInfos.size());
            resp.put("singles", singles);
            resp.put("groups", groups);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while getting patches from loader: " + e.getMessage());
            return null;
        }
    }
}