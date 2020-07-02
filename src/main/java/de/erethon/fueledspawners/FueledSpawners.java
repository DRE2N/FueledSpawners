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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Saukel
 */
public class FueledSpawners extends JavaPlugin {

    private Set<Spawner> spawners = new HashSet<>();

    public String msgBreeding = "&4Breeding mechanics are disabled, use mob spawners instead!";
    public String msgHelp = "&eUse &4/spawner [entity type] &eto get a spawner. &4/spawner reload &ereloads the configuration.";
    public String msgInventoryTitle = "Spawner";
    public String msgReloaded = "&eSuccessfully reloaded FueledSpawners.";
    public boolean dropSpawners = true;
    public boolean eggsDisabled = true;

    @Override
    public void onEnable() {
        getCommand("fueledspawners").setExecutor(new SpawnerCommand(this));
        getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);
        load();
    }

    public void load() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            getConfig().set("message.breeding", msgBreeding);
            getConfig().set("message.help", msgHelp);
            getConfig().set("message.inventoryTitle", msgInventoryTitle);
            getConfig().set("message.reloaded", msgReloaded);
            getConfig().set("dropSpawners", dropSpawners);
            getConfig().set("eggsDisabled", eggsDisabled);
            for (Food food : Food.values()) {
                getConfig().set("enabledMobTypes." + food.name(), food.isEnabled());
            }
            try {
                getConfig().save(file);
            } catch (IOException exception) {
            }
        } else {
            msgBreeding = getConfig().getString("message.breeding", msgBreeding);
            msgHelp = getConfig().getString("message.help", msgHelp);
            msgInventoryTitle = getConfig().getString("message.inventoryTitle", msgInventoryTitle);
            msgReloaded = getConfig().getString("message.reloaded", msgReloaded);
            dropSpawners = getConfig().getBoolean("dropSpawners", dropSpawners);
            eggsDisabled = getConfig().getBoolean("eggsDisabled", eggsDisabled);
            ConfigurationSection section = getConfig().getConfigurationSection("enabledMobTypes");
            if (section != null) {
                Map<String, Object> map = section.getValues(false);
                for (Entry<String, Object> entry : map.entrySet()) {
                    try {
                        Food food = Food.valueOf(entry.getKey());
                        food.setEnabled(Boolean.parseBoolean((String) entry.getValue()));
                    } catch (Exception exception) {
                    }
                }
            }
        }
    }

    public void add(Spawner spawner) {
        spawners.add(spawner);
    }

    public void remove(Spawner spawner) {
        spawners.remove(spawner);
    }

    public Spawner get(Block block) {
        for (Spawner spawner : spawners) {
            if (spawner.getBlock().equals(block)) {
                return spawner;
            }
        }
        if (Spawner.isSpawner(block)) {
            new Spawner(this, block);
        }
        return null;
    }

}
