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

import com.tealcube.minecraft.bukkit.facecore.plugin.FacePlugin;
import com.tealcube.minecraft.bukkit.shade.acf.PaperCommandManager;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;

import java.io.File;
import land.face.waypointer.listeners.LeaveListener;
import land.face.waypointer.managers.WaypointManager;
import land.face.waypointer.tasks.WaypointTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class WaypointerPlugin extends FacePlugin {

  private static WaypointerPlugin instance;

  private VersionedSmartYamlConfiguration configYAML;
  private WaypointManager waypointManager;

  private WaypointTask waypointTask;

  public static WaypointerPlugin getInstance() {
    return instance;
  }

  public WaypointerPlugin() {
    instance = this;
  }

  @Override
  public void enable() {
    instance = this;
    configYAML = new VersionedSmartYamlConfiguration(new File(getDataFolder(), "config.yml"),
        getResource("config.yml"),
        VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
    if (configYAML.update()) {
      getLogger().info("Updating config.yml");
    }

    waypointManager = new WaypointManager(this);

    Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);

    waypointManager.loadWaypoints();

    waypointTask = new WaypointTask();
    waypointTask.runTaskTimer(this, 1L, 1L);

    PaperCommandManager commandManager = new PaperCommandManager(this);
    commandManager.registerCommand(new Commands(this));

    commandManager.getCommandCompletions()
        .registerCompletion("waypoint-ids", c -> waypointManager.getLoadedWaypoints().keySet());
  }

  @Override
  public void disable() {
    waypointTask.cancel();
    for (Player p : Bukkit.getOnlinePlayers()) {
      waypointManager.removeWaypoint(p);
    }
    waypointManager.saveWaypoints();
    waypointManager.deleteIndicators();
    HandlerList.unregisterAll(this);
    Bukkit.getScheduler().cancelTasks(this);
  }

  public VersionedSmartYamlConfiguration getConfigYAML() {
    return configYAML;
  }

  public WaypointManager getWaypointManager() {
    return waypointManager;
  }
}
