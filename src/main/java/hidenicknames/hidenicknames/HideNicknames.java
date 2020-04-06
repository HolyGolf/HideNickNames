package hidenicknames.hidenicknames;

import hidenicknames.hidenicknames.additional.HideClass;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class HideNicknames extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Plugin on!");

        loadConfig();

        getServer().getPluginManager().registerEvents(new HideClass(), this);
        this.getCommand("hnames").setExecutor(new HideClass());

    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin off!");
    }

    public void loadConfig() {
        File config1 = new File(getDataFolder() + File.separator + "config.yml");
        if (!config1.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }
}
