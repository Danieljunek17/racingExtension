package me.danieljunek17.racingcommission.events;

import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.*;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import me.legofreak107.vehiclesplus.vehicles.api.events.VehicleDestroyEvent;
import me.legofreak107.vehiclesplus.vehicles.api.events.VehicleSpawnedEvent;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.SpawnedVehicle;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.addons.seats.Seat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class VehicleSpawnListener implements Listener {

    private YAMLFile data = Racingcommission.getDataFile();
    private YAMLFile settings = Racingcommission.getSettings();

    @EventHandler
    public void vehicleSpawnEvent(VehicleSpawnedEvent event) {
        SpawnedVehicle vehicle = event.getVehicle();

        Player player = Bukkit.getPlayer(UUID.fromString(vehicle.getStorageVehicle().getOwner()));

        Vector location = vehicle.getStorageVehicle().getLocation();
        Location location1 = new Location(player.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());

        boolean offgrid = SpeedLimitData.Manager.speedLimits.containsKey(location1.getBlock().getType());

        for(Team team: Team.Manager.teamdata) {
            if (player.hasPermission(team.getPermission())) {
                ItemStack wheelsItem = new ItemStack(Material.AIR);
                WheelsData wheelsData = null;
                if(!data.contains(vehicle.getStorageVehicle().getUuid() + ".team")) {
                    data.set(vehicle.getStorageVehicle().getUuid() + ".team", team.getName());
                }
                if(data.contains(vehicle.getStorageVehicle().getUuid() + ".wheelItem")) {
                    wheelsItem = data.getItemStack(vehicle.getStorageVehicle().getUuid() + ".wheelItem");
                }
                if(data.contains(vehicle.getStorageVehicle().getUuid() + ".wheelData")) {
                    wheelsData = data.getObject(vehicle.getStorageVehicle().getUuid() + ".wheelData", WheelsData.class);
                }
                VehicleData vehicleData = new VehicleData(vehicle.getBaseVehicle(), vehicle.getStorageVehicle(), vehicle.getStorageVehicle().getVehicleStats().getSpeed(), wheelsItem, wheelsData);
                vehicleData.setSavedspeed(vehicle.getStorageVehicle().getVehicleStats().getSpeed());
                if(!data.contains(vehicle.getStorageVehicle().getUuid() + ".wheelData")) {
                    vehicle.getStorageVehicle().getVehicleStats().setSpeed(settings.getInt("settings.brokenspeed"));
                }
                vehicleData.setOffgrid(offgrid, SpeedLimitData.Manager.speedLimits.get(location1.getBlock().getType()));
                team.getVehicleDataList().add(vehicleData);
                return;
            }
        }
        player.sendMessage(Messages.NOTEAM.getMessage());
    }

    @EventHandler
    public void vehicleDestroyEvent(VehicleDestroyEvent event) {
        SpawnedVehicle vehicle = event.getVehicle();

        Player player = Bukkit.getPlayer(UUID.fromString(vehicle.getStorageVehicle().getOwner()));
        assert player != null;

        for(Team team: Team.Manager.teamdata) {
            if(player.hasPermission(team.getPermission())) {
                VehicleData vehicleData = team.getVehicleDataList().stream().filter(vehicleData1 -> vehicleData1.getStorageVehicle() == vehicle.getStorageVehicle()).findFirst().orElse(null);
                vehicleData.setBatteryState(BatteryState.Manager.getBatteryState("standard").join());
                team.getVehicleDataList().remove(vehicleData);
                return;
            }
        }
        player.sendMessage(Messages.NOTEAM.getMessage());
    }

}
