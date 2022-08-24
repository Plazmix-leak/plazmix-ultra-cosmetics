package net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.impl;

import net.plazmix.ultracosmetics.UltraCosmetics;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffect;
import net.plazmix.ultracosmetics.cosmetics.elements.particleeffects.ParticleEffectType;
import net.plazmix.ultracosmetics.player.CosmeticPlayer;
import net.plazmix.ultracosmetics.util.MathUtils;
import net.plazmix.ultracosmetics.util.Particles;
import net.plazmix.ultracosmetics.util.UtilParticles;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents an instance of flame fairy particles summoned by a player.
 *
 * @author iSach
 * @since 12-23-2015
 */
public class ParticleEffectFlameFairy extends ParticleEffect {

    private Vector targetDirection = new Vector(1, 0, 0);

    private Location currentLocation, targetLocation;

    public double noMoveTime = 0, movementSpeed = 0.2d;

    public ParticleEffectFlameFairy(CosmeticPlayer owner, UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, owner, ParticleEffectType.valueOf("flamefairy"));

        currentLocation = getPlayer().getLocation();
        targetLocation = generateNewTarget();
        ignoreMove = true;
    }

    @Override
    public void onUpdate() {
        if (getPlayer().getWorld() != currentLocation.getWorld()
                || getPlayer().getWorld() != targetLocation.getWorld()) {
            currentLocation = getPlayer().getLocation();
            targetLocation = generateNewTarget();
        }

        double distanceBtw = getPlayer().getEyeLocation().distance(currentLocation);
        double distTarget = currentLocation.distance(targetLocation);

        if (distTarget < 1d || distanceBtw > 3)
            targetLocation = generateNewTarget();

        distTarget = currentLocation.distance(targetLocation);

        if (MathUtils.random.nextDouble() > 0.98)
            noMoveTime = System.currentTimeMillis() + MathUtils.randomDouble(0, 2000);

        if (getPlayer().getEyeLocation().distance(currentLocation) < 3)
            movementSpeed = noMoveTime > System.currentTimeMillis() ? Math.max(0, movementSpeed - 0.0075)
                    : Math.min(0.1, movementSpeed + 0.0075);
        else {
            noMoveTime = 0;
            movementSpeed = Math.min(0.15 + distanceBtw * 0.05, movementSpeed + 0.02);
        }

        targetDirection.add(targetLocation.toVector().subtract(currentLocation.toVector()).multiply(0.2));

        if (targetDirection.length() < 1)
            movementSpeed = targetDirection.length() * movementSpeed;

        targetDirection = targetDirection.normalize();

        if (distTarget > 0.1)
            currentLocation.add(targetDirection.clone().multiply(movementSpeed));

        UtilParticles.display(Particles.LAVA, currentLocation);
        UtilParticles.display(Particles.FLAME, currentLocation);
    }

    private Location generateNewTarget() {
        return getPlayer().getEyeLocation()
                .add(Math.random() * 6 - 3,
                        Math.random() * 1.5,
                        Math.random() * 6 - 3);
    }
}
