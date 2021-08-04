package pl.projectcode.pocketsectors.nukkit.listener.redis;

import cn.nukkit.command.ConsoleCommandSender;
import pl.projectcode.pocketsectors.nukkit.packet.PacketExecuteCommand;
import pl.projectcode.pocketsectors.common.redis.RedisPacketListener;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;

public class PacketExecuteCommandPacketListener extends RedisPacketListener<PacketExecuteCommand> {

    private final NukkitSector nukkitSector;

    public PacketExecuteCommandPacketListener(NukkitSector nukkitSector) {
        super(PacketExecuteCommand.class);

        this.nukkitSector = nukkitSector;
    }

    @Override
    public void handle(PacketExecuteCommand packet) {
        this.nukkitSector.getServer().getScheduler().scheduleDelayedTask(nukkitSector, () -> nukkitSector.getServer().dispatchCommand(new ConsoleCommandSender(), packet.getCommand()), 0);
    }
}
