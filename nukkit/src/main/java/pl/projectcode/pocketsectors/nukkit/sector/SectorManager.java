package pl.projectcode.pocketsectors.nukkit.sector;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.projectcode.pocketsectors.common.sector.SectorData;
import pl.projectcode.pocketsectors.common.sector.SectorType;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;
import pl.projectcode.pocketsectors.nukkit.packet.PacketTransferPlayer;
import pl.projectcode.pocketsectors.nukkit.util.PlayerInfoUtil;
import pl.projectcode.pocketsectors.nukkit.util.WaterdogUtil;

import java.util.*;

@RequiredArgsConstructor
public class SectorManager {

    private final Map<String, Sector> sectors = new HashMap<>();

    private final NukkitSector nukkitSector;
    @Getter
    private final String currentSectorName;

    public void addSector(SectorData sectorData) {
        this.addSector(new Sector(sectorData));
    }

    public void addSector(Sector sector) {
        this.sectors.put(sector.getName(), sector);
    }

    public void loadSectorsData(SectorData[] sectorsData) {
        for(SectorData sectorData : sectorsData) {
            this.addSector(sectorData);
        }
    }

    public Sector getSector(String sectorName) {
        return this.sectors.get(sectorName);
    }

    public Sector getSector(Location location) {
        Sector currentSector = this.getCurrentSector();

        for(Sector sector : sectors.values()) {
            if(sector.isInSector(location)) {
                if(currentSector.getType() != SectorType.SPAWN) {
                    return sector;
                } else if(sector.getType() != SectorType.SPAWN) {
                    return sector;
                }
            }
        }

        return null;
    }

    public void transferToSector(Player player, Sector sector) {
        PacketTransferPlayer packetTransferPlayer = new PacketTransferPlayer(PlayerInfoUtil.createPlayerInfo(player));

        sector.sendPacket(packetTransferPlayer);
        WaterdogUtil.transferPlayer(player, sector.getAddress(), sector.getPort());
    }

    public Sector getCurrentSector() {
        return this.getSector(currentSectorName);
    }

    public Collection<Sector> getSectors() {
        return this.sectors.values();
    }

    public List<String> getOnlinePlayers() {
        return this.nukkitSector.getRedisManager().getOnlinePlayersList().readAll();
    }

    public boolean isPlayerOnline(String playerName) {
        return this.getOnlinePlayers().contains(playerName);
    }
}
