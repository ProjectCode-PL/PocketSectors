package pl.projectcode.pocketsectors.nukkit.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.packet.Packet;

@AllArgsConstructor
@Getter
public class PacketPermissionBroadcastMessage extends Packet {

    private final String permission;
    private final String message;
}
