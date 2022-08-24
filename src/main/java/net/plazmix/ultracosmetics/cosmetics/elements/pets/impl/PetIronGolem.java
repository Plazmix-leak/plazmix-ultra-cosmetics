package net.plazmix.ultracosmetics.cosmetics.elements.pets.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.UltraCosmeticsData;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.cosmetics.elements.pets.PetType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.ItemFactory;
import net.plazmix.ultracosmetics.util.UCMaterial;

/**
 * Represents an instance of an iron golem pet summoned by a player.
 *
 * @author RadBuilder
 * @since 07-02-2017
 */
public class PetIronGolem extends Pet {
    public PetIronGolem(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(owner, ultraCosmetics, PetType.getByName("irongolem"), ItemFactory.create(UCMaterial.RED_DYE, UltraCosmeticsData.get().getItemNoPickupString()));

    }
}
