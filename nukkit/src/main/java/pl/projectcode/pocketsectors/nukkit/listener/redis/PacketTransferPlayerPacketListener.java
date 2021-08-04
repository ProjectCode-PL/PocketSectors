package pl.projectcode.pocketsectors.nukkit.listener.redis;

import pl.projectcode.pocketsectors.nukkit.packet.PacketTransferPlayer;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.nukkit.manager.TransferPlayerManager;

public class PacketTransferPlayerPacketListener extends RedisPacketListener<PacketTransferPlayer> {

    private final TransferPlayerManager transferPlayerManager;

    public PacketTransferPlayerPacketListener(TransferPlayerManager transferPlayerManager) {
        super(PacketTransferPlayer.class);

        this.transferPlayerManager = transferPlayerManager;
    }

    @Override
    public void handle(PacketTransferPlayer packet) {
        this.transferPlayerManager.setPacketTransferPlayer(packet);
    }
}
