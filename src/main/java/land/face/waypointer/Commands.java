/**
 * The MIT License Copyright (c) 2015 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package land.face.waypointer;

import com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils;
import com.tealcube.minecraft.bukkit.shade.acf.BaseCommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandAlias;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandCompletion;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandPermission;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.Subcommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.Syntax;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("waypointer|wp|marker")
public class Commands extends BaseCommand {

  private final WaypointerPlugin plugin;

  Commands(WaypointerPlugin plugin) {
    this.plugin = plugin;
  }

  @Subcommand("reload")
  @CommandPermission("waypointer.reload")
  public void reloadCommand(Player sender) {
    plugin.disable();
    plugin.enable();
    MessageUtils.sendMessage(sender, "&aWaypointer reloaded!");
  }

  @Subcommand("create")
  @CommandCompletion("id name")
  @Syntax("<waypoint-id> <waypoint-display-name>")
  @CommandPermission("waypointer.create")
  public void openCommand(Player sender, String id, String name) {
    plugin.getWaypointManager().createWaypoint(id, name, sender.getLocation());
    MessageUtils.sendMessage(sender, "Created waypoint " + id);
  }

  @Subcommand("delete")
  @CommandCompletion("@waypoint-ids")
  @Syntax("<waypoint-id>")
  @CommandPermission("waypointer.delete")
  public void openCommand(Player sender, String id) {
    if (plugin.getWaypointManager().isWaypoint(id)) {
      plugin.getWaypointManager().deleteWaypoint(id);
      MessageUtils.sendMessage(sender, "&eWaypoint id " + id + " deleted");
    } else {
      MessageUtils.sendMessage(sender, "&eWaypoint id " + id + " does not exist");
    }
  }

  @Subcommand("set")
  @CommandCompletion("@players @waypoint-ids")
  @Syntax("<player> <waypoint-id>")
  @CommandPermission("waypointer.set")
  public void openCommand(CommandSender sender, Player player, String id) {
    if (plugin.getWaypointManager().isWaypoint(id)) {
      plugin.getWaypointManager().setWaypoint(player, id);
    } else {
      MessageUtils.sendMessage(sender, "&eWaypoint id " + id + " does not exist");
    }
  }

  @Subcommand("custom")
  @CommandCompletion("@players @range:1-100 @range:1-100 @range:1-100 @worlds text")
  @Syntax("<player> <x> <y> <z> <world> <name>")
  @CommandPermission("waypointer.custom")
  public void customCommand(CommandSender sender, Player player, double x, double y, double z,
      World world, String name) {
    if (world == null) {
      MessageUtils.sendMessage(sender, "&eWorld does not exist!");
      return;
    }
    plugin.getWaypointManager().setWaypoint(player, name, new Location(world, x, y, z));
  }

  @Subcommand("point")
  @CommandCompletion("@range:1-100 @range:1-100 @range:1-100 @worlds text")
  @Syntax("<x> <y> <z> <world> <name>")
  public void pointCommand(CommandSender sender, double x, double y, double z, World world,
      String name) {
    if (world == null) {
      MessageUtils.sendMessage(sender, "&eWorld does not exist!");
      return;
    }
    plugin.getWaypointManager().setWaypoint((Player) sender, name.replaceAll("_", " "), new Location(world, x, y, z));
  }

  @Subcommand("list")
  @CommandPermission("waypointer.list")
  public void openCommand(CommandSender sender) {
    StringBuilder list = new StringBuilder();
    for (String s : plugin.getWaypointManager().getLoadedWaypoints().keySet()) {
      list.append(s).append(" ");
    }
    MessageUtils.sendMessage(sender, "Waypoint list");
    MessageUtils.sendMessage(sender, list.toString());
  }

  @Subcommand("clear|delete|remove")
  public void openCommand(Player sender) {
    plugin.getWaypointManager().removeWaypoint(sender);
    MessageUtils.sendMessage(sender, "&b&oCleared waypoint");
  }

}
