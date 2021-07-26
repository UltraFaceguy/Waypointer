package land.face.waypointer.managers;

import com.tealcube.minecraft.bukkit.TextUtils;
import com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils;
import com.tealcube.minecraft.bukkit.facecore.utilities.MoveUtil;
import com.tealcube.minecraft.bukkit.shade.google.gson.Gson;
import com.tealcube.minecraft.bukkit.shade.google.gson.JsonArray;
import com.tealcube.minecraft.bukkit.shade.google.gson.JsonElement;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import land.face.waypointer.ReachWaypointEvent;
import land.face.waypointer.WaypointerPlugin;
import land.face.waypointer.data.BasicLocation;
import land.face.waypointer.data.DistanceComparator;
import land.face.waypointer.data.Waypoint;
import land.face.waypointer.data.WaypointIndicator;
import land.face.waypointer.util.EntityUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WaypointManager {

  private final WaypointerPlugin plugin;

  private final Map<Player, Waypoint> playerWaypoints = new WeakHashMap<>();
  private final Map<Player, WaypointIndicator> indicators = new WeakHashMap<>();
  private final Map<String, Waypoint> loadedWaypoints = new HashMap<>();

  private final String WAYPOINT_IND;
  private final String WAYPOINT_TEXT;
  private final double WAYPOINT_CLEAR;
  private final double WAYPOINT_SNAP;

  private final DistanceComparator DISTANCE_COMPARATOR = new DistanceComparator();
  private final Gson gson = new Gson();

  public WaypointManager(WaypointerPlugin plugin) {
    this.plugin = plugin;
    WAYPOINT_IND = TextUtils
        .color(plugin.getConfigYAML().getString("waypoint-indicator", "&b✖"));
    WAYPOINT_TEXT = TextUtils
        .color(plugin.getConfigYAML().getString("waypoint-indicator-snapped", "&b► {0} ◄"));
    WAYPOINT_CLEAR = Math.pow(plugin.getConfigYAML().getDouble("waypoint-clear-range", 4.5), 2);
    WAYPOINT_SNAP = Math.pow(plugin.getConfigYAML().getDouble("waypoint-snap-range", 13), 2);
  }

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

  public void deleteWaypoint(String id) {
    Waypoint waypoint = loadedWaypoints.get(id);
    if (waypoint == null) {
      return;
    }
    for (Player p : playerWaypoints.keySet()) {
      if (playerWaypoints.get(p) == waypoint) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> removeWaypoint(p), 0L);
      }
    }
    loadedWaypoints.remove(id);
  }

  public boolean isWaypoint(String id) {
    Waypoint waypoint = loadedWaypoints.get(id);
    return waypoint != null;
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
    MessageUtils
        .sendMessage(player, "&l&b[Waypoint] &bSet waypoint to &f'" + waypoint.getName() + "'&b!");
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
  }

  public void setWaypoint(Player player, String customName, Location location) {

    removeWaypoint(player);

    Waypoint waypoint = new Waypoint();
    waypoint.setId("CUSTOM");
    waypoint.setLocation(BasicLocation.fromLocation(location));
    waypoint.setName(customName);

    playerWaypoints.put(player, waypoint);

    WaypointIndicator indicator = EntityUtil.createHologram(player, WAYPOINT_IND);
    indicators.put(player, indicator);
    MessageUtils
        .sendMessage(player, "&l&b[Waypoint] &bSet waypoint to &f'" + waypoint.getName() + "'&b!");
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
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
      if (offset.lengthSquared() < WAYPOINT_CLEAR) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> removeWaypoint(p), 0L);

        ReachWaypointEvent reachWaypointEvent = new ReachWaypointEvent(p, waypoint.getId());
        Bukkit.getPluginManager().callEvent(reachWaypointEvent);

        MessageUtils.sendMessage(p,
            "&l&b[Waypoint] &bYou've reached &f" + waypoint.getName() + "&b!");
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        return;
      }
      if (offset.lengthSquared() < WAYPOINT_SNAP) {
        EntityUtil.moveHologram(indicator, waypoint.getLocation().asLocation());
        if (indicator.isMoving()) {
          EntityUtil.updateHologramName(indicator,
              WAYPOINT_TEXT.replace("{0}", waypoint.getName()));
          indicator.setMoving(false);
        }
        return;
      }

      offset.normalize().multiply(3.8);
      offset.add(MoveUtil.getVelocity(p).multiply(3));

      Location finalLocation = p.getEyeLocation().clone().add(offset);

      EntityUtil.moveHologram(indicator, finalLocation);
      if (MoveUtil.hasMoved(p) && !indicator.isMoving()) {
        EntityUtil.updateHologramName(indicator, WAYPOINT_IND);
        indicator.setMoving(true);
      } else if (indicator.isMoving() && !MoveUtil.hasMoved(p)) {
        EntityUtil.updateHologramName(indicator,
            WAYPOINT_TEXT.replace("{0}", waypoint.getName()));
        indicator.setMoving(false);
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

  public Map<String, Waypoint> getLoadedWaypoints() {
    return loadedWaypoints;
  }

  public List<Waypoint> getWaypointsByDistance(Location location) {
    List<Waypoint> waypoints = new ArrayList<>();
    for (Waypoint w : loadedWaypoints.values()) {
      if (w.getLocation().getWorld().equals(location.getWorld().getName())) {
        waypoints.add(w);
      }
    }
    DISTANCE_COMPARATOR.setLoc(location);
    waypoints.sort(DISTANCE_COMPARATOR);
    return waypoints;
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
