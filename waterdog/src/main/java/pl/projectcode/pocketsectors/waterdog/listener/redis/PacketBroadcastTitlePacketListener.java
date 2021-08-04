package pl.projectcode.pocketsectors.waterdog.listener.redis;

import dev.waterdog.waterdogpe.ProxyServer;
import pl.projectcode.pocketsectors.common.packet.object.PacketBroadcastTitle;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;

public class PacketBroadcastTitlePacketListener extends RedisPacketListener<PacketBroadcastTitle> {

    public PacketBroadcastTitlePacketListener() {
        super(PacketBroadcastTitle.class);
    }

    @Override
    public void handle(PacketBroadcastTitle packet) {
        ProxyServer.getInstance().getPlayers().values().forEach(proxiedPlayer -> proxiedPlayer.sendTitle(packet.getTitle(), packet.getSubTitle(), packet.getFadeIn(), packet.getStay(), packet.getFadeOut()));
    }
}
