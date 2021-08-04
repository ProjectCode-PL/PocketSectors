package pl.projectcode.pocketsectors.nukkit.listener.other;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Location;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.projectcode.pocketsectors.common.sector.SectorType;
import pl.projectcode.pocketsectors.common.util.ChatUtil;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;
import pl.projectcode.pocketsectors.nukkit.event.sector.SectorChangeEvent;
import pl.projectcode.pocketsectors.nukkit.manager.TransferPlayerManager;
import pl.projectcode.pocketsectors.nukkit.sector.Sector;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;
import pl.projectcode.pocketsectors.nukkit.user.User;
import pl.projectcode.pocketsectors.nukkit.util.Configuration;

@RequiredArgsConstructor
public class MoveListener implements Listener {

    private final NukkitSector nukkitSector;

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        this.handlePlayerMove(event.getPlayer(), event.getFrom(), event.getTo(), true);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        this.handlePlayerMove(event.getPlayer(), event.getFrom(), event.getTo(), false);
    }

    private void handlePlayerMove(Player player, Location from, Location to, boolean teleport) {
        if(from.floor().equals(to.floor())) {
            return;
        }

        SectorManager sectorManager = this.nukkitSector.getSectorManager();
        Sector currentSector = sectorManager.getCurrentSector();

        User user = this.nukkitSector.getUserManager().getUser(player);

        int borderDistance = currentSector.getBorderDistance(to);
        Sector nearestSector = currentSector.getNearestSector(to);

        if(nearestSector != null && borderDistance <= Configuration.BORDER_MESSAGE_DISTANCE) {
            player.sendTip(ChatUtil.fixColors(Configuration.BORDER_MESSAGE
                    .replace("{SECTOR}", nearestSector.getName())
                    .replace("{DISTANCE}", Integer.toString(borderDistance))
            ));
        }

        Sector sector = sectorManager.getSector(to);

        if(sector == null) {
            return;
        }

        if(!currentSector.equals(sector) && !(currentSector.getType() == SectorType.SPAWN && sector.getType() == SectorType.SPAWN)) {
            if(!sector.isOnline()) {
                player.sendTitle(ChatUtil.fixColors(Configuration.SECTOR_DISABLED_TITLE), ChatUtil.fixColors(Configuration.SECTOR_DISABLED_SUBTITLE), 10, 20 * 2, 10);
                currentSector.knockBorder(player);
                return;
            }

            if(user.getLastSectorChange() <= 1000) {
                return;
            }

            user.setLastSectorChange();

            SectorChangeEvent ev = new SectorChangeEvent(player, sector);
            nukkitSector.getServer().getPluginManager().callEvent(ev);

            if(ev.isCancelled()) {
                return;
            }

            if(!teleport) {
                sectorManager.transferToSector(player, sector);
            } else {
                player.getServer().getScheduler().scheduleDelayedTask(this.nukkitSector, () -> sectorManager.transferToSector(player, sector), 3); // nukkit issue
            }
        }
    }
}
