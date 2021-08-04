package pl.projectcode.pocketsectors.nukkit.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import lombok.RequiredArgsConstructor;
import pl.projectcode.pocketsectors.common.packet.PacketChannel;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;
import pl.projectcode.pocketsectors.nukkit.packet.PacketSectorInfo;

@RequiredArgsConstructor
public class SendSectorInfoTask extends Task {

    private final NukkitSector nukkitSector;

    @Override
    public void onRun(int currentTick) {
        Server server = this.nukkitSector.getServer();
        this.nukkitSector.getRedisManager().publish(PacketChannel.SECTORS, new PacketSectorInfo(server.getTicksPerSecond(), server.getOnlinePlayers().size()));
    }
}
