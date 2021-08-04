package pl.projectcode.pocketsectors.nukkit.sector;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.projectcode.pocketsectors.common.packet.Packet;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.common.sector.SectorData;
import pl.projectcode.pocketsectors.common.sector.SectorType;
import pl.projectcode.pocketsectors.common.util.Vector3;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@RequiredArgsConstructor
public class Sector {

    private final SectorManager sectorManager = NukkitSector.getInstance().getSectorManager();

    private final SectorData sectorData;

    private long lastInfoPacket = -1;
    private float tps;
    private int playerCount = 0;

    public String getName() {
        return this.sectorData.getName();
    }

    public Vector3 getFirstCorner() {
        return this.sectorData.getFirstCorner();
    }

    public Vector3 getSecondCorner() {
        return this.sectorData.getSecondCorner();
    }

    public String getWorldName() {
        return this.sectorData.getWorld();
    }

    public SectorType getType() {
        return this.sectorData.getType();
    }

    public String getAddress() {
        return this.sectorData.getAddress();
    }

    public int getPort() {
        return this.sectorData.getPort();
    }

    public Vector3 getCenter() {
        return this.sectorData.getCenter();
    }

    public boolean isOnline() {
        return this.sectorData.isOnline();
    }

    public void setOnline(boolean online) {
        this.sectorData.setOnline(online);
    }

    public float getTPS() {
        return this.isOnline() ? tps : 0;
    }

    public void setTPS(float tps) {
        this.tps = tps;
    }

    public long getLastInfoPacket() {
        return this.lastInfoPacket == -1 ? TimeUnit.SECONDS.toMillis(-1) :  System.currentTimeMillis() - this.lastInfoPacket;
    }

    public void setLastInfoPacket() {
        this.lastInfoPacket = System.currentTimeMillis();
    }

    public boolean isInSector(Location loc) {
        return loc.getFloorX() <= Math.max(this.getFirstCorner().getX(), this.getSecondCorner().getX()) && loc.getFloorX() >= Math.min(this.getFirstCorner().getX(), this.getSecondCorner().getX()) && loc.getFloorZ() <= Math.max(this.getFirstCorner().getZ(), this.getSecondCorner().getZ()) && loc.getFloorZ() >= Math.min(this.getFirstCorner().getZ(), this.getSecondCorner().getZ()) && this.getWorldName().contains(loc.getLevel().getName());
    }

    public int getBorderDistance(Location loc) {
        double x1 = Math.abs(loc.getFloorX() - this.getFirstCorner().getX());
        double x2 = Math.abs(loc.getFloorX() - this.getSecondCorner().getX());
        double z1 = Math.abs(loc.getFloorZ() - this.getFirstCorner().getZ());
        double z2 = Math.abs(loc.getFloorZ() - this.getSecondCorner().getZ());

        return (int) Math.min(Math.min(x1, x2), Math.min(z1, z2));
    }

    public Sector getNearestSector(Location location) {
        return this.getNearestSector(getBorderDistance(location) + 1, location);
    }

    public Sector getNearestSector(int distance, Location location) {
        List<Sector> sectors = Arrays.asList(
                this.sectorManager.getSector(new Location(location.getX() + distance, location.getY(), location.getZ(), location.getLevel())),
                this.sectorManager.getSector(new Location(location.getX() - distance, location.getY(), location.getZ(), location.getLevel())),
                this.sectorManager.getSector(new Location(location.getX(), location.getY(), location.getZ() + distance, location.getLevel())),
                this.sectorManager.getSector(new Location(location.getX(), location.getY(), location.getZ() - distance, location.getLevel()))
        );

        for(Sector sector : sectors) {
            if(sector != null && sector.getWorldName().equals(this.getWorldName()) && !sector.getName().equals(this.getName())) {
                return sector;
            }
        }

        return null;
    }

    public void knockBorder(Player player) {
        cn.nukkit.math.Vector3 center = new cn.nukkit.math.Vector3(this.getCenter().getX(), this.getCenter().getY(), this.getCenter().getZ());
        double distance = player.getLocation().distance(center);
        cn.nukkit.math.Vector3 velocity = player.getLocation().subtract(center).add(new cn.nukkit.math.Vector3(0, 2, 0)).multiply(1 / distance);

        player.setMotion(velocity.multiply(-1));
    }

    public void sendPacket(Packet packet) {
        RedisManager.getInstance().publish(this.getName(), packet);
    }
}
