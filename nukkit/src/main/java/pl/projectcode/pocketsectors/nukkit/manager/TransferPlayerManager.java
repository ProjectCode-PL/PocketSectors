package pl.projectcode.pocketsectors.nukkit.manager;

import pl.projectcode.pocketsectors.nukkit.packet.PacketTransferPlayer;

import java.util.HashMap;
import java.util.Map;

public class TransferPlayerManager {

    private final Map<String, PacketTransferPlayer> transferPlayerPackets = new HashMap<>();

    public void setPacketTransferPlayer(PacketTransferPlayer packet) {
        this.transferPlayerPackets.put(packet.getPlayerInfo().getPlayerName(), packet);
    }

    public PacketTransferPlayer getPacketTransferPlayer(String playerName) {
        return this.transferPlayerPackets.get(playerName);
    }

    public void removePacketTransferPlayer(String playerName) {
        this.transferPlayerPackets.remove(playerName);
    }
}
