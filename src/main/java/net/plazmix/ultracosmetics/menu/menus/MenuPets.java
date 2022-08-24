package net.plazmix.ultracosmetics.menu.menus;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.config.MessageManager;
import net.plazmix.ultracosmetics.config.SettingsManager;
import net.plazmix.ultracosmetics.cosmetics.Category;
import net.plazmix.ultracosmetics.cosmetics.Cosmetic;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.menu.ClickRunnable;
import net.plazmix.ultracosmetics.menu.CosmeticMenu;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.PurchaseData;
import net.plazmix.ultracosmetics.util.ServerVersion;
import net.plazmix.ultracosmetics.util.UCMaterial;
import net.plazmix.ultracosmetics.version.AAnvilGUI;
import net.plazmix.ultracosmetics.version.VersionManager;
import net.plazmix.ultracosmetics.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Pet {@link Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuPets extends CosmeticMenu<PetType> {
    private UltraCosmetics ultraCosmetics;

    public MenuPets(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.PETS);
        this.ultraCosmetics = ultraCosmetics;
    }

    @Override
    protected void putItems(Inventory inventory, CosmeticPlayer cosmeticPlayer, int page) {
        addPetRenameItem(inventory, cosmeticPlayer);
    }

    private void addPetRenameItem(Inventory inventory, CosmeticPlayer player) {
        if (SettingsManager.getConfig().getBoolean("Pets-Rename.Enabled")) {
            ItemStack stack;
            int slot = inventory.getSize() - (getCategory().hasGoBackArrow() ? 5 : 6);
            ClickRunnable run;
            if (SettingsManager.getConfig().getBoolean("Pets-Rename.Permission-Required")) {
                if (player.hasPermission("ultracosmetics.pets.rename")) {
                    if (player.getCurrentPet() != null) {
                        stack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Rename-Pet-Item"), MessageManager.getMessage("Rename-Pet").replace("%petname%", player.getCurrentPet().getType().getName()));
                        run = data -> renamePet(player);
                    } else {
                        stack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Rename-Pet-Item"), MessageManager.getMessage("Active-Pet-Needed"));
                        run = data -> {
                            player.getBukkitPlayer().sendMessage(MessageManager.getMessage("Active-Pet-Needed"));
                            player.getBukkitPlayer().closeInventory();
                        };
                    }
                } else {
                    stack = new ItemStack(Material.AIR);
                    run = data -> {
                    };
                }
            } else if (player.getCurrentPet() != null) {
                stack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Rename-Pet-Item"), MessageManager.getMessage("Rename-Pet").replace("%petname%", player.getCurrentPet().getType().getName()));
                run = data -> renamePet(player);
            } else {
                stack = ItemFactory.rename(ItemFactory.getItemStackFromConfig("Categories.Rename-Pet-Item"), MessageManager.getMessage("Active-Pet-Needed"));
                run = data -> {
                    player.getBukkitPlayer().sendMessage(MessageManager.getMessage("Active-Pet-Needed"));
                    player.getBukkitPlayer().closeInventory();
                };
            }
            putItem(inventory, slot, stack, run);
        }
    }

    public void renamePet(final CosmeticPlayer cosmeticPlayer) {
        Player p = cosmeticPlayer.getBukkitPlayer();
        if (UltraCosmeticsData.get().getServerVersion().compareTo(ServerVersion.v1_14_R1) >= 0) {
            UltraCosmeticsData.get().getVersionManager().newAnvilGUI(p,
                    "",
                    (player1 -> {
                    }),
                    (player2, text) -> {
                        if (SettingsManager.getConfig().getBoolean("Pets-Rename.Requires-Money.Enabled")) {
                            buyRenamePet(cosmeticPlayer, text.replaceAll("[^A-Za-z0-9 &éèêë]", "")
                                    .replace("&", "§"));
                            return new AAnvilGUI.Response("BUY");
                        } else {
                            cosmeticPlayer.setPetName(cosmeticPlayer.getCurrentPet().getType(), text);
                            return AAnvilGUI.Response.close();
                        }
                    });
        } else {
            AAnvilGUI gui = newAnvilGUI(p, (AAnvilGUI.AnvilClickEvent event) -> {
                if (event.getSlot() == AAnvilGUI.AnvilSlot.OUTPUT) {
                    if (event.getName() == null) {
                        return;
                    }
                    if (SettingsManager.getConfig().getBoolean("Pets-Rename.Requires-Money.Enabled")) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        buyRenamePet(cosmeticPlayer, event.getName()
                                .replaceAll("[^A-Za-z0-9 &éèêë]", "")
                                .replace("&", "§"));
                    } else {
                        cosmeticPlayer.setPetName(cosmeticPlayer.getCurrentPet().getType(), event.getName());
                    }
                } else {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                }
            });
            gui.setSlot(AAnvilGUI.AnvilSlot.INPUT_LEFT, ItemFactory.create(UCMaterial.PAPER, ""));
            gui.open();
        }
    }

    private AAnvilGUI newAnvilGUI(Player player, AAnvilGUI.AnvilClickEventHandler handler) {
        VersionManager versionManager = UltraCosmeticsData.get().getVersionManager();
        return versionManager.newAnvilGUI(player, handler);
    }

    private void buyRenamePet(CosmeticPlayer cosmeticPlayer, final String name) {
        ItemStack showcaseItem = ItemFactory.create(UCMaterial.NAME_TAG, MessageManager.getMessage("Rename-Pet-Purchase")
                .replace("%price%", "" + SettingsManager.getConfig().get("Pets-Rename.Requires-Money.Price")).replace("%name%", name));

        PurchaseData purchaseData = new PurchaseData();
        purchaseData.setPrice(SettingsManager.getConfig().getInt("Pets-Rename.Requires-Money.Price"));
        purchaseData.setShowcaseItem(showcaseItem);
        purchaseData.setOnPurchase(() -> {
            cosmeticPlayer.setPetName(cosmeticPlayer.getCurrentPet().getType(), name);
        });

        MenuPurchase menu = new MenuPurchase(getUltraCosmetics(), MessageManager.getMessage("Menus.Rename-Pet"), purchaseData);
        menu.open(cosmeticPlayer);
    }

    @Override
    protected ItemStack filterItem(ItemStack itemStack, PetType cosmeticType, CosmeticPlayer player) {
        if (player.getPetName(cosmeticType) != null) {
            ItemStack item = itemStack.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemMeta.getDisplayName() + ChatColor.GRAY + " (" + player.getPetName(cosmeticType) + ChatColor.GRAY + ")");
            item.setItemMeta(itemMeta);
            return item;
        }
        return super.filterItem(itemStack, cosmeticType, player);
    }

    @Override
    public List<PetType> enabled() {
        return PetType.enabled();
    }

    @Override
    protected void toggleOn(CosmeticPlayer cosmeticPlayer, PetType petType, UltraCosmetics ultraCosmetics) {
        petType.equip(cosmeticPlayer, ultraCosmetics);
    }

    @Override
    protected void toggleOff(CosmeticPlayer cosmeticPlayer) {
        cosmeticPlayer.removePet();
    }

    @Override
    protected Cosmetic getCosmetic(CosmeticPlayer cosmeticPlayer) {
        return cosmeticPlayer.getCurrentPet();
    }
}