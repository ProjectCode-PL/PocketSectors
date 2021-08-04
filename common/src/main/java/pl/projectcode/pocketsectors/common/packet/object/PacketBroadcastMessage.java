package pl.projectcode.pocketsectors.common.packet.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.packet.Packet;

@AllArgsConstructor
@Getter
public class PacketBroadcastMessage extends Packet {

    private final String message;
}
