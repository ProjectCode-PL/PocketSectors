package pl.projectcode.pocketsectors.waterdog.listener.redis;

import pl.projectcode.pocketsectors.common.packet.object.PacketSectorConnected;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.waterdog.manager.SectorManager;

public class PacketSectorConnectedPacketListener extends RedisPacketListener<PacketSectorConnected> {

    private final SectorManager sectorManager;

    public PacketSectorConnectedPacketListener(SectorManager sectorManager) {
        super(PacketSectorConnected.class);

        this.sectorManager = sectorManager;
    }

    @Override
    public void handle(PacketSectorConnected packet) {
        this.sectorManager.getSectorData(packet.getSender()).setOnline(true);
    }
}
