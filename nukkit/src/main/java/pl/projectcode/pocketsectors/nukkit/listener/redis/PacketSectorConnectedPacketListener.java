package pl.projectcode.pocketsectors.nukkit.listener.redis;

import cn.nukkit.Server;
import pl.projectcode.pocketsectors.common.packet.object.PacketSectorConnected;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.common.util.ChatUtil;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;
import pl.projectcode.pocketsectors.nukkit.util.Logger;

public class PacketSectorConnectedPacketListener extends RedisPacketListener<PacketSectorConnected> {

    private final SectorManager sectorManager;

    public PacketSectorConnectedPacketListener(SectorManager sectorManager) {
        super(PacketSectorConnected.class);

        this.sectorManager = sectorManager;
    }

    @Override
    public void handle(PacketSectorConnected packet) {
        String sectorName = packet.getSender();

        if(!sectorName.equalsIgnoreCase(this.sectorManager.getCurrentSectorName())) {
            String message = "%CPodlaczono sektor %M" + sectorName + "%C!";

            Logger.info(message);

            Server.getInstance().getOnlinePlayers().values().forEach(player -> {
                if(player.hasPermission("pocketsectors.messages")) {
                    player.sendMessage(ChatUtil.fixColors(message));
                }
            });
        }

        this.sectorManager.getSector(sectorName).setOnline(true);
    }
}
