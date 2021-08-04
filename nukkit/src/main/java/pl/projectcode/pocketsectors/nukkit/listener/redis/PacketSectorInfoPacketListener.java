package pl.projectcode.pocketsectors.nukkit.listener.redis;

import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.nukkit.packet.PacketSectorInfo;
import pl.projectcode.pocketsectors.nukkit.sector.Sector;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;

public class PacketSectorInfoPacketListener extends RedisPacketListener<PacketSectorInfo> {

    private final SectorManager sectorManager;

    public PacketSectorInfoPacketListener(SectorManager sectorManager) {
        super(PacketSectorInfo.class);

        this.sectorManager = sectorManager;
    }

    @Override
    public void handle(PacketSectorInfo packet) {
        Sector sector = this.sectorManager.getSector(packet.getSender());

        if(sector != null) {
            sector.setLastInfoPacket();
            sector.setTPS(packet.getTPS());
            sector.setPlayerCount(packet.getPlayerCount());
        }
    }
}
