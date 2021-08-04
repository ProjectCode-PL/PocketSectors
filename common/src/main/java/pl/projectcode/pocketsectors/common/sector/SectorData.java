package pl.projectcode.pocketsectors.common.sector;

import lombok.Getter;
import lombok.Setter;
import pl.projectcode.pocketsectors.common.util.Vector3;

import java.io.Serializable;

@Getter
@Setter
public class SectorData implements Serializable {

    private final String name;
    private final Vector3 firstCorner;
    private final Vector3 secondCorner;
    private final String world;
    private final SectorType type;
    private final Vector3 center;
    private final String address;
    private final int port;

    private boolean online;

    public SectorData(String name, Vector3 firstCorner, Vector3 secondCorner, String world, SectorType type, String address, int port) {
        this.name = name;
        this.firstCorner = firstCorner;
        this.secondCorner = secondCorner;
        this.world = world;
        this.type = type;
        this.address = address;
        this.port = port;

        this.center = new Vector3(firstCorner.getX() + (secondCorner.getX() - firstCorner.getX()) / 2, 0, firstCorner.getZ() + (secondCorner.getZ() - firstCorner.getZ()) / 2);
    }
}
