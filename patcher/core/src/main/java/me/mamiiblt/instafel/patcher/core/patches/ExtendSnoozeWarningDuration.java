package me.mamiiblt.instafel.patcher.core.patches;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.core.utils.Log;
import me.mamiiblt.instafel.patcher.core.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.core.utils.Utils;
import me.mamiiblt.instafel.patcher.core.utils.models.LineData;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.core.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.core.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Extend Snooze Warning Duration",
    shortname = "ext_snooze_warning_dur",
    desc = "You can extend snooze activity duration with this patch",
    author = "mamiiblt",
    isSingle = true
)
public class ExtendSnoozeWarningDuration extends InstafelPatch {
    
    private File dogfoodingClass = null;
    private SmaliUtils smaliUtils = getSmaliUtils();

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            findDogfoodingClass,
            extendConstraint
        );
    }

    InstafelTask findDogfoodingClass = new InstafelTask("Find constraint in DogfoodingEligibilityApi included classes") {

        @Override
        public void execute() throws Exception {
            File[] smaliFolders = smaliUtils.getSmaliFolders();
            int scannedFileSize = 0;
            boolean fileFoundLock = false;
            for (File folder : smaliFolders) {
                if (fileFoundLock) {
                    break;
                } else {
                    File xFolder = new File(Utils.mergePaths(folder.getAbsolutePath(), "X"));
                    Log.info("Searching in X folder of " + folder.getName());
    
                    Iterator<File> fileIterator = FileUtils.iterateFiles(xFolder, null, true);
                    while (fileIterator.hasNext()) {
                        scannedFileSize++;
                        File file = fileIterator.next();
                        List<String> fContent = smaliUtils.getSmaliFileContent(file.getAbsolutePath()); 
                        List<LineData> matchLines = smaliUtils.getContainLines(fContent,
                            ".method", ";Lcom/instagram/release/lockout/DogfoodingEligibilityApi;L", "<init>");
                        
                        if (matchLines.size() == 1) {
                            dogfoodingClass = file;
                            Log.info("File found in " + dogfoodingClass.getName() + " at " + folder.getName());
                            fileFoundLock = true;
                            break;
                        } 
                    }
                }
            }

            if (dogfoodingClass != null) {
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                success("DogfoodingEligibilityApi founded.");
            } else {
                failure("DogfoodingEligibilityApi class cannot found");
            }
        }
    };

    InstafelTask extendConstraint = new InstafelTask("Change 7 day constraint to 50 day") {

        @Override
        public void execute() throws Exception {
            List<String> fContent = smaliUtils.getSmaliFileContent(dogfoodingClass.getAbsolutePath());
            boolean status = false;
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);

                if (line.contains("const") && line.contains("0x7")) {
                    fContent.set(i, "    const/16 v0, 0x32");
                    Log.info("Day constraint extended at line " + fContent.get(i) + " to 50 day.");
                    status = true;
                }
            }  

            if (status) {
                FileUtils.writeLines(dogfoodingClass, fContent);
                success("Day constraint succesfully extended.");
            } else {
                failure("Patcher can't found constraint line...");
            }
        }
    };
}
