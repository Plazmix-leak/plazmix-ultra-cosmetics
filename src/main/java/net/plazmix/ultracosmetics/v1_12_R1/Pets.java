package net.plazmix.ultracosmetics.v1_12_R1;

import net.plazmix.ultracosmetics.cosmetics.elements.pets.Pet;
import net.plazmix.ultracosmetics.v1_12_R1.pets.PetPumpling;
import net.plazmix.ultracosmetics.version.IPets;

/**
 * @author RadBuilder
 */
public class Pets implements IPets {
    @Override
    public Class<? extends Pet> getPumplingClass() {
        return PetPumpling.class;
    }
}
