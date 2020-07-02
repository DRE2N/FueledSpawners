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

import com.griefcraft.lwc.LWC;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Daniel Saukel
 */
public class SpawnerListener implements Listener {

    private FueledSpawners plugin;

    private Map<Player, Spawner> openedInvs = new HashMap<>();

    public SpawnerListener(FueledSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SPAWNER && event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasLore()) {
            String name = ChatColor.stripColor(event.getItemInHand().getItemMeta().getLore().get(0));
            EntityType type = null;
            try {
                type = EntityType.valueOf(name);
            } catch (IllegalArgumentException exception) {
                return;
            }
            Food food = Food.get(type);
            if (food == null || !food.isEnabled()) {
                return;
            }
            CreatureSpawner state = ((CreatureSpawner) block.getState());
            state.setSpawnedType(type);
            state.update(true);
            new Spawner(plugin, block);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        World world = block.getWorld();
        Location location = block.getLocation();
        Spawner spawner = plugin.get(block);
        if (spawner == null) {
            return;
        }

        for (ItemStack item : spawner.getInventory().getContents()) {
            if (item != null) {
                world.dropItemNaturally(location, item);
            }
        }
        spawner.clearData();
        if (plugin.dropSpawners) {
            ItemStack item = new ItemStack(block.getType());
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Arrays.asList(ChatColor.GRAY + ((CreatureSpawner) block.getState()).getSpawnedType().name()));
            item.setItemMeta(meta);
            world.dropItemNaturally(location, item);
            event.setExpToDrop(0);
        }
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        Spawner spawner = plugin.get(event.getSpawner().getBlock());
        if (spawner == null) {
            return;
        }
        for (Material food : Food.get(spawner.getEntityType()).getFood()) {
            if (spawner.getInventory().contains(food, 1)) {
                spawner.getInventory().removeItem(new ItemStack(food, 1));
                spawner.saveStorageContents();
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (plugin.eggsDisabled && (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DISPENSE_EGG)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Spawner spawner = plugin.get(block);
        if (spawner == null) {
            return;
        }
        if (plugin.getServer().getPluginManager().getPlugin("LWC") != null) {
            if (!LWC.getInstance().canAccessProtection(player, block)) {
                event.setCancelled(true);
            }
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        player.openInventory(spawner.getInventory());
        openedInvs.put(player, spawner);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            openedInvs.remove((Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (openedInvs.containsKey(player)) {
            openedInvs.get(player).saveStorageContents();
            openedInvs.remove(player);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Food food = Food.get(event.getRightClicked().getType());
        if (food == null || !food.isEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();
        for (Material material : food.getFood()) {
            if (material == main.getType() || material == off.getType()) {
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plugin.msgBreeding)));
            }
        }
    }

}
