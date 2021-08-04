package pl.projectcode.pocketsectors.waterdog.listener.redis;

import pl.projectcode.pocketsectors.common.packet.object.PacketConfiguration;
import pl.projectcode.pocketsectors.common.packet.object.PacketConfigurationRequest;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.common.sector.SectorData;
import pl.projectcode.pocketsectors.waterdog.manager.SectorManager;
import pl.projectcode.pocketsectors.waterdog.util.Logger;

public class PacketConfigurationRequestPacketListener extends RedisPacketListener<PacketConfigurationRequest> {

    private final SectorManager sectorManager;

    public PacketConfigurationRequestPacketListener(SectorManager sectorManager) {
        super(PacketConfigurationRequest.class);

        this.sectorManager = sectorManager;
    }

    @Override
    public void handle(PacketConfigurationRequest packet) {
        Logger.info("Otrzymano zapytanie o pakiet konfiguracji od sektora %M" + packet.getSender());

        PacketConfiguration packetConfiguration = new PacketConfiguration(
                this.sectorManager.getSectorsData().toArray(new SectorData[0])
        );

        RedisManager.getInstance().publish(packet.getSender(), packetConfiguration);
    }
}
