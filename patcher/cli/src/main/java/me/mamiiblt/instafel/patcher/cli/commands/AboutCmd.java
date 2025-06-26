package me.mamiiblt.instafel.patcher.cli.commands;

import brut.androlib.ApktoolProperties;
import me.mamiiblt.instafel.patcher.cli.handlers.Command;
import me.mamiiblt.instafel.patcher.cli.utils.Utils;

public class AboutCmd implements Command {

    @Override
    public void execute(String[] args) {
        String[] helpLines = {
            "CLI Version    : v" + Utils.PROP_CLI_VERSION + " (" + Utils.PROP_CLI_PROJECT_TAG + ")",
            "CLI Commit     : " + Utils.PROP_CLI_COMMIT_HASH + "@" + Utils.PROP_CLI_PROJECT_BRANCH,
            "Core Commit    : " + Utils.PROP_CORE_COMMIT + "@" + Utils.PROP_CORE_BRANCH,
            "Apktool Ver.   : v" + ApktoolProperties.getVersion(),
            "Baksmali Ver.  : v" + ApktoolProperties.getBaksmaliVersion(),
            "Telegram       : t.me/instafel",
            "Repository     : github.com/mamiiblt/instafel",
        };

        for (String line : helpLines) {
            System.out.println(line);
        }
    }
}