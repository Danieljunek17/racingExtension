package me.danieljunek17.racingcommission.gui;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.danieljunek17.racingcommission.Racingcommission;
import me.danieljunek17.racingcommission.objects.Team;
import me.danieljunek17.racingcommission.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminGUI {

    public static void AdminMenu(Player player) {
        String[] guiSetup = {
                "xxxxxxxxx",
                "xxxxxxxxx",
                "xxxxxxxxx",
        };

        InventoryGui gui = new InventoryGui(Racingcommission.getInstance(), Utils.color("&cRace Computer Manager"), guiSetup);

        gui.setFiller(Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, 1, true, 0, ""));

        GuiElementGroup group = new GuiElementGroup('x');
        int i = 1;
        for (Team team : Team.Manager.teamdata) {
            ItemStack vehicleItem = Utils.createItem(Material.BOOK, 1, true, 0, team.getName());
            group.addElement((new StaticGuiElement('x', vehicleItem, click -> {
                MainGUI.MainMenu(player, team);
                gui.close();
                return true;
            })));
            i++;
        }
        gui.addElement(group);
        gui.show(player);
    }

}
