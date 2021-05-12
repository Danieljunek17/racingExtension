package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.BatteryState;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.Utils;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.VehicleStats;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BatteryGUI {

    public static InventoryGui batteryMenu(VehicleData vehicleData) {
        String[] guiSetup = {
                "    I    ",
                " S  S  S ",
                "         "
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cRace Computer Manager"), guiSetup);
        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));
        gui.addElement(new DynamicGuiElement('I', (viewer) -> {
            return new StaticGuiElement('I', Utils.createItem(Material.BOOK, 1, true, 0,"&4INFO" , "&cBatterij: " + Racingcommission.df.format(vehicleData.getBatteryPercentage())));
        }));

        VehicleStats vehicleStats = vehicleData.getStorageVehicle().getVehicleStats();

        GuiElementGroup group = new GuiElementGroup('S');
        for (BatteryState batteryState : BatteryState.Manager.batteryStates.values()) {
            ItemStack vehicleItem = Utils.createItem(batteryState.getMaterial(), 1, true, 0, batteryState.getDisplayName());
            group.addElement((new StaticGuiElement('S', vehicleItem, click -> {
                if(vehicleData.isOffgrid()) {
                    click.getWhoClicked().sendMessage(Messages.NOTONTHEROAD.getMessage());
                    return true;
                }
                if(vehicleData.getBatteryState() == batteryState) {
                    click.getWhoClicked().sendMessage(Messages.SAMESTATE.getMessage());
                    return true;
                }
                if(vehicleData.getBatteryPercentage() == 100 && batteryState == BatteryState.BatteryStateEnum.CHARGING.getState()) {
                    click.getWhoClicked().sendMessage(Messages.BATTERYALREADYFULL.getMessage());
                    return true;
                }
                if(vehicleData.getBatteryPercentage() == 0 && batteryState == BatteryState.BatteryStateEnum.BOOST.getState()) {
                    click.getWhoClicked().sendMessage(Messages.BATTERYEMPTYNOBOOST.getMessage());
                    return true;
                }
                if(vehicleData.getBatteryPercentage() < Math.abs(BatteryState.BatteryStateEnum.BOOST.getState().getCharge()) && batteryState == BatteryState.BatteryStateEnum.BOOST.getState()) {
                    click.getWhoClicked().sendMessage(Messages.BATTERYALMOSTEMPTYNOBOOST.getMessage());
                    return true;
                }
                vehicleStats.setSpeed(vehicleData.getCachespeed() + vehicleData.getRegenpenalty() + batteryState.getSpeed() + vehicleData.getFuelboost() + vehicleData.getWheelboost());
                vehicleData.setBatteryState(batteryState);
                return true;
            })));
        }
        gui.addElement(group);
        return gui;
    }
}
