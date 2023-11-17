package land.face.waypointer.util;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.pixeloutlaw.minecraft.spigot.garbage.StringExtensionsKt;
import java.util.List;
import java.util.UUID;
import land.face.waypointer.data.WaypointIndicator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EntityUtil {

  public static WaypointIndicator createHologram(Player player, String text) {
    Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(),
        player.getEyeLocation().clone(), false, List.of(""));
    hologram.hideAll();
    hologram.addPage();
    DHAPI.setHologramLines(hologram, 1, List.of(StringExtensionsKt.chatColorize(text)));
    hologram.show(player, 1);
    WaypointIndicator indicator = new WaypointIndicator();

    indicator.setMoving(true);
    indicator.setHologram(hologram);

    return indicator;
  }

  public static void moveHologram(WaypointIndicator indicator, Location location) {
    Hologram hologram = indicator.getHologram();
    DHAPI.moveHologram(hologram, location);
  }

  public static void updateHologramName(WaypointIndicator indicator, String text) {
    Hologram hologram = indicator.getHologram();
    DHAPI.setHologramLines(hologram, 1, List.of(StringExtensionsKt.chatColorize(text)));
  }

  public static void deleteHologram(WaypointIndicator indicator) {
    Hologram hologram = indicator.getHologram();
    hologram.destroy();
  }

}
