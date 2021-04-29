package me.danieljunek17.racingcommission.commands;

import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.commands.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

public class Rain extends Commands {
    public Rain() {
        super("regen", "laat het regenen en verander daarbij de speed van alle vehicles", "regen (speed)", "racemenu.regen");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = ((Player) sender).getPlayer();
        String regen = args[0];
        if(regen.isEmpty() || !regen.contains("\\d+")) {
            player.sendMessage(Messages.WRONGRAINUSAGE.getMessage());
        }
        player.getWorld().setStorm(true);
        for(Team team : Team.Manager.teamdata) {
            for(VehicleData vehicleData : team.getVehicleDataList()) {
                vehicleData.setCachespeed(Integer.parseInt(regen));
                vehicleData.getStorageVehicle().getVehicleStats().setSpeed(vehicleData.getCachespeed() + vehicleData.getFuelboost() + vehicleData.getBatteryboost() + vehicleData.getWheelboost());
            }
        }
        player.sendMessage(Messages.RAINSUCCES.getMessage());

        return false;
    }
}
