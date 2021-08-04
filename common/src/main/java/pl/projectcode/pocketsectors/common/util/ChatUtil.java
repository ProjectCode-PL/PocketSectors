package pl.projectcode.pocketsectors.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatUtil {

    private static final String COLOR = "&7";
    private static final String COLOR_MARK = "&c";
    private static final String COLOR_LOGGER = "&6";
    private static final String COLOR_LOGGER_MARK = "&c";

    public static String fixColors(String message) {
        return message.replace("%C", COLOR).replace("%M", COLOR_MARK).replace("&", "§");
    }

    public static String fixColorsLogger(String message) {
        return message.replace("%C", COLOR_LOGGER).replace("%M", COLOR_LOGGER_MARK).replace("&", "§");
    }
}
