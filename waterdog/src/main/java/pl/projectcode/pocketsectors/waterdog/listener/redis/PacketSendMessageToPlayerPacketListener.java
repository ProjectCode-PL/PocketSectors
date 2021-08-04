package pl.projectcode.pocketsectors.waterdog.listener.redis;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import pl.projectcode.pocketsectors.common.packet.object.PacketSendMessageToPlayer;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;

public class PacketSendMessageToPlayerPacketListener extends RedisPacketListener<PacketSendMessageToPlayer> {

    public PacketSendMessageToPlayerPacketListener() {
        super(PacketSendMessageToPlayer.class);
    }

    @Override
    public void handle(PacketSendMessageToPlayer packet) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(packet.getPlayerName());

        if(player != null) {
            player.sendMessage(packet.getMessage());
        }
    }
}
