package pl.projectcode.pocketsectors.nukkit.event.sector;

import cn.nukkit.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.projectcode.pocketsectors.nukkit.sector.Sector;

@RequiredArgsConstructor
@Getter
public abstract class SectorEvent extends Event {

    protected final Sector sector;
}
