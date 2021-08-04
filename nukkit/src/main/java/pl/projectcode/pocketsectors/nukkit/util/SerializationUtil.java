package pl.projectcode.pocketsectors.nukkit.util;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.potion.Effect;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SerializationUtil {

    public static String serializeItem(Item item) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(item.getId());
        output.writeInt(item.getDamage());
        output.writeInt(item.getCount());

        output.writeUTF(item.hasCompoundTag() ? new String(item.getCompoundTag()) : "");

        return new String(output.toByteArray());
    }

    public static Item deserializeItem(String serialized) {
        ByteArrayDataInput input = ByteStreams.newDataInput(serialized.getBytes());

        Item item = Item.get(input.readInt(), input.readInt(), input.readInt());

        String compoundTag = input.readUTF();

        if(compoundTag.length() > 0)
            item.setCompoundTag(compoundTag.getBytes());

        return item;
    }

    public static String serializeItemArray(Item[] items) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(items.length);

        for(Item item : items)
            output.writeUTF(serializeItem(item));

        return new String(output.toByteArray());
    }

    public static Item[] deserializeItemArray(String serialized) {
        ByteArrayDataInput input = ByteStreams.newDataInput(serialized.getBytes());

        Item[] items = new Item[input.readInt()];

        for(int i = 0; i < items.length; i++)
            items[i] = deserializeItem(input.readUTF());

        return items;
    }

    public static String serializeInventory(Inventory inventory) {
        return serializeItemArray(InventoryUtil.inventoryContentsToItemArray(InventoryUtil.getIncludeEmptyInventoryContents(inventory)));
    }

    public static String serializeEffect(Effect effect) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(effect.getId());
        output.writeInt(effect.getAmplifier());
        output.writeInt(effect.getDuration());

        return new String(output.toByteArray());
    }

    public static Effect deserializeEffect(String serialized) {
        ByteArrayDataInput input = ByteStreams.newDataInput(serialized.getBytes());

        return Effect.getEffect(input.readInt()).setAmplifier(input.readInt()).setDuration(input.readInt());
    }

    public static String serializeEffects(Effect[] effects) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(effects.length);

        for(Effect effect : effects)
            output.writeUTF(serializeEffect(effect));

        return new String(output.toByteArray());
    }

    public static Effect[] deserializeEffects(String serialized) {
        ByteArrayDataInput input = ByteStreams.newDataInput(serialized.getBytes());

        Effect[] effects = new Effect[input.readInt()];

        for(int i = 0; i < effects.length; i++)
            effects[i] = deserializeEffect(input.readUTF());

        return effects;
    }
}
