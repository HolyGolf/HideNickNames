package hidenicknames.hidenicknames;

import org.bstats.bukkit.Metrics;
import hidenicknames.hidenicknames.commands.UpdateChecker;
import hidenicknames.hidenicknames.commands.commands;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public final class HideNickNames extends JavaPlugin implements Listener {

@Override
public void onEnable() {
	getServer().getPluginManager().registerEvents(this, this); /*Регистрация ивентов*/
	this.getLogger().info("HideNickNames on!"); /*Сообщение о включении плагина в консоли*/
	loadConfig(); /*Загрузка конфига*/
	metrcs();
	getServer().getPluginManager().registerEvents(new commands(), this); /*Регистрация ивентов в другом классе*/
	this.getCommand("hnames").setExecutor(new commands()); /*Команда hnames*/

	Logger logger = this.getLogger();

	new UpdateChecker(this, 77039).getVersion(version -> {
		if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
			logger.info("There is not a new update available.");
		} else {
			logger.info("New version " + version + " available at https://www.spigotmc.org/resources/hidenicknames.77039/");
		}
	});
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

public void metrcs(){
	int pluginId = 9666;
	Metrics metrics = new Metrics(this, pluginId);
	metrics.addCustomChart(new Metrics.SingleLineChart("players", new Callable<Integer>() {
		@Override
		public Integer call() throws Exception {
			return Bukkit.getOnlinePlayers().size();
		}
	}));
	metrics.addCustomChart(new Metrics.SingleLineChart("servers", new Callable<Integer>() {
		@Override
		public Integer call() throws Exception {
			return 1;
		}
	}));
}
}
