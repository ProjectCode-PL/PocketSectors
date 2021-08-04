package pl.projectcode.pocketsectors.common.packet;

import lombok.Getter;
import pl.projectcode.pocketsectors.common.redis.RedisManager;

import java.io.Serializable;

@Getter
public abstract class Packet implements Serializable {

    private static final RedisManager redisManager = RedisManager.getInstance();

    private final String sender;

    public Packet() {
        this.sender = redisManager.getPacketSender();
    }
}
