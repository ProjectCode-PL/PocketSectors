package pl.projectcode.pocketsectors.nukkit.listener.player;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.level.Location;
import cn.nukkit.potion.Effect;
import lombok.AllArgsConstructor;
import pl.projectcode.pocketsectors.common.object.PlayerInfo;
import pl.projectcode.pocketsectors.common.util.Vector3;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;
import pl.projectcode.pocketsectors.nukkit.event.sector.SectorPlayerFirstJoinEvent;
import pl.projectcode.pocketsectors.nukkit.packet.PacketTransferPlayer;
import pl.projectcode.pocketsectors.nukkit.user.User;
import pl.projectcode.pocketsectors.nukkit.user.UserManager;
import pl.projectcode.pocketsectors.nukkit.util.InventoryUtil;
import pl.projectcode.pocketsectors.nukkit.util.SerializationUtil;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class PlayerLocallyInitializedListener implements Listener {

    private final NukkitSector nukkitSector;
    
    @EventHandler
    public void onPlayerLocallyInitialized(final PlayerLocallyInitializedEvent event) {
        Player player = event.getPlayer();
        PacketTransferPlayer packetTransferPlayer = this.nukkitSector.getTransferPlayerManager().getPacketTransferPlayer(player.getName());
        UserManager userManager = this.nukkitSector.getUserManager();

        userManager.createUser(player);

        User user = userManager.getUser(player);

        if(packetTransferPlayer != null) {
            this.onPacketTransferPlayer(player, packetTransferPlayer);
            user.setFirstJoin(false);
        } else {
            user.setFirstJoin(true);
        }

        if(user.isFirstJoin()) {
            this.nukkitSector.getServer().getPluginManager().callEvent(new SectorPlayerFirstJoinEvent(player, this.nukkitSector.getSectorManager().getCurrentSector()));
        }
    }

    private void onPacketTransferPlayer(Player player, PacketTransferPlayer packet) {
        this.nukkitSector.getTransferPlayerManager().removePacketTransferPlayer(player.getName());

        PlayerInfo playerInfo = packet.getPlayerInfo();
        Vector3 position = playerInfo.getPosition();

        player.teleport(new Location(position.getX(), position.getY(), position.getZ(), playerInfo.getYaw(), playerInfo.getPitch(), player.getLevel()));

        PlayerInventory inventory = player.getInventory();

        inventory.setContents(InventoryUtil.itemArrayToInventoryContents(SerializationUtil.deserializeItemArray(playerInfo.getInventory())));
        inventory.setArmorContents(SerializationUtil.deserializeItemArray(playerInfo.getArmorInventory()));
        player.getEnderChestInventory().setContents(InventoryUtil.itemArrayToInventoryContents(SerializationUtil.deserializeItemArray(playerInfo.getEnderChest())));

        inventory.setHeldItemIndex(playerInfo.getHeldItemIndex());

        player.removeAllEffects();

        for(Effect effect : SerializationUtil.deserializeEffects(playerInfo.getPotionEffects())) {
            player.addEffect(effect);
        }

        player.setGamemode(playerInfo.getGameMode());

        player.setHealth(playerInfo.getHealth());
        player.getFoodData().setLevel(playerInfo.getFoodLevel());

        player.setExperience(playerInfo.getExperience());
        player.sendExperienceLevel(playerInfo.getExperienceLevel());

        player.fireTicks = playerInfo.getFireTicks();

        player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, playerInfo.isAllowFlight());
        player.getAdventureSettings().set(AdventureSettings.Type.FLYING, playerInfo.isFlying());
    }
}
