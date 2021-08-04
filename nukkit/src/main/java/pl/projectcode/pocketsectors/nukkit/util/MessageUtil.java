package pl.projectcode.pocketsectors.nukkit.util;

import lombok.experimental.UtilityClass;
import pl.projectcode.pocketsectors.common.packet.object.PacketBroadcastMessage;
import pl.projectcode.pocketsectors.common.packet.object.PacketBroadcastTitle;
import pl.projectcode.pocketsectors.common.packet.PacketChannel;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.nukkit.packet.PacketPermissionBroadcastMessage;
import pl.projectcode.pocketsectors.common.packet.object.PacketSendMessageToPlayer;

@UtilityClass
public class MessageUtil {

    private static final RedisManager redisManager = RedisManager.getInstance();

    public static void sendBroadcastMessage(String message) {
        redisManager.publish(PacketChannel.PROXY, new PacketBroadcastMessage(message));
    }

    public static void sendBroadcastPermissionMessage(String permission, String message) {
        redisManager.publish(PacketChannel.SECTORS, new PacketPermissionBroadcastMessage(permission, message));
    }

    public static void sendMessageToPlayer(String playerName, String message) {
        redisManager.publish(PacketChannel.PROXY, new PacketSendMessageToPlayer(playerName, message));
    }

    public static void sendBroadcastTitle(String title, String subTitle) {
        sendBroadcastTitle(title, subTitle, 20, 20, 20);
    }

    public static void sendBroadcastTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        redisManager.publish(PacketChannel.PROXY, new PacketBroadcastTitle(title, subTitle, fadeIn, stay, fadeOut));
    }
}
