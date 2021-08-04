package pl.projectcode.pocketsectors.nukkit.listener.redis;

import cn.nukkit.Server;
import pl.projectcode.pocketsectors.common.packet.object.PacketSectorDisconnected;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.common.util.ChatUtil;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;
import pl.projectcode.pocketsectors.nukkit.util.Logger;

public class PacketSectorDisconnectedPacketListener extends RedisPacketListener<PacketSectorDisconnected> {

    private final SectorManager sectorManager;

    public PacketSectorDisconnectedPacketListener(SectorManager sectorManager) {
        super(PacketSectorDisconnected.class);

        this.sectorManager = sectorManager;
    }

    @Override
    public void handle(PacketSectorDisconnected packet) {
        String sectorName = packet.getSender();

        if(!sectorName.equalsIgnoreCase(this.sectorManager.getCurrentSectorName())) {
            String message = "%COdlaczono sektor %M" + sectorName + "%C!";

            Logger.info(message);

            Server.getInstance().getOnlinePlayers().values().forEach(player -> {
                if(player.hasPermission("pocketsectors.messages")) {
                    player.sendMessage(ChatUtil.fixColors(message));
                }
            });
        }

        this.sectorManager.getSector(sectorName).setOnline(false);
    }
}
