package me.danieljunek17.racingcommission.events;

import me.danieljunek17.racingcommission.utils.Utils;
import me.legofreak107.vehiclesplus.vehicles.api.events.VehicleEnterEvent;
import me.legofreak107.vehiclesplus.vehicles.api.events.VehicleLeaveEvent;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.SpawnedVehicle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VehicleEnterListener implements Listener {

    @EventHandler
    public void vehicleEnterEvent(VehicleEnterEvent event) {
        SpawnedVehicle vehicle = event.getVehicle();
        Player player = event.getDriver();
        if(event.getVehicle().getLocked()){
            return;
        }
        if(event.getSeat().getSteer()) {
            String playername = player.getName();
            vehicle.getStorageVehicle().setName(Utils.color("&9" + playername));
        }
    }

    @EventHandler
    public void vehicleLeaveEvent(VehicleLeaveEvent event) {
        SpawnedVehicle vehicle = event.getVehicle();
        Player player = event.getDriver();
        if(event.getVehicle().getLocked()){
            return;
        }
        if(event.getSeat().getSteer()) {
            String playername = player.getName();
            vehicle.getStorageVehicle().setName(Utils.color("&4- &c" + playername));
        }
    }

}
