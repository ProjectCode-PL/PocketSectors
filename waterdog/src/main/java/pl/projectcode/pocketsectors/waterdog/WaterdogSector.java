package pl.projectcode.pocketsectors.waterdog;

import dev.waterdog.waterdogpe.event.EventManager;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import pl.projectcode.pocketsectors.common.packet.PacketChannel;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.common.sector.SectorData;
import pl.projectcode.pocketsectors.common.sector.SectorType;
import pl.projectcode.pocketsectors.common.util.StringUtil;
import pl.projectcode.pocketsectors.common.util.Vector3;
import pl.projectcode.pocketsectors.waterdog.listener.redis.*;
import pl.projectcode.pocketsectors.waterdog.manager.SectorManager;
import pl.projectcode.pocketsectors.waterdog.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class WaterdogSector extends Plugin {

    private static WaterdogSector instance;

    private SectorManager sectorManager;
    private RedisManager redisManager;

    @Override
    public void onEnable() {
        instance = this;

        super.saveResource("config.yml");

        this.sectorManager = new SectorManager();

        this.loadSectors();
        this.initRedisManager();
        this.initListeners();

        Logger.info("Uruchomiono!");
    }

    @Override
    public void onDisable() {
        this.redisManager.shutdown();
    }

    private void loadSectors() {
        LinkedHashMap<String, LinkedHashMap<String, Object>> sectors = (LinkedHashMap<String, LinkedHashMap<String, Object>>) super.getConfig().get("sectors");

        sectors.forEach((sectorName, sectorData) -> {
            if(super.getProxy().getServerInfo(sectorName) == null) {
                Logger.info("Nie udalo zasisektora &c" + sectorName + "&6: &cNie znaleziono serwera Waterdog!");
                return;
            }

            int x1 = (int) sectorData.get("x1");
            int z1 = (int) sectorData.get("z1");
            int x2 = (int) sectorData.get("x2");
            int z2 = (int) sectorData.get("z2");
            String worldName = (String) sectorData.get("world");
            String[] splittedAddress = ((String) sectorData.get("address")).split(":");
            SectorType sectorType;

            try {
                sectorType = SectorType.valueOf((String) sectorData.get("type"));
            } catch (IllegalArgumentException ignored) {
                Logger.info("Nie udalo zasisektora &c" + sectorName + "&6: &cNieprawidlowy typ sektora! (Do wyboru: SPAWN, SECTOR)");
                return;
            }

            Vector3 firstCorner = new Vector3(x1, 0.0, z1);
            Vector3 secondCorner = new Vector3(x2, 0.0, z2);

            this.sectorManager.addSectorData(new SectorData(sectorName, firstCorner, secondCorner, worldName, sectorType, splittedAddress[0], Integer.parseInt(splittedAddress[1])));
        });

        List<String> sectorNames = new ArrayList<>();

        this.sectorManager.getSectorsData().forEach(sectorData -> sectorNames.add(sectorData.getName()));

        Logger.info("Zaladowano %M" + sectorNames.size() + " %Csektorow (%M" + StringUtil.join(sectorNames, "%C, %M") + "%C)");
    }

    private void initRedisManager() {
        this.redisManager = new RedisManager();
        this.redisManager.setPacketSender(PacketChannel.PROXY);

        Arrays.stream(new RedisPacketListener<?>[] {
                new PacketConfigurationRequestPacketListener(this.sectorManager),
                new PacketBroadcastMessagePacketListener(),
                new PacketSendMessageToPlayerPacketListener(),
                new PacketBroadcastTitlePacketListener()
        }).forEach(this.redisManager::subscribe);

        Arrays.stream(new RedisPacketListener<?>[] {
                new PacketSectorConnectedPacketListener(this.sectorManager),
                new PacketSectorDisconnectedPacketListener(this.sectorManager)
        }).forEach(listener -> this.redisManager.subscribe(PacketChannel.GLOBAL, listener));

        Logger.info("Zainicjalizowano %MRedisManagera");
    }

    private void initListeners() {
        EventManager eventManager = super.getProxy().getEventManager();

        eventManager.subscribe(PlayerLoginEvent.class, this::onPlayerLogin);
        eventManager.subscribe(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    public void onPlayerLogin(PlayerLoginEvent event) {
        this.redisManager.getOnlinePlayersList().add(event.getPlayer().getName());
    }

    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        this.redisManager.getOnlinePlayersList().remove(event.getPlayer().getName());
    }
}
