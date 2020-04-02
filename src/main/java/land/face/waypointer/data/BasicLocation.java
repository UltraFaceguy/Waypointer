package land.face.waypointer.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class BasicLocation {

  private String world;
  private double x;
  private double y;
  private double z;

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public Location asLocation() {
    return new Location(Bukkit.getWorld(world), x, y, z);
  }

  public Vector asVector() {
    return new Vector(x, y, z);
  }

  public static BasicLocation fromLocation(Location location) {
    BasicLocation basicLocation = new BasicLocation();
    basicLocation.setWorld(location.getWorld().getName());
    basicLocation.setX(location.getX());
    basicLocation.setY(location.getY());
    basicLocation.setZ(location.getZ());
    return basicLocation;
  }
}
