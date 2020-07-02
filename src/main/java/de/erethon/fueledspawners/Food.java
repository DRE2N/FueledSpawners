/*
 * Copyright (C) 2018-2020 Daniel Saukel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.fueledspawners;

import org.bukkit.Material;
import static org.bukkit.Material.*;
import org.bukkit.entity.EntityType;

/**
 * @author Daniel Saukel
 */
public enum Food {

    CHICKEN(EntityType.CHICKEN, WHEAT_SEEDS, MELON_SEEDS, PUMPKIN_SEEDS, BEETROOT_SEEDS),
    COW(EntityType.COW, WHEAT),
    DONKEY(EntityType.DONKEY, GOLDEN_CARROT, GOLDEN_APPLE),
    HORSE(EntityType.HORSE, GOLDEN_CARROT, GOLDEN_APPLE),
    LLAMA(EntityType.LLAMA, HAY_BLOCK),
    MULE(EntityType.MULE, GOLDEN_CARROT, GOLDEN_APPLE),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, WHEAT),
    OCELOT(EntityType.OCELOT, COD, COOKED_COD, SALMON, COOKED_SALMON, PUFFERFISH, TROPICAL_FISH),
    PARROT(EntityType.PARROT, WHEAT_SEEDS, MELON_SEEDS, PUMPKIN_SEEDS, BEETROOT_SEEDS),
    PIG(EntityType.PIG, CARROT, POTATO, BEETROOT),
    POLAR_BEAR(EntityType.POLAR_BEAR, COD, COOKED_COD, SALMON, COOKED_SALMON, PUFFERFISH, TROPICAL_FISH),
    RABBIT(EntityType.RABBIT, CARROT, DANDELION, GOLDEN_CARROT),
    SHEEP(EntityType.SHEEP, WHEAT),
    WOLF(EntityType.WOLF, PORKCHOP, COOKED_PORKCHOP, BEEF, COOKED_BEEF, Material.CHICKEN, COOKED_CHICKEN, MUTTON, COOKED_MUTTON, COD, COOKED_COD, ROTTEN_FLESH);

    private EntityType type;
    private Material[] food;
    private boolean enabled;

    Food(EntityType type, Material... food) {
        this.type = type;
        this.food = food;
        enabled = true;
    }

    public EntityType getEntityType() {
        return type;
    }

    public Material[] getFood() {
        return food;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static Food get(EntityType type) {
        for (Food food : values()) {
            if (food.type == type) {
                return food;
            }
        }
        return null;
    }

}
