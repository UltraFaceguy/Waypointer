package land.face.waypointer.listeners;

import land.face.waypointer.WaypointerPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(final PlayerQuitEvent event) {
    WaypointerPlugin.getInstance().getWaypointManager().removeWaypoint(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerKick(final PlayerKickEvent event) {
    WaypointerPlugin.getInstance().getWaypointManager().removeWaypoint(event.getPlayer());
  }
}
