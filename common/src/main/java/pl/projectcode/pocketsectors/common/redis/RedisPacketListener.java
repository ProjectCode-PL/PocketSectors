package pl.projectcode.pocketsectors.common.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.redisson.api.listener.MessageListener;
import pl.projectcode.pocketsectors.common.packet.Packet;

@Getter
@AllArgsConstructor
public abstract class RedisPacketListener<T extends Packet> implements MessageListener<T> {

    private final Class<T> packetClass;

    @Override
    public void onMessage(CharSequence channel, T packet) {
        this.handle(packet);
    }

    public abstract void handle(T packet);
}
