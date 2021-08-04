package pl.projectcode.pocketsectors.nukkit.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.object.PlayerInfo;
import pl.projectcode.pocketsectors.common.packet.Packet;

@AllArgsConstructor
@Getter
public class PacketTransferPlayer extends Packet {

    private final PlayerInfo playerInfo;
}
