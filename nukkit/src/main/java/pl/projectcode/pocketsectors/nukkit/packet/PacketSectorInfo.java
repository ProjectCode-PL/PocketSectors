package pl.projectcode.pocketsectors.nukkit.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.packet.Packet;

@AllArgsConstructor
@Getter
public class PacketSectorInfo extends Packet {

    private final float tps;
    private final int playerCount;

    public float getTPS() {
        return tps;
    }
}
