package me.danieljunek17.racingcommission;

import me.danieljunek17.racingcommission.commands.Rain;
import me.danieljunek17.racingcommission.events.PlayerInteractListener;
import me.danieljunek17.racingcommission.events.VehicleEnterListener;
import me.danieljunek17.racingcommission.events.VehicleSpawnListener;
import me.danieljunek17.racingcommission.events.WeatherListener;
import me.danieljunek17.racingcommission.objects.*;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import me.danieljunek17.racingcommission.utils.commands.CommandHandler;
import me.danieljunek17.racingcommission.utils.commands.subcommands.SubCommandHandler;
import me.legofreak107.vehiclesplus.VehiclesPlus;
import me.legofreak107.vehiclesplus.libraries.nbtsaving.NBTDataType;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.SpawnedVehicle;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.VehicleStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class Racingcommission extends JavaPlugin {

    private static YAMLFile messages, settings, data;
    private static Racingcommission instance;
    public static CommandHandler commandHandler;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationSerialization.registerClass(WheelsData.class);
        loadYAML();
        if(!initializeSettings()) return;
        commandHandler = new CommandHandler();
        commandHandler.registerCommands();

        getCommand("regen").setExecutor(new Rain());

        Bukkit.getPluginManager().registerEvents(new VehicleSpawnListener(), instance);
        Bukkit.getPluginManager().registerEvents(new VehicleEnterListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), instance);
        Bukkit.getPluginManager().registerEvents(new WeatherListener(), instance);

        reloadVehicles();
        runBatteryDrain();
        checkBlocksUnderVehicle();
        runWheelWorn();
    }

    @Override
    public void onDisable() {
        CommandHandler.command.clear();
        SubCommandHandler.commands.clear();
        Team.Manager.teamdata.clear();
        SpeedLimitData.Manager.speedLimits.clear();
    }

    public void loadYAML() {
        // YAML Files
        messages = new YAMLFile("Messages.yml", this);
        messages.loadDefaultFile();
        settings = new YAMLFile("Settings.yml", this);
        settings.loadDefaultFile();
        data = new YAMLFile("Data.yml", this);
        data.loadFile();
    }

    public boolean initializeSettings() {
        if(!settings.contains("teams")) {
            Bukkit.broadcastMessage(Utils.color("&4Disabling plugin, no teams or wrong config for teams"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }
        if(!settings.isConfigurationSection("teams")) {
            Bukkit.broadcastMessage(Utils.color("&4Disabling plugin, no teams or wrong config for teams"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }
        try {
            for (String id : settings.getConfigurationSection("teams").getKeys(false)) {
                String permission = settings.getString("teams." + id);
                Team.Manager.newTeam(new Team(permission.substring(5), permission, new ArrayList<>()));
            }
        } catch (Exception exception) {
            Bukkit.broadcastMessage(Utils.color("&4Disabling plugin, no teams or wrong config for teams"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }

        if(settings.getConfigurationSection("settings.wheels") == null) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&4Disabling plugin, no wheel setting or wrong config for wheels"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }
        for(String state : settings.getConfigurationSection("settings.wheels").getKeys(false)) {
            int durability = settings.getInt("settings.wheels." + state + ".durability");
            int speed = settings.getInt("settings.wheels." + state + ".speed");
            String displayname = settings.getString("settings.wheels." + state + ".displayname");
            String material = settings.getString("settings.wheels." + state + ".material");
            WheelsData.Manager.newWheelsData(state, new WheelsData(state, displayname, durability, speed, material));
        }

        if(settings.getConfigurationSection("settings.batterij") == null) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&4Disabling plugin, no battery setting or wrong config for batteries"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }
        for(String state : settings.getConfigurationSection("settings.batterij").getKeys(false)) {
            double charge = settings.getDouble("settings.batterij." + state + ".charge");
            int speed = settings.getInt("settings.batterij." + state + ".speed");
            String displayname = settings.getString("settings.batterij." + state + ".displayname");
            String material = settings.getString("settings.batterij." + state + ".material");
            BatteryState.Manager.newBatteryState(state, new BatteryState(speed, state, charge, displayname, material));
        }

        if(settings.getConfigurationSection("settings.fuel") == null) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&4Disabling plugin, no fuel setting or wrong config for fuel setting"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }
        for(String state : settings.getConfigurationSection("settings.fuel").getKeys(false)) {
            double usage = settings.getDouble("settings.fuel." + state + ".usage");
            int speed = settings.getInt("settings.fuel." + state + ".speed");
            String displayname = settings.getString("settings.fuel." + state + ".displayname");
            String material = settings.getString("settings.fuel." + state + ".material");
            FuelState.Manager.newFuelState(state, new FuelState(speed, state, usage, displayname, material));
        }

        if(settings.getConfigurationSection("settings.speedlimit") == null) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&4Disabling plugin, no speed limit setting or wrong config for speed limits"));
            instance.getPluginLoader().disablePlugin(this);
            return false;
        }
        for(String state : settings.getConfigurationSection("settings.speedlimit").getKeys(false)) {
            int speed = settings.getInt("settings.speedlimit." + state);
            SpeedLimitData.Manager.newSpeedLimit(new SpeedLimitData(state, speed));
        }
        return true;
    }

    public void reloadVehicles() {
        try {
            for (ArmorStand stand : VehiclesPlus.getVehicleManager().getHolderStands()) {
                SpawnedVehicle spawnedVehicle = (SpawnedVehicle) stand.getMetadata(NBTDataType.VEHICLE_BASE.name()).get(0).value();
                for (Team team : Team.Manager.teamdata) {
                    Player player = Bukkit.getPlayer(UUID.fromString(spawnedVehicle.getStorageVehicle().getOwner()));

                    Vector location = spawnedVehicle.getStorageVehicle().getLocation();
                    Location location1 = new Location(player.getWorld(), location.getBlockX(), (location.getBlockY() - 1), location.getBlockZ());

                    boolean offgrid = SpeedLimitData.Manager.speedLimits.containsKey(location1.getBlock().getType());
                    if ((player.hasPermission(team.getPermission()) && !player.hasPermission(Team.Manager.teamdata.get(0).getPermission())) || (player.hasPermission(team.getPermission()) && team.getPermission().equals(Team.Manager.teamdata.get(0).getPermission()))) {
                        VehicleData vehicleData;
                        if(!data.contains(spawnedVehicle.getStorageVehicle().getUuid() + ".wheelData")) {
                            vehicleData = new VehicleData(spawnedVehicle.getBaseVehicle(), spawnedVehicle.getStorageVehicle(), data.getInt(spawnedVehicle.getStorageVehicle().getUuid() + ".speed"), new ItemStack(Material.AIR), null);
                        } else {
                            vehicleData = new VehicleData(spawnedVehicle.getBaseVehicle(), spawnedVehicle.getStorageVehicle(), data.getInt(spawnedVehicle.getStorageVehicle().getUuid() + ".speed"), data.getItemStack(spawnedVehicle.getStorageVehicle().getUuid() + ".wheelItem"), (WheelsData) data.get(spawnedVehicle.getStorageVehicle().getUuid() + ".wheelData"));
                        }
                        vehicleData.setOffgrid(offgrid, SpeedLimitData.Manager.speedLimits.get(location1.getBlock().getType()));
                        Bukkit.getConsoleSender().sendMessage(String.valueOf(vehicleData.getWheelboost()));
                        vehicleData.getStorageVehicle().getVehicleStats().setSpeed(vehicleData.getCachespeed() + vehicleData.getWheelboost());
                        team.getVehicleDataList().add(vehicleData);
                    }
                }
            }
        }catch (NullPointerException ignored) {}
    }

    public void runBatteryDrain() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for(Team team : Team.Manager.teamdata) {
                for(VehicleData vehicleData : team.getVehicleDataList()) {
                    BatteryState batteryState = vehicleData.getBatteryState();
                    VehicleStats vehicleStats = vehicleData.getStorageVehicle().getVehicleStats();
                    if(vehicleData.getBatteryPercentage() + batteryState.getCharge() > 100) {
                        vehicleData.setBatteryPercentage(100);
                        vehicleStats.setSpeed(vehicleStats.getSpeed() - BatteryState.BatteryStateEnum.CHARGING.getState().getSpeed());
                        vehicleData.setBatteryState(BatteryState.Manager.getBatteryState("standard").join());
                    } else if(vehicleData.getBatteryPercentage() + batteryState.getCharge() >= 0) {
                        vehicleData.setBatteryPercentage(vehicleData.getBatteryPercentage() + batteryState.getCharge());
                    } else {
                        vehicleData.setBatteryPercentage(0);
                        vehicleStats.setSpeed(vehicleStats.getSpeed() - BatteryState.BatteryStateEnum.BOOST.getState().getSpeed());
                        vehicleData.setBatteryState(BatteryState.Manager.getBatteryState("standard").join());
                    }
                    vehicleData.getBatteryMenu().draw();
                }
            }
        }, 0L, 20L);
    }

    public void runWheelWorn() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for(Team team : Team.Manager.teamdata) {
                for(VehicleData vehicleData : team.getVehicleDataList()) {
                    if(vehicleData.getWheelsData() != null) {
                        ItemStack item = vehicleData.getWheelsItem().clone();
                        ItemMeta itemMeta = item.getItemMeta();
                        String lore = item.getItemMeta().getLore().get(0);
                        String numberedlore = lore.replaceAll("\\D+","");
                        String durabilitystring = numberedlore.substring(0, numberedlore.length() - String.valueOf(vehicleData.getWheelsData().getMaxDurability()).length());
                        int durability = Integer.parseInt(durabilitystring) - 1;
                        if(Integer.parseInt(durabilitystring) == 0) {
                            vehicleData.getStorageVehicle().getVehicleStats().setSpeed(settings.getInt("settings.brokenspeed"));
                            continue;
                        }
                        itemMeta.setLore(Collections.singletonList("Durability: " + durability + "/" + vehicleData.getWheelsData().getMaxDurability()));
                        item.setItemMeta(itemMeta);
                        vehicleData.setWheelsItem(item);
                        vehicleData.getChangeMenu().draw();
                    }
                }
            }
        }, 0L, 20L);
    }

    public void checkBlocksUnderVehicle() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for(Team team : Team.Manager.teamdata) {
                for (VehicleData vehicleData : team.getVehicleDataList()) {
                    Vector vector = vehicleData.getStorageVehicle().getLocation();
                    Player owner = Bukkit.getPlayer(UUID.fromString(vehicleData.getStorageVehicle().getOwner()));
                    Material material = new Location(owner.getWorld(), vector.getBlockX(), vector.getBlockY() - 1, vector.getBlockZ()).getBlock().getType();
                    if(SpeedLimitData.Manager.speedLimits.containsKey(material)) {
                        SpeedLimitData speedLimitData = SpeedLimitData.Manager.speedLimits.get(material);
                        if(vehicleData.getStorageVehicle().getVehicleStats().getSpeed() != speedLimitData.getSpeedLimit()) {
                            vehicleData.enableOffGrid(speedLimitData);
                        }
                    }else if(vehicleData.isOffgrid()) {
                        vehicleData.disableOffGrid();
                    }
                }
            }
        }, 0L, 10L);
    }

    public static Racingcommission getInstance() {
        return instance;
    }

    public static YAMLFile getMessages() {
        return messages;
    }

    public static YAMLFile getSettings() {
        return settings;
    }

    public static YAMLFile getDataFile() {
        return data;
    }
}
