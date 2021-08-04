package pl.projectcode.pocketsectors.nukkit.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {

    public static boolean isNumeric(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
