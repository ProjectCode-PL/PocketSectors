package pl.projectcode.pocketsectors.nukkit.listener.redis;

import pl.projectcode.pocketsectors.common.object.PlayerInfo;
import pl.projectcode.pocketsectors.nukkit.packet.PacketPlayerInfo;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.nukkit.util.PlayerInfoUtil;

import java.util.concurrent.CompletableFuture;

public class PacketPlayerInfoPacketListener extends RedisPacketListener<PacketPlayerInfo> {

    public PacketPlayerInfoPacketListener() {
        super(PacketPlayerInfo.class);
    }

    @Override
    public void handle(PacketPlayerInfo packet) {
        PlayerInfo playerInfo = packet.getPlayerInfo();
        CompletableFuture<PlayerInfo> completableFuture = PlayerInfoUtil.getPlayerInfoCompletableFuture(playerInfo.getPlayerName());

        if(completableFuture != null) {
            completableFuture.complete(playerInfo);
        }
    }
}
