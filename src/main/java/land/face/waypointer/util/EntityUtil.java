package land.face.waypointer.util;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.Hologram;
import de.oliver.fancyholograms.api.HologramData;
import org.bukkit.entity.Display.Brightness;
import java.util.List;
import java.util.UUID;
import land.face.waypointer.data.WaypointIndicator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EntityUtil {


  public static WaypointIndicator createHologram(Player player, String text) {

    HologramData data = new HologramData(UUID.randomUUID().toString());
    data.setText(List.of(text));
    data.setBackground(Hologram.TRANSPARENT);
    data.setTextHasShadow(true);
    data.setScale(1);
    data.setLocation(player.getLocation().clone());
    data.setBrightness(new Brightness(13, 13));
    Hologram holo = FancyHologramsPlugin.get().getHologramManager().create(data);
    holo.showHologram(List.of());
    holo.createHologram();
    holo.showHologram(player);
    data.setText(List.of(text));
    holo.refreshHologram(player);

    WaypointIndicator indicator = new WaypointIndicator();
    indicator.setPlayer(player);
    indicator.setMoving(true);
    indicator.setHologram(holo);

    return indicator;
  }

  public static void moveHologram(WaypointIndicator indicator, Location location) {
    indicator.getHologram().getData().setLocation(location);
    indicator.getHologram().refreshHologram(indicator.getPlayer());
  }

  public static void updateHologramName(WaypointIndicator indicator, String text) {
    indicator.getHologram().getData().setText(List.of(text));
    indicator.getHologram().updateHologram();
    indicator.getHologram().refreshHologram(indicator.getPlayer());
  }

  public static void deleteHologram(WaypointIndicator indicator) {
    Hologram hologram = indicator.getHologram();
    hologram.hideHologram(Bukkit.getOnlinePlayers());
    hologram.deleteHologram();
  }

}
