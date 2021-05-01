package me.danieljunek17.racingcommission.events;

import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.legofreak107.vehiclesplus.vehicles.api.events.VehicleSpawnedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherEvent;

public class WeatherListener implements Listener {

    @EventHandler
    public void weatherChangeEvent(WeatherChangeEvent event) {
        if(!event.toWeatherState()) {
            for(Team team : Team.Manager.teamdata) {
                for(VehicleData vehicleData : team.getVehicleDataList()) {
                    vehicleData.getStorageVehicle().getVehicleStats().setSpeed(vehicleData.getCachespeed() + vehicleData.getFuelboost() + vehicleData.getBatteryboost() + vehicleData.getWheelboost());
                }
            }
        }
    }
}
