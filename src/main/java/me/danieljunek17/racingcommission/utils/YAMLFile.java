package me.danieljunek17.racingcommission.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YAMLFile extends YamlConfiguration {

    private String path;
    private Plugin plugin;
    private File file;

    public YAMLFile(String path, JavaPlugin plugin) {
        this.plugin = plugin;
        this.path = path;
        this.file = new File(plugin.getDataFolder() + "/" + path);
        this.file.getParentFile().mkdirs();
    }

    public void loadFile() {
        loadConfiguration(file);

        try {
            if(file.exists()) {
                load(file);
            }else {
                options().copyDefaults(true);
                save(file);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultFile() {
        if (!file.exists()) plugin.saveResource(path, false);
        loadConfiguration(file);

        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}