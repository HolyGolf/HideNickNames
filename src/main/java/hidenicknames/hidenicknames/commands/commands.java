package hidenicknames.hidenicknames.commands;

import hidenicknames.hidenicknames.HideNickNames;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;


public class commands implements Listener, CommandExecutor {

private HideNickNames plugin = HideNickNames.getPlugin(HideNickNames.class);

public static HashMap<UUID, ArmorStand> hide = new HashMap<>();
public static HashMap<String, Boolean> plug = new HashMap<>();
public static int idt;

@Override
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	if (args.length == 0) /*Если нет аргументов*/ {
		sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "------{\nHideNickNames\n" + ChatColor.AQUA + "" + ChatColor.BOLD + "/hnames [on/off/reload]\n" + ChatColor.YELLOW + "" + ChatColor.BOLD + "}------"); /*Отображение списка команд в чате*/
		return true;
	} else if (sender.hasPermission("HideNicknames.switching") || sender.isOp()) /*Проверка на права*/ {
		if (cmd.getName().equalsIgnoreCase("hnames") && sender instanceof Player) /*Проверка на консоль*/ {
			if (args[0].equals("on")) /*Команда выключения ников*/ {
				sender.sendMessage(ChatColor.YELLOW + "HideNickNames: plugin turns on...");
				if (!plug.isEmpty()) {
					plug.remove("Enable");
				}
				for (Player online : Bukkit.getOnlinePlayers()) {
					String world = online.getWorld().getName();
					if (plugin.getConfig().getStringList("Enabled_Worlds").contains(world)) {
						hide(online);
					}
				}
				return true;
			} else if (args[0].equals("off")) /*Команда включения ников*/ {
				sender.sendMessage(ChatColor.YELLOW + "HideNickNames: plugin turns off...");
				if (plug.isEmpty()) {
					plug.put("Enable", false);
				}
				for (Player online : Bukkit.getOnlinePlayers()) {
					unhide(online);
				}
			} else if (args[0].equals("reload")) /*Команда включения ников*/ {
				for (Player p : Bukkit.getOnlinePlayers()) {
					unhide(p);
				}
				plugin.reloadConfig();
				plugin.getServer().getPluginManager().disablePlugin(plugin);
				plugin.getServer().getPluginManager().enablePlugin(plugin);
				for (Player p : Bukkit.getOnlinePlayers()) {
					check(p);
				}
				sender.sendMessage(ChatColor.GOLD + "[HideNickNames]: Plugin reloaded");
			} else {
				sender.sendMessage(ChatColor.YELLOW + "What?!"); /*Если человек с iq <10 и не может ввести либо "on" либо "off"*/
			}
		} else {
			sender.sendMessage(ChatColor.RED + "HideNickNames: Only players can use this commands!");
			/*Если команда активирована с консоли*/
			return true;
		}
	} else {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("NoPermissionsMessage")));
		return true;
	}
	return true;
}

public void hide(Player player){ /*Добавление стойки*/
	UUID plr = player.getUniqueId();
	if (!hide.containsKey(plr)) {
		ArmorStand stand = player.getWorld().spawn(new Location(player.getWorld(), 5, 5, 5), ArmorStand.class);
		stand.setVisible(false);
		stand.setGravity(false);
		stand.setInvulnerable(true);
		stand.setMarker(true);
		stand.setSmall(true);
		stand.setCollidable(false);
		stand.setCustomNameVisible(false);
		player.addPassenger(stand);
		hide.put(plr, stand);
	}
}

public void unhide(Player player) {
	ArmorStand stand = hide.remove(player.getUniqueId());
	if ( stand != null) {
		stand.remove();
	}
}

public void check(Player player) { /*Проверка и действие*/
	String world = player.getWorld().getName();
	if (plugin.getConfig().getBoolean("Hide_NameTags", true) && plugin.getConfig().getStringList("Enabled_Worlds").contains(world)) {
		if (plug.isEmpty()) { /*Скрытие ников при входе*/
			hide(player);
		} else { /*Отображение ников при входе*/
			unhide(player);
		}
	} else {
		unhide(player);
	}
}

/*-----------------------------------------------------------------------------*/
/*Проверка при входе человека и тыкание на людей (Можно и в людей, без разницы)*/
/*Проверка при смене мира, выходе с сервера, а так же смерти.*/
/*-----------------------------------------------------------------------------*/

@EventHandler
public void onPlayerQuit(PlayerQuitEvent event) {
	Player p = event.getPlayer();
	unhide(p);
}

@EventHandler
public void onPlayerRespawn(PlayerRespawnEvent event) {
	Player p = event.getPlayer();
	unhide(p);
	if (!hide.containsKey(p.getUniqueId())) {
		check(p);
	}
}

@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
	Player p = event.getPlayer();
	check(p);
	if (p.isOp()) {
		new UpdateChecker(plugin, 77039).getVersion(version -> {
			if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
				p.sendMessage(ChatColor.GOLD + "[HideNickNames]: New version " + version + " available at https://www.spigotmc.org/resources/hidenicknames.77039/");
			}
		});
	}
}

@EventHandler
public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event){
	Player p = event.getPlayer();
	unhide(p);
	if (!hide.containsKey(p.getUniqueId())) {
		check(p);
	}
}

@EventHandler
public void onMove(PlayerMoveEvent event) {
	Player p = event.getPlayer();
	if (event.getTo().getBlock().getType().equals((Material.END_PORTAL)) || event.getTo().getBlock().getType().equals(Material.NETHER_PORTAL)) {
		unhide(p);
	} else {
		if (!hide.containsKey(p.getUniqueId()) && plug.isEmpty()) {
			check(p);
		}
	}
}

@EventHandler
public void onUnmount(EntityDismountEvent event){
	if(event.getEntity() instanceof ArmorStand){
		if (event.getDismounted() instanceof Player) {
			if (((Player) event.getDismounted()).getPlayer().isSwimming() || ((Player) event.getDismounted()).getPlayer().getLocation().getBlock().isLiquid()) {
				event.setCancelled(true);
			}
		}
	}
}

@EventHandler
public void onSpellPlayer(PlayerInteractEntityEvent event) {
	if (plugin.getConfig().getBoolean("Show_NameTags", true)) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (entity instanceof Player) {
			Player ent = (Player) entity;
			String world = player.getWorld().getName();
			if (plugin.getConfig().getStringList("Enabled_Worlds").contains(world)) {
				String mess1 = plugin.getConfig().getString("NameTags_show").replaceAll("<player>", ent.getDisplayName());
				String message = ChatColor.translateAlternateColorCodes('&', mess1);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
			}
		}
	}
}
}