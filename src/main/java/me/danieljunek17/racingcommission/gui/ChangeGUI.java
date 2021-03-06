package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.objects.WheelsData;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.Utils;
import me.danieljunek17.racingcommission.utils.YAMLFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class ChangeGUI {

    private static YAMLFile settings = Racingcommission.getSettings();

    public static InventoryGui changeMenu(Team team, VehicleData vehicleData) {
        String[] guiSetup = {
                "         ",
                "    B    ",
                "         ",
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cPitstop Wheel Change"), guiSetup);

        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));

        gui.addElement(new DynamicGuiElement('B', (viewer) -> new StaticGuiElement('B' , vehicleData.getWheelsItem(), click -> {
            if(click.getEvent().getAction() == InventoryAction.PLACE_ALL || click.getEvent().getAction() == InventoryAction.PLACE_ONE) {
                WheelsData wheelsData = WheelsData.Manager.wheelsData.values().stream().filter(wheelsData1 -> click.getEvent().getCursor().getType() == wheelsData1.getMaterial()).findFirst().orElse(null);
                if(wheelsData != null) {
                    if(click.getEvent().getCursor().getItemMeta() == null || click.getEvent().getCursor().getItemMeta().getLore() == null) return true;
                    String lore = click.getEvent().getCursor().getItemMeta().getLore().get(0);
                    String numberedlore = lore.replaceAll("\\D+","");
                    String durabilitystring = numberedlore.substring(0, numberedlore.length() - String.valueOf(wheelsData.getMaxDurability()).length());
                    if(vehicleData.getStorageVehicle().getVehicleStats().getCurrentSpeed() != 0) {
                        click.getWhoClicked().sendMessage(Messages.DRIVINGWHENWHEELCHANGE.getMessage());
                        return true;
                    }
                    if(lore.startsWith("Durability: ") && !durabilitystring.equals("0")) {
                        vehicleData.setWheelsItem(click.getEvent().getCursor().clone());
                        vehicleData.setWheelsData(wheelsData);
                        vehicleData.setWheelboost(wheelsData.getSpeed());
                        for(Player player : Bukkit.getOnlinePlayers()){
                            if(player.hasPermission("team.admin")) {
                                player.sendMessage(Utils.color("Het team: " + team.getName() + " heeft de banden van de auto gewisselt waarin " + vehicleData.getStorageVehicle().getName() + "zit of zat"));
                            }
                        }
                        if(vehicleData.getLockedspeed() == 0) {
                            vehicleData.getStorageVehicle().getVehicleStats().setSpeed(vehicleData.getCachespeed() + vehicleData.getRegenpenalty() + wheelsData.getSpeed() + vehicleData.getBatteryboost() + vehicleData.getFuelboost());
                        }
                        return false;
                    } else {
                        return true;
                    }
                }
                return true;
            }
            if(click.getEvent().getAction() == InventoryAction.PICKUP_ALL || click.getEvent().getAction() == InventoryAction.PICKUP_HALF) {
                if(vehicleData.getStorageVehicle().getVehicleStats().getCurrentSpeed() != 0) {
                    click.getWhoClicked().sendMessage(Messages.DRIVINGWHENWHEELCHANGE.getMessage());
                    return true;
                }
                vehicleData.setWheelsItem(new ItemStack(Material.AIR));
                vehicleData.setWheelsData(null);
                if(vehicleData.getLockedspeed() == 0) {
                    vehicleData.getStorageVehicle().getVehicleStats().setSpeed(settings.getInt("settings.brokenspeed"));
                }
                return false;
            }
            return true;
        })));
        return gui;
    }

}
