package pl.projectcode.pocketsectors.nukkit;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.packet.PacketChannel;
import pl.projectcode.pocketsectors.common.packet.object.PacketConfigurationRequest;
import pl.projectcode.pocketsectors.common.packet.object.PacketSectorConnected;
import pl.projectcode.pocketsectors.common.packet.object.PacketSectorDisconnected;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.nukkit.command.SectorCommand;
import pl.projectcode.pocketsectors.nukkit.listener.block.BlockBreakListener;
import pl.projectcode.pocketsectors.nukkit.listener.block.BlockPlaceListener;
import pl.projectcode.pocketsectors.nukkit.listener.player.PlayerLocallyInitializedListener;
import pl.projectcode.pocketsectors.nukkit.listener.redis.*;
import pl.projectcode.pocketsectors.nukkit.listener.other.MoveListener;
import pl.projectcode.pocketsectors.nukkit.manager.TransferPlayerManager;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;
import pl.projectcode.pocketsectors.nukkit.task.SendSectorInfoTask;
import pl.projectcode.pocketsectors.nukkit.user.UserManager;
import pl.projectcode.pocketsectors.nukkit.util.Logger;

import java.util.Arrays;

@Getter
public class NukkitSector extends PluginBase {

    @Getter
    private static NukkitSector instance;

    private SectorManager sectorManager;
    private UserManager userManager;
    private TransferPlayerManager transferPlayerManager;
    private RedisManager redisManager;

    private boolean inited = false;

    @Override
    public void onEnable() {
        instance = this;

        super.saveDefaultConfig();

        this.sectorManager = new SectorManager(this, super.getConfig().getString("current-sector"));
        this.userManager = new UserManager();
        this.transferPlayerManager = new TransferPlayerManager();

        this.initRedisManager();
        this.initListeners();

        super.getServer().getCommandMap().register("pocketsectors", new SectorCommand(this));

        this.redisManager.publish(PacketChannel.PROXY, new PacketConfigurationRequest());

        Logger.info("Wlaczono!");
    }

    @Override
    public void onDisable() {
        this.redisManager.publish(PacketChannel.GLOBAL, new PacketSectorDisconnected());
        this.redisManager.shutdown();
    }

    public void init() {
        if(this.sectorManager.getCurrentSector() == null) {
            Logger.info("Aktualny sektor jest %MNULL%C, prawdopodobnie sektor o nazwie %M" + sectorManager.getCurrentSectorName() + " %Cnie zostal dodany do Configu w pluginie Proxy!");
            super.getServer().shutdown();
            return;
        }

        Logger.info("Zaladowano %M" + sectorManager.getSectors().size() + " %Csektorow!");

        if(!this.inited) {
            this.inited = true;

            super.getServer().getScheduler().scheduleRepeatingTask(this, new SendSectorInfoTask(this), 20);
        }

        this.redisManager.publish(PacketChannel.GLOBAL, new PacketSectorConnected());

        super.getServer().getOnlinePlayers().values().forEach(userManager::createUser);
    }

    private void initRedisManager() {
        this.redisManager = new RedisManager();
        this.redisManager.setPacketSender(sectorManager.getCurrentSectorName());

        Arrays.stream(new RedisPacketListener<?>[] {
                new PacketConfigurationPacketListener(this),
                new PacketTransferPlayerPacketListener(this.transferPlayerManager),
                new PacketPlayerInfoPacketListener()
        }).forEach(redisManager::subscribe);

        Arrays.stream(new RedisPacketListener<?>[] {
                new PacketExecuteCommandPacketListener(this),
                new PacketPlayerInfoRequestPacketListener(),
                new PacketPermissionBroadcastMessagePacketListener(),
                new PacketSectorInfoPacketListener(this.sectorManager)
        }).forEach(listener -> this.redisManager.subscribe(PacketChannel.SECTORS, listener));

        Arrays.stream(new RedisPacketListener<?>[] {
                new PacketSectorConnectedPacketListener(sectorManager),
                new PacketSectorDisconnectedPacketListener(sectorManager)
        }).forEach(listener -> this.redisManager.subscribe(PacketChannel.GLOBAL, listener));

        Logger.info("Zainicjalizowano %MRedisManagera");
    }

    private void initListeners() {
        Arrays.stream(new Listener[] {
                new PlayerLocallyInitializedListener(this),
                new BlockPlaceListener(this.sectorManager),
                new BlockBreakListener(this.sectorManager),
                new MoveListener(this),
        }).forEach(listener -> super.getServer().getPluginManager().registerEvents(listener, this));
    }
}
