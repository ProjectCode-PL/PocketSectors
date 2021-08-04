package pl.projectcode.pocketsectors.nukkit.listener.block;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import lombok.AllArgsConstructor;
import pl.projectcode.pocketsectors.common.util.ChatUtil;
import pl.projectcode.pocketsectors.nukkit.util.Configuration;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;

@AllArgsConstructor
public class BlockBreakListener implements Listener {

    private final SectorManager sectorManager;

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        int borderDistance = this.sectorManager.getCurrentSector().getBorderDistance(event.getBlock().getLocation());

        if(borderDistance <= Configuration.BREAK_BORDER_DISTANCE && !player.hasPermission("pocketsectors.border.break")) {
            event.setCancelled(true);
            player.sendMessage(ChatUtil.fixColors(Configuration.BREAK_BORDER_DISTANCE_MESSAGE));
        }
    }
}
