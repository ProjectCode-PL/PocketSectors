package pl.projectcode.pocketsectors.nukkit.util;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class InventoryUtil {

    public static Map<Integer, Item> getIncludeEmptyInventoryContents(Inventory inventory) {
        Map<Integer, Item> contents = new HashMap<>();

        for(int i = 0; i < inventory.getSize(); i++)
            contents.put(i, inventory.getItem(i));

        return contents;
    }

    public static Item[] inventoryContentsToItemArray(Map<Integer, Item> contents) {
        Item[] items = new Item[contents.size()];

        for(Map.Entry<Integer, Item> entry : contents.entrySet())
            items[entry.getKey()] = entry.getValue();

        return items;
    }

    public static Map<Integer, Item> itemArrayToInventoryContents(Item[] items) {
        Map<Integer, Item> contents = new HashMap<>();

        for(int i = 0; i < items.length; i++)
            contents.put(i, items[i]);

        return contents;
    }
}

