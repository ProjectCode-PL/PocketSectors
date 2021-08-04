package pl.projectcode.pocketsectors.nukkit.listener.redis;

import cn.nukkit.Player;
import cn.nukkit.Server;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.nukkit.packet.PacketPlayerInfo;
import pl.projectcode.pocketsectors.nukkit.packet.PacketPlayerInfoRequest;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.nukkit.util.PlayerInfoUtil;

public class PacketPlayerInfoRequestPacketListener extends RedisPacketListener<PacketPlayerInfoRequest> {

    public PacketPlayerInfoRequestPacketListener() {
        super(PacketPlayerInfoRequest.class);
    }

    @Override
    public void handle(PacketPlayerInfoRequest packet) {
        Player player = Server.getInstance().getPlayerExact(packet.getPlayerName());

        if(player != null) {
            PacketPlayerInfo packetPlayerInfo = new PacketPlayerInfo(PlayerInfoUtil.createPlayerInfo(player));
            RedisManager.getInstance().publish(packet.getSender(), packetPlayerInfo);
        }
    }
}
