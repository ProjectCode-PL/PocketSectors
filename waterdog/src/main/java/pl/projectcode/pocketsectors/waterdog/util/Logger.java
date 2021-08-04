package pl.projectcode.pocketsectors.waterdog.util;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.command.ConsoleCommandSender;
import pl.projectcode.pocketsectors.common.util.ChatUtil;

public class Logger {

    private static final ConsoleCommandSender console = ProxyServer.getInstance().getConsoleSender();

    public static void info(Object object) {
        console.sendMessage(ChatUtil.fixColorsLogger("&c[PocketSectors-Waterdog] &6" + object.toString()));
    }
}
