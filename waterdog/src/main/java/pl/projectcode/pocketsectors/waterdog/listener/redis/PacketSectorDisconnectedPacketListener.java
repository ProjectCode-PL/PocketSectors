package pl.projectcode.pocketsectors.waterdog.listener.redis;

import pl.projectcode.pocketsectors.common.packet.object.PacketSectorDisconnected;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.waterdog.manager.SectorManager;

public class PacketSectorDisconnectedPacketListener extends RedisPacketListener<PacketSectorDisconnected> {

    private final SectorManager sectorManager;

    public PacketSectorDisconnectedPacketListener(SectorManager sectorManager) {
        super(PacketSectorDisconnected.class);

        this.sectorManager = sectorManager;
    }

    @Override
    public void handle(PacketSectorDisconnected packet) {
        this.sectorManager.getSectorData(packet.getSender()).setOnline(false);
    }
}
