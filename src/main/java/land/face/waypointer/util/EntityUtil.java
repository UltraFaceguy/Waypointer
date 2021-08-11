package land.face.waypointer.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import land.face.waypointer.WaypointerPlugin;
import land.face.waypointer.data.WaypointIndicator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EntityUtil {

  public static WaypointIndicator createHologram(Player player, String text) {
    Hologram hologram = HologramsAPI.createHologram(WaypointerPlugin.getInstance(), player.getEyeLocation());
    hologram.clearLines();
    hologram.appendTextLine(StringExtensionsKt.chatColorize(text));
    VisibilityManager visibilityManager = hologram.getVisibilityManager();
    visibilityManager.showTo(player);
    visibilityManager.setVisibleByDefault(false);
    WaypointIndicator indicator = new WaypointIndicator();

    indicator.setMoving(true);
    indicator.setHologram(hologram);

    return indicator;
  }

  public static void moveHologram(WaypointIndicator indicator, Location location) {
    Hologram hologram = indicator.getHologram();
    hologram.teleport(location);
  }

  public static void updateHologramName(WaypointIndicator indicator, String text) {
    Hologram hologram = indicator.getHologram();
    hologram.clearLines();
    hologram.appendTextLine(StringExtensionsKt.chatColorize(text));
  }

  public static void deleteHologram(WaypointIndicator indicator) {
    Hologram hologram = indicator.getHologram();
    hologram.delete();
  }

}
