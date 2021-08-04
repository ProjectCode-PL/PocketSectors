package pl.projectcode.pocketsectors.nukkit.event.sector;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import pl.projectcode.pocketsectors.nukkit.sector.Sector;

@Getter
public class SectorPlayerFirstJoinEvent extends SectorEvent {

    private static final HandlerList handlers = new HandlerList();

    protected Player player;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public SectorPlayerFirstJoinEvent(Player player, Sector sector) {
        super(sector);

        this.player = player;
    }
}
