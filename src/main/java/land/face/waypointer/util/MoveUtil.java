package land.face.waypointer.util;

import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MoveUtil {

  private final static Map<Player, Long> LAST_MOVED = new WeakHashMap<>();
  private final static Map<Player, Vector> lastPosition = new WeakHashMap<>();
  private final static Map<Player, Vector> velocity = new WeakHashMap<>();

  public static void setLastMoved(Player player) {
    LAST_MOVED.put(player, System.currentTimeMillis());
  }

  public static boolean hasMoved(Player player) {
    return System.currentTimeMillis() - LAST_MOVED.getOrDefault(player, 0L) < 1000;
  }

  public static void setVelocity(Player player) {
    velocity.put(player, player.getLocation().toVector()
        .subtract(lastPosition.getOrDefault(player, player.getLocation().toVector())));
    lastPosition.put(player, player.getLocation().toVector());
  }

  public static Vector getVelocity(Player player) {
    return velocity.getOrDefault(player, new Vector(0, 0, 0)).clone();
  }
}
