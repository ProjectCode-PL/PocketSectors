package pl.projectcode.pocketsectors.nukkit.util;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.TransferPacket;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WaterdogUtil {

    public static void transferPlayer(Player player, String address, int port) {
        TransferPacket transferPacket = new TransferPacket();
        transferPacket.address = address;
        transferPacket.port = port;

        player.dataPacket(transferPacket);
    }
}
