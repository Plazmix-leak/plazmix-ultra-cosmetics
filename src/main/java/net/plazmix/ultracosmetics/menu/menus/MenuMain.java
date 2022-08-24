package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.menu.Menu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Main {@link Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuMain extends Menu {

    private int[] layout;

    public MenuMain(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics);

        switch (Category.enabledSize()) {
            case 8:
                layout = new int[]{10, 12, 14, 16, 28, 30, 32, 34};
                break;
            case 7:
                layout = new int[]{10, 13, 16, 28, 30, 32, 34};
                break;
            case 6:
                layout = new int[]{10, 13, 16, 28, 31, 34};
                break;
            case 5:
                layout = new int[]{10, 16, 22, 29, 33};
                break;
            case 4:
                layout = new int[]{19, 21, 23, 25};
                break;
            case 3:
                layout = new int[]{20, 22, 24};
                break;
            case 2:
                layout = new int[]{21, 23};
                break;
            case 1:
                layout = new int[]{22};
                break;
        }

        //if (layout != null) {
        //    for (int i = 0; i < layout.length; i++) {
        //        layout[i] += 9;
        //    }
        //}
    }

    @Override
    public void open(CosmeticPlayer player) {
        if (Category.enabledSize() == 1) {
            Category.enabled().get(0).getMenu(getUltraCosmetics()).open(player);
            return;
        }
        super.open(player);
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer player) {
        if (Category.enabledSize() > 0) {
            for (int i = 0; i < Category.enabledSize(); i++) {
                int slot = layout[i];
                Category category = Category.enabled().get(i);
                putItem(inventory, slot, category.getItemStack(), data -> {
                    category.getMenu(getUltraCosmetics()).open(player);
                });
            }
        }

        // Clear cosmetics item.
        String message = MessageManager.getMessage("Clear-Cosmetics");
        ItemStack itemStack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Clear-Cosmetic-Item"), message);
        putItem(inventory, inventory.getSize() - 5, itemStack, data -> {
            player.clear();
            open(player);
        });

    }

    @Override
    protected String getName() {
        return MessageManager.getMessage("Menus.Main-Menu");
    }

    @Override
    protected int getSize() {
        return 45;
    }
}
