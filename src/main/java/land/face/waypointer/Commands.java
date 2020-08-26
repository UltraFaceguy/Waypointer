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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;

public class Commands {

  private final WaypointerPlugin plugin;

  Commands(WaypointerPlugin plugin) {
    this.plugin = plugin;
  }

  @Command(identifier = "waypointer reload", permissions = "waypointer.reload")
  public void reloadCommand(Player sender) {
    plugin.disable();
    plugin.enable();
    MessageUtils.sendMessage(sender, "&aWaypointer reloaded!");
  }

  @Command(identifier = "waypointer create", permissions = "waypointer.create")
  public void openCommand(Player sender, @Arg(name = "name") String id,
      @Arg(name = "name") String name) {
    plugin.getWaypointManager().createWaypoint(id, name, sender.getLocation());
    MessageUtils.sendMessage(sender, "Created waypoint " + id);
  }

  @Command(identifier = "waypointer delete", permissions = "waypointer.delete")
  public void openCommand(Player sender, @Arg(name = "id") String id) {
    if (plugin.getWaypointManager().isWaypoint(id)) {
      plugin.getWaypointManager().deleteWaypoint(id);
      MessageUtils.sendMessage(sender, "&eWaypoint id " + id + " deleted");
    } else {
      MessageUtils.sendMessage(sender, "&eWaypoint id " + id + " does not exist");
    }
  }

  @Command(identifier = "waypointer set", permissions = "waypointer.set", onlyPlayers = false)
  public void openCommand(CommandSender sender, @Arg(name = "target") Player player,
      @Arg(name = "id") String id) {
    if (plugin.getWaypointManager().isWaypoint(id)) {
      plugin.getWaypointManager().setWaypoint(player, id);
    } else {
      MessageUtils.sendMessage(sender, "&eWaypoint id " + id + " does not exist");
    }
  }

  @Command(identifier = "waypointer list", permissions = "waypointer.list", onlyPlayers = false)
  public void openCommand(CommandSender sender) {
    StringBuilder list = new StringBuilder();
    for (String s : plugin.getWaypointManager().getLoadedWaypoints().keySet()) {
      list.append(s).append(" ");
    }
    MessageUtils.sendMessage(sender, "Waypoint list");
    MessageUtils.sendMessage(sender, list.toString());
  }

  @Command(identifier = "waypointer clear", permissions = "waypointer.clear")
  public void openCommand(Player sender) {
    plugin.getWaypointManager().removeWaypoint(sender);
    MessageUtils.sendMessage(sender, "&b&oCleared waypoint");
  }

}
