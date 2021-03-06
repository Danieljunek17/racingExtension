package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rain implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();
        if(!(player.hasPermission("racemenu.admin.regen"))) return false;
        String regen = args[0];
        if(regen.isEmpty() || !regen.contains("\\d+")) {
            player.sendMessage(Messages.WRONGRAINUSAGE.getMessage());
        }
        player.getWorld().setStorm(true);
        for(Team team : Team.Manager.teamdata) {
            for(VehicleData vehicleData : team.getVehicleDataList()) {
                vehicleData.setRegenpenalty(Integer.parseInt(regen));
                vehicleData.getStorageVehicle().getVehicleStats().setSpeed(vehicleData.getCachespeed() + vehicleData.getRegenpenalty() + vehicleData.getFuelboost() + vehicleData.getBatteryboost() + vehicleData.getWheelboost());
            }
        }
        player.sendMessage(Messages.RAINSUCCES.getMessage());

        return false;
    }
}
