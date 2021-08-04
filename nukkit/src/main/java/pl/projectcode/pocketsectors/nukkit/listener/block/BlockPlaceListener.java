package pl.projectcode.pocketsectors.nukkit.listener.block;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import lombok.AllArgsConstructor;
import pl.projectcode.pocketsectors.common.util.ChatUtil;
import pl.projectcode.pocketsectors.nukkit.util.Configuration;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;

@AllArgsConstructor
public class BlockPlaceListener implements Listener {

    private final SectorManager sectorManager;

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();
        int borderDistance = this.sectorManager.getCurrentSector().getBorderDistance(event.getBlock().getLocation());

        if(borderDistance <= Configuration.PLACE_BORDER_DISTANCE && !player.hasPermission("pocketsectors.border.place")) {
            event.setCancelled(true);
            player.sendMessage(ChatUtil.fixColors(Configuration.PLACE_BORDER_DISTANCE_MESSAGE));
        }
    }
}
