package hidenicknames.hidenicknames;

import hidenicknames.hidenicknames.commands.commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class HideNickNames extends JavaPlugin implements Listener {

@Override
public void onEnable() {
	getServer().getPluginManager().registerEvents(this, this); /*Регистрация ивентов*/
	this.getLogger().info("HideNickNames on!"); /*Сообщение о включении плагина в консоли*/
	loadConfig(); /*Загрузка конфига*/
	getServer().getPluginManager().registerEvents(new commands(), this); /*Регистрация ивентов в другом классе*/
	this.getCommand("hnames").setExecutor(new commands()); /*Команда hnames*/
}

@Override
public void onDisable() {
	this.getLogger().info("HideNickNames off!");
} /*Сообщение о выключении плагина*/

public void loadConfig() {
	File config = new File(getDataFolder() + File.separator + "config.yml");
	if (!config.exists()) {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
} /*Загрузка конфига*/
}
