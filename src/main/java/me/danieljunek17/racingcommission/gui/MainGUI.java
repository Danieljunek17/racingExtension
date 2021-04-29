package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainGUI {

    public static void MainMenu(Player player, Team team) {
        String[] guiSetup = {
                "    I    ",
                "  xxxxx  ",
                "         ",
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cRace Computer Manager"), guiSetup);

        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));

        GuiElementGroup group = new GuiElementGroup('x');
        int i = 1;
        for (VehicleData vehicleData : team.getVehicleDataList()) {
            ItemStack vehicleItem = Utils.createItem(Material.BOOK, 1, true, 0, vehicleData.getStorageVehicle().getName());
            group.addElement((new StaticGuiElement('e', vehicleItem, click -> {
                vehicleData.getSelectorMenu().show(player);
                gui.close();
                return true;
            })));
            i++;
        }
        gui.addElement(group);
        gui.show(player);
    }

}
