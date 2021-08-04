package pl.projectcode.pocketsectors.nukkit.util;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.level.Location;
import cn.nukkit.potion.Effect;
import lombok.experimental.UtilityClass;
import pl.projectcode.pocketsectors.common.object.PlayerInfo;
import pl.projectcode.pocketsectors.common.packet.PacketChannel;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.nukkit.packet.PacketPlayerInfoRequest;
import pl.projectcode.pocketsectors.common.util.Vector3;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class PlayerInfoUtil {

    private static final Map<String, CompletableFuture<PlayerInfo>> playerInfoCompletableFutures = new HashMap<>();

    public static PlayerInfo createPlayerInfo(Player player) {
        Location location = player.getLocation();
        PlayerInventory playerInventory = player.getInventory();

        String inventory = SerializationUtil.serializeInventory(playerInventory);
        String armorInventory = SerializationUtil.serializeItemArray(playerInventory.getArmorContents());
        String enderChest = SerializationUtil.serializeInventory(player.getEnderChestInventory());

        PlayerInfo playerInfo = new PlayerInfo();

        playerInfo.setPlayerName(player.getName());
        playerInfo.setSectorName(NukkitSector.getInstance().getSectorManager().getCurrentSectorName());
        playerInfo.setPosition(new Vector3(location.getX(), location.getY(), location.getZ()));
        playerInfo.setYaw(player.yaw);
        playerInfo.setPitch(player.pitch);
        playerInfo.setInventory(inventory);
        playerInfo.setArmorInventory(armorInventory);
        playerInfo.setEnderChest(enderChest);
        playerInfo.setHeldItemIndex(playerInventory.getHeldItemIndex());
        playerInfo.setGameMode(player.getGamemode());
        playerInfo.setPotionEffects(SerializationUtil.serializeEffects(player.getEffects().values().toArray(new Effect[0])));
        playerInfo.setHealth(player.getHealth());
        playerInfo.setFoodLevel(player.getFoodData().getLevel());
        playerInfo.setExperience(player.getExperience());
        playerInfo.setExperienceLevel(player.getExperienceLevel());
        playerInfo.setFireTicks(player.fireTicks);
        playerInfo.setAllowFlight(player.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT));
        playerInfo.setFlying(player.getAdventureSettings().get(AdventureSettings.Type.FLYING));

        return playerInfo;
    }

    public static CompletableFuture<PlayerInfo> getPlayerInfo(String playerName) {
        if(!NukkitSector.getInstance().getSectorManager().isPlayerOnline(playerName)) {
            return null;
        }

        RedisManager.getInstance().publish(PacketChannel.SECTORS, new PacketPlayerInfoRequest(playerName));

        CompletableFuture<PlayerInfo> completableFuture = new CompletableFuture<>();

        playerInfoCompletableFutures.put(playerName, completableFuture);

        return completableFuture;
    }

    public static CompletableFuture<PlayerInfo> getPlayerInfoCompletableFuture(String playerName) {
        return playerInfoCompletableFutures.get(playerName);
    }
}
