package pl.projectcode.pocketsectors.nukkit.listener.redis;

import cn.nukkit.Server;
import pl.projectcode.pocketsectors.nukkit.packet.PacketPermissionBroadcastMessage;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;

public class PacketPermissionBroadcastMessagePacketListener extends RedisPacketListener<PacketPermissionBroadcastMessage> {

    public PacketPermissionBroadcastMessagePacketListener() {
        super(PacketPermissionBroadcastMessage.class);
    }

    @Override
    public void handle(PacketPermissionBroadcastMessage packet) {
        Server.getInstance().getOnlinePlayers().values().forEach(player -> {
            if(player.hasPermission(packet.getPermission())) {
                player.sendMessage(packet.getMessage());
            }
        });
    }
}
