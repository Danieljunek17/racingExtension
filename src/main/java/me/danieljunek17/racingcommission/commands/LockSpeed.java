package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.objects.BatteryState;
import me.danieljunek17.racingcommission.objects.FuelState;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.Utils;
import org.bukkit.entity.Player;

public class LockSpeed {

    public static boolean lockSpeed(Player player, String lockedspeed) {
        if(!(player.hasPermission("racemenu.admin.lockspeed"))) return false;
        if(lockedspeed.isEmpty() || !lockedspeed.contains("\\d+")) {
            player.sendMessage(Utils.color("&cje moet een getal opgeven om de speed op te limiteren"));
            return false;
        } else if(lockedspeed.equalsIgnoreCase("off")) {
            lockedspeed = "0";
        }
        player.getWorld().setStorm(true);
        for(Team team : Team.Manager.teamdata) {
            for(VehicleData vehicleData : team.getVehicleDataList()) {
                vehicleData.setLockedspeed(Integer.parseInt(lockedspeed));
                if(Integer.parseInt(lockedspeed) == 0) {
                    vehicleData.getStorageVehicle().getVehicleStats().setSpeed(vehicleData.getCachespeed() + vehicleData.getRegenpenalty() + vehicleData.getFuelboost() + vehicleData.getBatteryboost() + vehicleData.getWheelboost());
                    return false;
                }
                vehicleData.setBatteryState(BatteryState.Manager.getBatteryState("standard").join());
                vehicleData.setFuelState(FuelState.Manager.getFuelState("balance").join());
                vehicleData.getStorageVehicle().getVehicleStats().setSpeed(Integer.parseInt(lockedspeed));
            }
        }
        player.sendMessage(Messages.RAINSUCCES.getMessage());

        return false;
    }
}
