package land.face.waypointer.listeners;

import land.face.waypointer.WaypointerPlugin;
import land.face.waypointer.data.Waypoint;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LeaveListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(final PlayerQuitEvent event) {
    WaypointerPlugin.getInstance().getWaypointManager().removeWaypoint(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerKick(final PlayerKickEvent event) {
    WaypointerPlugin.getInstance().getWaypointManager().removeWaypoint(event.getPlayer());
  }

  @EventHandler
  public void onPlayerTp(final PlayerTeleportEvent event) {
    Waypoint waypoint = WaypointerPlugin.getInstance()
        .getWaypointManager().getWaypoint(event.getPlayer());
    if (waypoint == null) {
      return;
    }
    if (event.getTo().getWorld() != event.getFrom().getWorld()) {
      WaypointerPlugin.getInstance().getWaypointManager()
          .removeWaypoint(event.getPlayer());
      return;
    }
    if (event.getTo().distanceSquared(event.getFrom()) > 4096) {
      WaypointerPlugin.getInstance().getWaypointManager()
          .removeWaypoint(event.getPlayer());
      WaypointerPlugin.getInstance().getWaypointManager()
          .setWaypoint(event.getPlayer(), waypoint.getId());
    }
  }
}
