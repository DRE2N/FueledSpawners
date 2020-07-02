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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Saukel
 */
public class Spawner implements InventoryHolder {

    private FueledSpawners plugin;

    private static final NamespacedKey KEY = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner");
    private static final NamespacedKey SLOT_0_AMOUNT = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_0_amount");
    private static final NamespacedKey SLOT_0_TYPE = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_0_type");
    private static final NamespacedKey SLOT_1_AMOUNT = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_1_amount");
    private static final NamespacedKey SLOT_1_TYPE = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_1_type");
    private static final NamespacedKey SLOT_2_AMOUNT = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_2_amount");
    private static final NamespacedKey SLOT_2_TYPE = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_2_type");
    private static final NamespacedKey SLOT_3_AMOUNT = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_3_amount");
    private static final NamespacedKey SLOT_3_TYPE = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_3_type");
    private static final NamespacedKey SLOT_4_AMOUNT = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_4_amount");
    private static final NamespacedKey SLOT_4_TYPE = new NamespacedKey(JavaPlugin.getPlugin(FueledSpawners.class), "spawner_slot_4_type");

    private Block block;
    private EntityType entityType;
    private Inventory storage;

    public Spawner(FueledSpawners plugin, Block block) {
        this.plugin = plugin;
        this.block = block;
        CreatureSpawner state = ((CreatureSpawner) block.getState());
        PersistentDataContainer data = state.getPersistentDataContainer();
        entityType = state.getSpawnedType();
        storage = Bukkit.createInventory(this, InventoryType.HOPPER, plugin.msgInventoryTitle);
        if (data.get(KEY, PersistentDataType.BYTE) != null) {
            fetchStorageContents(data);
        } else {
            saveStorageContents(data);
        }
        plugin.add(this);
    }

    public Block getBlock() {
        return block;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public Inventory getInventory() {
        return storage;
    }

    public void fetchStorageContents() {
        fetchStorageContents(((TileState) block.getState()).getPersistentDataContainer());
    }

    public void fetchStorageContents(PersistentDataContainer data) {
        int a0 = data.get(SLOT_0_AMOUNT, PersistentDataType.INTEGER);
        Material m0 = Material.getMaterial(data.get(SLOT_0_TYPE, PersistentDataType.STRING));
        int a1 = data.get(SLOT_1_AMOUNT, PersistentDataType.INTEGER);
        Material m1 = Material.getMaterial(data.get(SLOT_1_TYPE, PersistentDataType.STRING));
        int a2 = data.get(SLOT_2_AMOUNT, PersistentDataType.INTEGER);
        Material m2 = Material.getMaterial(data.get(SLOT_2_TYPE, PersistentDataType.STRING));
        int a3 = data.get(SLOT_3_AMOUNT, PersistentDataType.INTEGER);
        Material m3 = Material.getMaterial(data.get(SLOT_3_TYPE, PersistentDataType.STRING));
        int a4 = data.get(SLOT_4_AMOUNT, PersistentDataType.INTEGER);
        Material m4 = Material.getMaterial(data.get(SLOT_4_TYPE, PersistentDataType.STRING));
        storage.setContents(new ItemStack[]{new ItemStack(m0, a0), new ItemStack(m1, a1), new ItemStack(m2, a2), new ItemStack(m3, a3), new ItemStack(m4, a4)});
    }

    public void saveStorageContents() {
        saveStorageContents(((TileState) block.getState()).getPersistentDataContainer());
    }

    public void saveStorageContents(PersistentDataContainer data) {
        data.set(KEY, PersistentDataType.BYTE, (byte) 1);
        data.set(SLOT_0_AMOUNT, PersistentDataType.INTEGER, storage.getItem(0) != null ? storage.getItem(0).getAmount() : 0);
        data.set(SLOT_0_TYPE, PersistentDataType.STRING, storage.getItem(0) != null ? storage.getItem(0).getType().name() : Material.AIR.name());
        data.set(SLOT_1_AMOUNT, PersistentDataType.INTEGER, storage.getItem(1) != null ? storage.getItem(1).getAmount() : 0);
        data.set(SLOT_1_TYPE, PersistentDataType.STRING, storage.getItem(1) != null ? storage.getItem(1).getType().name() : Material.AIR.name());
        data.set(SLOT_2_AMOUNT, PersistentDataType.INTEGER, storage.getItem(2) != null ? storage.getItem(2).getAmount() : 0);
        data.set(SLOT_2_TYPE, PersistentDataType.STRING, storage.getItem(2) != null ? storage.getItem(2).getType().name() : Material.AIR.name());
        data.set(SLOT_3_AMOUNT, PersistentDataType.INTEGER, storage.getItem(3) != null ? storage.getItem(3).getAmount() : 0);
        data.set(SLOT_3_TYPE, PersistentDataType.STRING, storage.getItem(3) != null ? storage.getItem(3).getType().name() : Material.AIR.name());
        data.set(SLOT_4_AMOUNT, PersistentDataType.INTEGER, storage.getItem(4) != null ? storage.getItem(4).getAmount() : 0);
        data.set(SLOT_4_TYPE, PersistentDataType.STRING, storage.getItem(4) != null ? storage.getItem(4).getType().name() : Material.AIR.name());
    }

    public void clearData() {
        clearData(((TileState) block.getState()).getPersistentDataContainer());
    }

    public void clearData(PersistentDataContainer data) {
        data.remove(KEY);
        data.remove(SLOT_0_AMOUNT);
        data.remove(SLOT_0_TYPE);
        data.remove(SLOT_1_AMOUNT);
        data.remove(SLOT_1_TYPE);
        data.remove(SLOT_2_AMOUNT);
        data.remove(SLOT_2_TYPE);
        data.remove(SLOT_3_AMOUNT);
        data.remove(SLOT_3_TYPE);
        data.remove(SLOT_4_AMOUNT);
        data.remove(SLOT_4_TYPE);
        plugin.remove(this);
    }

    public static boolean isSpawner(Block block) {
        if (block == null) {
            return false;
        }
        return ((TileState) block.getState()).getPersistentDataContainer().get(KEY, PersistentDataType.BYTE) != null;
    }

}
