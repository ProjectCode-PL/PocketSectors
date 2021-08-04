package pl.projectcode.pocketsectors.nukkit.event.sector;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import pl.projectcode.pocketsectors.nukkit.sector.Sector;

@Getter
public class SectorChangeEvent extends SectorEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    protected final Player player;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public SectorChangeEvent(Player player, Sector sector) {
        super(sector);

        this.player = player;
    }
}
