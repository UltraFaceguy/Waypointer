package land.face.waypointer.listeners;

import land.face.waypointer.util.MoveUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerMove(PlayerMoveEvent event) {
    if (event.getFrom().getX() != event.getTo().getX()
        || event.getFrom().getY() != event.getTo().getY()
        || event.getFrom().getZ() != event.getTo().getZ()) {
      MoveUtil.setLastMoved(event.getPlayer());
      MoveUtil.setVelocity(event.getPlayer());
    }
  }
}
