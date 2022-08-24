package net.plazmix.ultracosmetics.cosmetics.elements.pets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;

/**
 * Represents an instance of a kitten pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetKitty extends Pet {

    public PetKitty(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, PetType.getByName("kitty"), ItemFactory.create(UCMaterial.TROPICAL_FISH, UltraCosmeticsData.get().getItemNoPickupString()));
        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
            if (getOwner() != null && getEntity() != null) {
                Ageable cat = (Ageable) getEntity();
                cat.setBaby();
                // cat.setCatType(Cat.Type.RED_CAT); TODO nms?
            }
        }, 30);
    }
}
