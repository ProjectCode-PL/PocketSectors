package pl.projectcode.pocketsectors.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import pl.projectcode.pocketsectors.common.object.PlayerInfo;
import pl.projectcode.pocketsectors.common.packet.PacketChannel;
import pl.projectcode.pocketsectors.common.redis.RedisManager;
import pl.projectcode.pocketsectors.nukkit.packet.PacketExecuteCommand;
import pl.projectcode.pocketsectors.common.util.ChatUtil;
import pl.projectcode.pocketsectors.common.util.StringUtil;
import pl.projectcode.pocketsectors.nukkit.NukkitSector;
import pl.projectcode.pocketsectors.nukkit.sector.Sector;
import pl.projectcode.pocketsectors.nukkit.sector.SectorManager;
import pl.projectcode.pocketsectors.nukkit.util.PlayerInfoUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SectorCommand extends Command {

    private final NukkitSector nukkitSector;

    public SectorCommand(NukkitSector nukkitSector) {
        super("sector");

        super.setDescription("Glowna komenda sektorow");
        super.setAliases(new String[]{"sectors", "sektor", "sektory"});
        super.setPermission("pocketsectors.command.sector");

        this.nukkitSector = nukkitSector;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!super.testPermission(sender)) {
            return false;
        }

        if(args.length == 0) {
            sender.sendMessage(ChatUtil.fixColors("&6/sector current &8 - &7Wyswietla aktualny sektor"));
            sender.sendMessage(ChatUtil.fixColors("&6/sector list &8 - &7Wyswietla liste sektorow"));
            sender.sendMessage(ChatUtil.fixColors("&6/sector cmd (komenda) &8 - &7Wysyla komende do wszystkich sektorow"));
            sender.sendMessage(ChatUtil.fixColors("&6/sector online (nick) &8 - &7Wysyla informacje, czy podany gracz jest online"));
            sender.sendMessage(ChatUtil.fixColors("&6/sector onlinePlayers &8 - &7Wysyla liste graczy online"));
            sender.sendMessage(ChatUtil.fixColors("&6/sector playerInfo (nick) &8 - &7Pobiera informacje o danym graczu"));
            return false;
        }

        SectorManager sectorManager = this.nukkitSector.getSectorManager();
        RedisManager redisManager = this.nukkitSector.getRedisManager();

        switch(args[0]) {
            case "current":
                sender.sendMessage(ChatUtil.fixColors("&7Aktualnie znajdujesz sie na sektorze &6" + sectorManager.getCurrentSectorName()));
                break;
            case "list":
                for(Sector sector : sectorManager.getSectors()) {
                    sender.sendMessage(ChatUtil.fixColors("&6" + sector.getName() + " &8(&7Status: " + (sector.isOnline() ? "&aOnline" : "&cOffline") + " &7TPS: &6" + sector.getTPS() + " &7Online: &6" + sector.getPlayerCount() + " &7Last info: &6" + TimeUnit.MILLISECONDS.toSeconds(sector.getLastInfoPacket()) + " &7seconds ago&8)"));
                }
                break;
            case "cmd":
                if(args.length == 1) {
                    sender.sendMessage(ChatUtil.fixColors("&7Uzycie: &6/sector cmd (komenda)"));
                    return false;
                }

                String command =  String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                redisManager.publish(PacketChannel.SECTORS, new PacketExecuteCommand(command));
                break;
            case "online":
                if(args.length == 1) {
                    sender.sendMessage(ChatUtil.fixColors("&7Uzycie: &6/sector online (nick)"));
                    return false;
                }

                sender.sendMessage(ChatUtil.fixColors("&7Gracz &6" + args[1] + " &7jest: " + (sectorManager.isPlayerOnline(args[1]) ? "&aONLINE" : "&cOFFLINE")));
                break;
            case "onlinePlayers":
                List<String> onlinePlayers = sectorManager.getOnlinePlayers();

                sender.sendMessage(ChatUtil.fixColors("&7Lista graczy online &8(&6" + onlinePlayers.size() + "&8)&7: &6" + StringUtil.join(onlinePlayers, "&7, &6")));
                break;
            case "playerInfo":
                if(args.length == 1) {
                    sender.sendMessage(ChatUtil.fixColors("&7Uzycie: &6/sector playerInfo (nick)"));
                    return false;
                }

                CompletableFuture<PlayerInfo> playerInfoFuture = PlayerInfoUtil.getPlayerInfo(args[1]);

                if(playerInfoFuture == null) {
                    sender.sendMessage(ChatUtil.fixColors("&cTen gracz jest offline!"));
                    return false;
                }

                sender.sendMessage(ChatUtil.fixColors("&aPobieranie informacji o graczu..."));

                playerInfoFuture.thenAccept(playerInfo -> {
                    sender.sendMessage(ChatUtil.fixColors("&7Nick: &6" + playerInfo.getPlayerName()));
                    sender.sendMessage(ChatUtil.fixColors("&7Sektor: &6" + playerInfo.getSectorName()));
                    sender.sendMessage(ChatUtil.fixColors("&7Pozycja: &6" + playerInfo.getPosition()));
                    sender.sendMessage(ChatUtil.fixColors("&7Gamemode: &6" + playerInfo.getGameMode()));
                    sender.sendMessage(ChatUtil.fixColors("&7Zycie: &6" + playerInfo.getHealth()));
                    sender.sendMessage(ChatUtil.fixColors("&7Poziom glodu: &6" + playerInfo.getFoodLevel()));
                    sender.sendMessage(ChatUtil.fixColors("&7Level: &6" + playerInfo.getExperienceLevel()));
                    sender.sendMessage(ChatUtil.fixColors("&7Exp: &6" + playerInfo.getExperience()));
                });
                break;
            default:
                sender.sendMessage(ChatUtil.fixColors("&cNieznany argument!"));
        }
        return false;
    }
}
