package hidenicknames.hidenicknames.additional;

import hidenicknames.hidenicknames.HideNicknames;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;


public class HideClass implements Listener, CommandExecutor {

    private HideNicknames plugin = HideNicknames.getPlugin(HideNicknames.class);

    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();
    Objective obj = board.registerNewObjective("MCBR", "dummy");
    Team nameless = board.registerNewTeam("nameless");

    ScoreboardManager manager2 = Bukkit.getScoreboardManager();
    Scoreboard board2 = manager2.getNewScoreboard();
    Objective obj2 = board2.registerNewObjective("MCBR2", "dummy2");
    Team name = board2.registerNewTeam("name");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        nameless.setNameTagVisibility(NameTagVisibility.NEVER);
        name.setNameTagVisibility(NameTagVisibility.ALWAYS);
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "------HideNickName Commands------\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/HNames [on/off]\n" + ChatColor.YELLOW + "" + ChatColor.BOLD + "---------------------------------");
            return true;
        } else if (sender.hasPermission("HideNicknames.switching") || sender.isOp()) {
            if (cmd.getName().equalsIgnoreCase("hnames") && sender instanceof Player) {
                if (args[0].equals("on")) {
                    sender.sendMessage(ChatColor.YELLOW + "Player nicknames turn off...");
                    for (Player online : Bukkit.getOnlinePlayers()) {
                            nameless.addPlayer(online);
                            online.setScoreboard(board);
                    }
                    return true;
                } else if (args[0].equals("off")) {
                    sender.sendMessage(ChatColor.YELLOW + "Player nicknames turn on...");
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        name.addPlayer(online);
                        online.setScoreboard(board2);
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "What?!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this commands!");
                return true;
            }
        } else {
            sender.sendMessage(plugin.getConfig().getString("NoPermissionsMessage"));
            return true;
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("Hide_NameTags", true)) {
            Player p = event.getPlayer();
                nameless.setNameTagVisibility(NameTagVisibility.NEVER);
                for (Player online : Bukkit.getOnlinePlayers()) {
                    nameless.addPlayer(online);
                    online.setScoreboard(board);
            }
        }
    }

    @EventHandler
    public void onSpellPlayer(PlayerInteractEntityEvent event) {
        if (plugin.getConfig().getBoolean("Show_NameTags", true)) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            Player ent = (Player) entity;
            if (entity instanceof Player == true) {
                String message = ChatColor.GOLD + "" + ChatColor.BOLD + "-=[" + ChatColor.AQUA + "" + ChatColor.BOLD + ent.getDisplayName() + ChatColor.GOLD + "" + ChatColor.BOLD + "]=-";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            }
        }
    }
}
