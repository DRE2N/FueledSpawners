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

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Daniel Saukel
 */
public class SpawnerCommand implements CommandExecutor {

    private FueledSpawners plugin;

    public SpawnerCommand(FueledSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgHelp));
            return false;
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            plugin.onEnable();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgReloaded));
            return true;
        } else if (sender instanceof Player) {
            try {
                EntityType type = EntityType.valueOf(args[0].toUpperCase());
                ItemStack item = new ItemStack(Material.SPAWNER);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Arrays.asList(ChatColor.GRAY + type.name()));
                item.setItemMeta(meta);
                ((Player) sender).getInventory().addItem(item);
                return true;
            } catch (IllegalArgumentException exception) {
                sender.sendMessage(plugin.msgReloaded);
                return false;
            }
        } else {
            sender.sendMessage("This is not a console command.");
            return false;
        }
    }

}
