package land.face.waypointer;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ReachWaypointEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Player player;
  private final String waypoint;

  public ReachWaypointEvent(Player player, String waypoint) {
    this.player = player;
    this.waypoint = waypoint;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public Player getPlayer() {
    return player;
  }

  public String getWaypoint() {
    return waypoint;
  }

}
