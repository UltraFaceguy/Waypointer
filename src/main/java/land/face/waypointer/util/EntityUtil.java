package land.face.waypointer.util;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.tealcube.minecraft.bukkit.TextUtils;
import land.face.waypointer.WaypointerPlugin;
import land.face.waypointer.data.WaypointIndicator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EntityUtil {

  public static WaypointIndicator createHologram(Player player, String text) {
    Hologram hologram = HologramsAPI.createHologram(WaypointerPlugin.getInstance(), player.getEyeLocation());
    hologram.clearLines();
    hologram.appendTextLine(TextUtils.color(text));
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
    hologram.appendTextLine(TextUtils.color(text));
  }

  public static void deleteHologram(WaypointIndicator indicator) {
    Hologram hologram = indicator.getHologram();
    hologram.delete();
  }

  /*
  public static WaypointIndicator createIndicator(Player player, Location location, String name) {

    WorldServer w = ((CraftWorld) location.getWorld()).getHandle();

    EntityArmorStand stand = new EntityArmorStand(w.getMinecraftWorld(),
        location.getX(), location.getY(), location.getZ());

    stand.setNoGravity(true);
    stand.setInvisible(true);
    stand.setSmall(true);
    stand.setMarker(true);
    stand.setCustomNameVisible(false);
    stand.setCustomName(new ChatComponentText(TextUtils.color(name)));
    stand.setCustomNameVisible(true);

    PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(stand);
    PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(stand.getId(),
        stand.getDataWatcher(), true);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnPacket);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(meta);

    WaypointIndicator indicator = new WaypointIndicator();
    indicator.setStand(stand);
    indicator.setLocation(location.clone());

    return indicator;
  }

  public static void moveIndicator(Player player, WaypointIndicator indicator, Location location) {
    Vector change = location.toVector().subtract(indicator.getLocation().toVector());
    change.multiply(32 * 128);
    PacketPlayOutRelEntityMove goal = new PacketPlayOutRelEntityMove(
        indicator.getStand().getId(),
        (short) Math.floor(change.getX()),
        (short) Math.floor(change.getY()),
        (short) Math.floor(change.getZ()),
        false);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(goal);
    indicator.setLocation(location);
  }

  public static void moveIndicator2(Player player, WaypointIndicator indicator, Location location) {
    indicator.getStand().setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
    PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(indicator.getStand().getId(),
        indicator.getStand().getDataWatcher(), true);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(meta);
  }

  public static void updateIndicatorName(Player player, WaypointIndicator indicator, String name) {
    PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(indicator.getStand().getId(),
        indicator.getStand().getDataWatcher(), true);
    indicator.getStand().setCustomName(new ChatComponentText(TextUtils.color(name)));
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(meta);
  }

  public static void killIndicator(Player player, WaypointIndicator indicator) {
    PacketPlayOutEntityDestroy killStand = new PacketPlayOutEntityDestroy(
        indicator.getStand().getId());
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(killStand);
  }
  */

}
