package me.mamiiblt.instafel.patcher.cli.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import me.mamiiblt.instafel.patcher.cli.handlers.Command;
import me.mamiiblt.instafel.patcher.cli.handlers.CoreHandler;
import me.mamiiblt.instafel.patcher.cli.utils.Log;
import me.mamiiblt.instafel.patcher.cli.utils.Utils;
import me.mamiiblt.instafel.patcher.cli.utils.modals.UpdateInfo;

public class ForceUpdateCore implements Command {

    @Override
    public void execute(String[] args) {
        try {
            Log.info("Downloading latest core...");
            UpdateInfo uInfo = CoreHandler.getLatestCoreUpdateInfo();
            if (Utils.PROP_CLI_VERSION.equals(uInfo.supported_pversion)) {
                Log.info("Core " + uInfo.commit + "@main downloading...");
                Path coreDir = Paths.get(Utils.getPatcherFolder(), "core_data");
                CoreHandler.downloadCoreJAR(uInfo, coreDir.resolve("core.jar").toFile());
                Log.info("Core updated succesfully.");
            } else {
                Log.info("Latest core isn't compatible with your patcher, please update the CLI");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.info("An error occured while running command");
            System.exit(-1);
        }

    }
    
}
