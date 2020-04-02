package land.face.waypointer.managers;

import com.tealcube.minecraft.bukkit.shade.google.gson.Gson;
import com.tealcube.minecraft.bukkit.shade.google.gson.JsonArray;
import com.tealcube.minecraft.bukkit.shade.google.gson.JsonElement;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import land.face.waypointer.WaypointerPlugin;
import land.face.waypointer.data.BasicLocation;
import land.face.waypointer.data.Waypoint;
import land.face.waypointer.data.WaypointIndicator;
import land.face.waypointer.util.EntityUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WaypointManager {

  private Map<Player, Waypoint> playerWaypoints = new WeakHashMap<>();
  private Map<Player, WaypointIndicator> indicators = new WeakHashMap<>();
  private Map<String, Waypoint> loadedWaypoints = new HashMap<>();
  private Map<Player, Vector> previousLocation = new WeakHashMap<>();

  private static String WAYPOINT_IND = ChatColor.AQUA + "✖";
  private static String WAYPOINT_TEXT = "&b►Waypoint: {0}◄";

  private Gson gson = new Gson();

  public void createWaypoint(String id, String name, Location location) {
    name = WordUtils.capitalizeFully(name.replaceAll("-", " ").replaceAll("_", " "));
    if (loadedWaypoints.containsKey(id)) {
      Waypoint waypoint = loadedWaypoints.get(id);
      waypoint.setName(name);
      waypoint.setLocation(BasicLocation.fromLocation(location));
      return;
    }
    Waypoint waypoint = new Waypoint();
    waypoint.setId(id);
    waypoint.setName(name);
    waypoint.setLocation(BasicLocation.fromLocation(location));
    loadedWaypoints.put(id, waypoint);
  }

  public void setWaypoint(Player player, String id) {
    if (!loadedWaypoints.containsKey(id)) {
      Bukkit.getLogger().warning("Tried to set unknown waypoint " + id + " - " + player.getName());
      return;
    }
    removeWaypoint(player);

    Waypoint waypoint = loadedWaypoints.get(id);
    playerWaypoints.put(player, waypoint);

    WaypointIndicator indicator = EntityUtil.createHologram(player, WAYPOINT_IND);
    indicators.put(player, indicator);
  }

  public void tickWaypoints() {
    for (Player p : playerWaypoints.keySet()) {
      if (!p.isOnline()) {
        continue;
      }
      Waypoint waypoint = playerWaypoints.get(p);
      if (!p.getWorld().getName().equals(waypoint.getLocation().getWorld())) {
        continue;
      }
      WaypointIndicator indicator = indicators.get(p);
      if (indicator == null) {
        continue;
      }
      Vector offset = waypoint.getLocation().asVector().subtract(p.getEyeLocation().toVector());
      if (offset.length() < 5) {
        Bukkit.getScheduler()
            .runTaskLater(WaypointerPlugin.getInstance(), () -> removeWaypoint(p), 1L);
      } else if (offset.length() < 15) {
        EntityUtil.moveHologram(indicator, waypoint.getLocation().asLocation());
        if (indicator.isMoving()) {
          EntityUtil.updateHologramName(indicator, WAYPOINT_TEXT.replace("{0}", waypoint.getName()));
          indicator.setMoving(false);
        }
      } else {
        offset.normalize().multiply(4);
        Location newLoc = p.getEyeLocation().clone().add(offset);
        newLoc.add(p.getVelocity());
        EntityUtil.moveHologram(indicator, newLoc);
        if (!indicator.isMoving()) {
          EntityUtil.updateHologramName(indicator, WAYPOINT_IND);
          indicator.setMoving(true);
        }
      }
    }
  }

  public void removeWaypoint(Player player) {
    playerWaypoints.remove(player);
    if (indicators.containsKey(player)) {
      EntityUtil.deleteHologram(indicators.get(player));
      indicators.remove(player);
    }
  }

  public void saveWaypoints() {
    try (FileWriter writer = new FileWriter(
        WaypointerPlugin.getInstance().getDataFolder() + "/waypoints.json")) {
      gson.toJson(loadedWaypoints.values(), writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadWaypoints() {
    try (FileReader reader = new FileReader(
        WaypointerPlugin.getInstance().getDataFolder() + "/waypoints.json")) {
      JsonArray array = gson.fromJson(reader, JsonArray.class);
      for (JsonElement e : array) {
        Waypoint waypoint = gson.fromJson(e, Waypoint.class);
        loadedWaypoints.put(waypoint.getId(), waypoint);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
