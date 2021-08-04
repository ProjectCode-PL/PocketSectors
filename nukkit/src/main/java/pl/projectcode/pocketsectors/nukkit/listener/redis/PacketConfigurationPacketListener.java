package pl.projectcode.pocketsectors.nukkit.listener.redis;

import pl.projectcode.pocketsectors.common.packet.object.PacketConfiguration;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;
import pl.projectcode.pocketsectors.nukkit.util.Logger;

public class PacketConfigurationPacketListener extends RedisPacketListener<PacketConfiguration> {

    private final NukkitSector nukkitSector;

    public PacketConfigurationPacketListener(NukkitSector nukkitSector) {
        super(PacketConfiguration.class);

        this.nukkitSector = nukkitSector;
    }

    @Override
    public void handle(PacketConfiguration packet) {
        Logger.info("Otrzymano pakiet konfiguracji!");

        this.nukkitSector.getSectorManager().loadSectorsData(packet.getSectorsData());
        this.nukkitSector.init();
    }
}
