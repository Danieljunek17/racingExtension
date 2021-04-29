package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.objects.WheelsData;
import me.danieljunek17.racingcommission.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class SelectorGUI {

    public static InventoryGui selectorMenu(VehicleData vehicleData) {
        String[] guiSetup = {
                "    I    ",
                "         ",
                "  G   R  ",
                "         ",
                "WWWWWWWWW"
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cRace Computer Manager"), guiSetup);

        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));
        gui.addElement('W', Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, 1, true, 0, ""));
        gui.addElement('I', Utils.createItem(Material.BOOK, 1, true, 0, ""));
        gui.addElement(new StaticGuiElement('G', Utils.createItem(Material.GREEN_WOOL, 1, true, 0, "Fuel"), click -> {
            vehicleData.getFuelMenu().show(click.getWhoClicked());
            gui.close();
            return true;
        }));

        gui.addElement(new StaticGuiElement('R', Utils.createItem(Material.RED_WOOL, 1, true, 0, "Battery"), click -> {
            vehicleData.getBatteryMenu().show(click.getWhoClicked());
            gui.close();
            return true;
        }));
        return gui;
    }

}
