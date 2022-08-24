package net.plazmix.ultracosmetics.menu;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

import static net.plazmix.ultracosmetics.util.ItemFactory.fillerItem;

/**
 * Represents a Menu. A menu can have multiple pages in case of cosmetics.
 * Each item in the menu will, when clicked by a player, execute a ClickRunnable.
 *
 * @author iSach
 * @since 07-05-2016
 */
public abstract class Menu implements Listener {

    /**
     * UltraCosmetcs Instance.
     */
    private UltraCosmetics ultraCosmetics;

    /**
     * Click Runnables maps.
     * <p>
     * Key: Item
     * Value: ClickRunnable to call when item is clicked.
     */
    private Map<Inventory, Map<ItemStack, ClickRunnable>> clickRunnableMap = new HashMap<>();

    public Menu(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;

        ultraCosmetics.getServer().getPluginManager().registerEvents(this, ultraCosmetics);
    }

    public void open(CosmeticPlayer player) {
        Inventory inventory = Bukkit.createInventory(new CosmeticsInventoryHolder(), getSize(), getName());
        putItems(inventory, player);
        ItemFactory.fillInventory(inventory);
        player.getBukkitPlayer().openInventory(inventory);
    }

    protected void putItem(Inventory inventory, int slot, ItemStack itemStack, ClickRunnable clickRunnable) {
        Validate.notNull(itemStack);
        Validate.notNull(clickRunnable);

        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.values());
            itemStack.setItemMeta(itemMeta);
        }

        inventory.setItem(slot, itemStack);
        if (clickRunnableMap.containsKey(inventory)) {
            Map<ItemStack, ClickRunnable> map = clickRunnableMap.get(inventory);
            map.put(itemStack, clickRunnable);
        } else {
            Map<ItemStack, ClickRunnable> map = new HashMap<>();
            map.put(itemStack, clickRunnable);
            clickRunnableMap.put(inventory, map);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        // Check Inventory isn't null
        if (event.getInventory() == null) {
            return;
        }

        // Check Item clicked isn't null
        if (event.getCurrentItem() == null) {
            return;
        }

        // Check clicker is player
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        // Check Inventory is the good one
        if (!event.getView().getTitle().contains(getName())) {
            return;
        }

        // Check that the filler item isn't being clicked
        if (fillerItem != null && event.getCurrentItem().equals(fillerItem)) {
            event.setCancelled(true);
            return;
        }
        // Check that Inventory is valid.
        if (!clickRunnableMap.containsKey(event.getInventory())) {
            return;
        }

        boolean correctItem = false;

        ClickRunnable clickRunnable = null;
        for (ItemStack itemStack : clickRunnableMap.get(event.getInventory()).keySet()) {
            if (ItemFactory.haveSameName(itemStack, event.getCurrentItem())) {
                correctItem = true;
                clickRunnable = clickRunnableMap.get(event.getInventory()).get(itemStack);
            }
        }
        if (!correctItem) {
            return;
        }

        event.setCancelled(true);

        // Check clickrunnable isn't null.
        if (clickRunnable == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        CosmeticPlayer cosmeticPlayer = ultraCosmetics.getPlayerManager().getUltraPlayer(player);
        clickRunnable.run(new ClickData(event.getInventory(), cosmeticPlayer, event.getAction(), event.getCurrentItem(), event.getSlot()));
        ((Player) event.getWhoClicked()).updateInventory();
    }

    public UltraCosmetics getUltraCosmetics() {
        return ultraCosmetics;
    }

    protected abstract void putItems(Inventory inventory, CosmeticPlayer player);

    protected abstract int getSize();

    protected abstract String getName();
}
