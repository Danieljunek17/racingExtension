package me.danieljunek17.racingcommission.events;

import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.gui.MainGUI;
import me.danieljunek17.racingcommission.gui.PreChangeGUI;
import me.danieljunek17.racingcommission.gui.WheelsGUI;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {

    private YAMLFile settings = Racingcommission.getSettings();

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.HAND)) {
            Player player = event.getPlayer();
            if(event.getClickedBlock() == null) return;
            Material material = event.getClickedBlock().getType();

            try {
                settings.getString("settings.ControllerBlock").toUpperCase();
            } catch (NullPointerException exception) {
                Bukkit.getConsoleSender().sendMessage(Messages.NOCONTROLLERBLOCK.getMessage());
                return;
            }
            if(Material.valueOf(settings.getString("settings.ControllerBlock").toUpperCase()) == material) {
                for (Team team : Team.Manager.teamdata) {
                    if (player.hasPermission(team.getPermission())) {
                        if (team.getVehicleDataList().isEmpty()) {
                            player.sendMessage(Messages.NOCAR.getMessage());
                            return;
                        } else {
                            MainGUI.MainMenu(player, team);
                        }
                        break;
                    }
                }
            } else if(Material.valueOf(settings.getString("settings.PitstopWheelChangeBlock").toUpperCase()) == material) {
                for (Team team : Team.Manager.teamdata) {
                    if (player.hasPermission(team.getPermission())) {
                        if (team.getVehicleDataList().isEmpty()) {
                            player.sendMessage(Messages.NOCAR.getMessage());
                            return;
                        } else {
                            PreChangeGUI.preChangeGUI(player, team);
                        }
                        break;
                    }
                }
            }
        }
    }

}
