package pl.projectcode.pocketsectors.nukkit.util;

import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import pl.projectcode.pocketsectors.common.util.ChatUtil;

public class Logger {

    private static final ConsoleCommandSender console = Server.getInstance().getConsoleSender();

    public static void info(Object object) {
        console.sendMessage(ChatUtil.fixColorsLogger("%M[PocketSectors-Nukkit] %C" + object.toString()));
    }
}
