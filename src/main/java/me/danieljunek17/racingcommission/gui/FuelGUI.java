package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.FuelState;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Messages;
import me.danieljunek17.racingcommission.utils.Utils;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.VehicleStats;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FuelGUI {

    public static InventoryGui fuelMenu(VehicleData vehicleData) {
        String[] guiSetup = {
                "    I    ",
                " S  S  S ",
                "         "
        };

        //vehicleData.getBaseVehicle().getFuelSettings().setUsage();

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cRace Computer Manager"), guiSetup);
        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));
        gui.addElement(new DynamicGuiElement('I', (viewer) -> {
            return new StaticGuiElement('I', Utils.createItem(Material.BOOK, 1, true, 0,"&4INFO" , "&cFuel: " + Racingcommission.df.format(vehicleData.getStorageVehicle().getVehicleStats().getCurrentFuel()) + "L"));
        }));

        VehicleStats vehicleStats = vehicleData.getStorageVehicle().getVehicleStats();

        GuiElementGroup group = new GuiElementGroup('S');
        for (FuelState fuelState : FuelState.Manager.fuelStates.values()) {
            ItemStack vehicleItem = Utils.createItem(fuelState.getMaterial(), 1, true, 0, fuelState.getDisplayName());
            group.addElement((new StaticGuiElement('S', vehicleItem, click -> {
                if(vehicleData.isOffgrid()) {
                    click.getWhoClicked().sendMessage(Messages.NOTONTHEROAD.getMessage());
                    return true;
                }
                if(vehicleData.getFuelState() == fuelState) {
                    click.getWhoClicked().sendMessage(Messages.SAMESTATE.getMessage());
                    return true;
                }
                vehicleData.getBaseVehicle().getFuelSettings().setUsage(vehicleData.getBaseVehicle().getFuelSettings().getUsage() + fuelState.getUsage());
                vehicleStats.setSpeed(vehicleData.getCachespeed() + fuelState.getSpeed() + vehicleData.getBatteryboost() + vehicleData.getWheelboost());
                vehicleData.setFuelboost(fuelState.getSpeed());
                vehicleData.setFuelState(fuelState);
                return true;
            })));
        }
        gui.addElement(group);
        return gui;
    }

}
