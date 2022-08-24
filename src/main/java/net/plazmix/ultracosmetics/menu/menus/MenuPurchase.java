package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.menu.ClickRunnable;
import net.plazmix.ultracosmetics.menu.Menu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.PurchaseData;
import net.plazmix.ultracosmetics.util.UCMaterial;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sacha on 04/04/2017.
 */
public class MenuPurchase extends Menu {

    private String name;
    private PurchaseData purchaseData;

    public MenuPurchase(UltraCosmetics ultraCosmetics, String name, PurchaseData purchaseData) {
        super(ultraCosmetics);
        this.name = name;
        this.purchaseData = purchaseData;
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer player) {
        // Showcase Item
        putItem(inventory, 13, purchaseData.getShowcaseItem(), data -> {
        });

        // Purchase Item
        ItemStack purchaseItem = ItemFactory.create(UCMaterial.EMERALD_BLOCK, MessageManager.getMessage("Purchase"));
        ClickRunnable purchaseClickRunnable = data -> {
            PlazmixUser plazmixUser = PlazmixUser.of(player.getBukkitPlayer());
            plazmixUser.setCoins(plazmixUser.getCoins() - purchaseData.getPrice());

            purchaseData.getOnPurchase().run();
            player.getBukkitPlayer().closeInventory();
        };
        for (int i = 27; i < 30; i++) {
            for (int j = i; j <= i + 18; j += 9) {
                putItem(inventory, j, purchaseItem, purchaseClickRunnable);
            }
        }

        // Cancel Item
        ItemStack cancelItem = ItemFactory.create(UCMaterial.REDSTONE_BLOCK, MessageManager.getMessage("Cancel"));
        ClickRunnable cancelClickRunnable = data -> player.getBukkitPlayer().closeInventory();
        for (int i = 33; i < 36; i++) {
            for (int j = i; j <= i + 18; j += 9) {
                putItem(inventory, j, cancelItem, cancelClickRunnable);
            }
        }
    }

    @Override
    protected int getSize() {
        return 54;
    }

    @Override
    protected String getName() {
        return name == null ? "Purchase" : name;
    }
}
