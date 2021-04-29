package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.objects.VehicleData;
import me.danieljunek17.racingcommission.objects.WheelsData;
import me.danieljunek17.racingcommission.utils.Utils;
import me.legofreak107.vehiclesplus.vehicles.vehicles.objects.addons.Wheel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WheelsGUI {

    public static void WheelsMenu(Player player) {
        String[] guiSetup = {
                "xxxxxxxxx",
                "xxxxxxxxx",
                "xxxxxxxxx",
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cGet Wheels"), guiSetup);

        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));

        GuiElementGroup group = new GuiElementGroup('x');
        for (WheelsData wheelsData : WheelsData.Manager.wheelsData.values()) {
            ItemStack wheel = Utils.createItem(wheelsData.getMaterial(), 1, true, 0, wheelsData.getName(), "Durability: " + wheelsData.getMaxDurability() + "/" +  wheelsData.getMaxDurability());
            group.addElement((new StaticGuiElement('e', wheel, click -> {
                player.getInventory().addItem(wheel);
                gui.close();
                return true;
            })));
        }
        gui.addElement(group);
        gui.show(player);
    }

}
