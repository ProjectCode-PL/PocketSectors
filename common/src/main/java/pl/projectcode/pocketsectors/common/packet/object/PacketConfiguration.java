package pl.projectcode.pocketsectors.common.packet.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.packet.Packet;
import pl.projectcode.pocketsectors.common.sector.SectorData;

@AllArgsConstructor
@Getter
public class PacketConfiguration extends Packet {

    private final SectorData[] sectorsData;
}
