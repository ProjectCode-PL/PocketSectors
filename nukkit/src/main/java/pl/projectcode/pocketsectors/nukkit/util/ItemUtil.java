package pl.projectcode.pocketsectors.nukkit.util;

import cn.nukkit.item.Item;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemUtil {

    public static int getItemIdByName(String name) {
        name = name.trim().replace(' ', '_').replace("minecraft:", "");

        try {
            return Item.class.getField(name.toUpperCase()).getInt(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
