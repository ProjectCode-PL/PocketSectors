package pl.projectcode.pocketsectors.waterdog.listener.redis;

import dev.waterdog.waterdogpe.ProxyServer;
import pl.projectcode.pocketsectors.common.packet.object.PacketBroadcastMessage;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;

public class PacketBroadcastMessagePacketListener extends RedisPacketListener<PacketBroadcastMessage> {

    public PacketBroadcastMessagePacketListener() {
        super(PacketBroadcastMessage.class);
    }

    @Override
    public void handle(PacketBroadcastMessage packet) {
        ProxyServer.getInstance().getPlayers().values().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(packet.getMessage()));
    }
}
