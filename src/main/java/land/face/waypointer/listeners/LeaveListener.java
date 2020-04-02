package land.face.waypointer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(final PlayerQuitEvent event) {
    //
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerKick(final PlayerKickEvent event) {
    //
  }
}
