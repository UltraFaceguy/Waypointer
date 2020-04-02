package land.face.waypointer.util;

import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;

public class MoveUtil {

  private final static Map<Player, Long> LAST_MOVED = new WeakHashMap<>();

  public static void setLastMoved(Player player) {
    LAST_MOVED.put(player, System.currentTimeMillis());
  }

  public static boolean hasMoved(Player player) {
    return System.currentTimeMillis() - LAST_MOVED.getOrDefault(player, 0L) < 1000;
  }
}
