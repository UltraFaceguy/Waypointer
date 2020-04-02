package land.face.waypointer.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import land.face.waypointer.util.MoveUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

  private Map<UUID, Boolean> groundedLastTick = new HashMap<>();

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerMoveHorizontally(PlayerMoveEvent event) {
    if (event.getFrom().getX() != event.getTo().getX()
        || event.getFrom().getZ() != event.getTo().getZ()) {
      MoveUtil.setLastMoved(event.getPlayer());
    }
  }
}
