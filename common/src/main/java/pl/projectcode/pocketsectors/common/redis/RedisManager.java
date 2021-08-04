package pl.projectcode.pocketsectors.common.redis;

import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import pl.projectcode.pocketsectors.common.packet.Packet;

@Getter
@Setter
public class RedisManager {

    @Getter
    private static RedisManager instance;

    private final RedissonClient redisson;
    private final RList<String> onlinePlayersList;

    private String packetSender;

    public RedisManager() {
        this("localhost", 6379, "");
    }

    public RedisManager(String address, int port, String password) {
        if(instance != null) {
            throw new RuntimeException("Only one RedisManager instance can exist at once");
        }

        instance = this;

        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer().setAddress("redis://" + address + ":" + port);

        if(!password.isEmpty()) {
            serverConfig.setPassword(password);
        }

        this.redisson = Redisson.create(config);
        this.onlinePlayersList = this.redisson.getList("onlinePlayers", StringCodec.INSTANCE);
    }

    public void shutdown() {
        this.redisson.shutdown();
    }

    public void publish(String channel, Packet packet) {
        this.redisson.getTopic(channel).publish(packet);
    }

    public <T extends Packet> void subscribe(RedisPacketListener<T> listener) {
        this.subscribe(packetSender, listener);
    }

    public <T extends Packet> void subscribe(String channel, RedisPacketListener<T> listener) {
        // TODO: In the future implement FastSerialization (FST)
        this.redisson.getTopic(channel).addListener(listener.getPacketClass(), listener);
    }
}
