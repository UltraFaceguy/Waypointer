package land.face.waypointer.data;

import java.util.Comparator;
import org.bukkit.Location;

public class DistanceComparator implements Comparator<Waypoint> {

  private Location loc;

  public int compare(Waypoint waypoint1, Waypoint waypoint2) {
    return Double.compare(waypoint1.getLocation().asLocation().distanceSquared(loc),
        waypoint2.getLocation().asLocation().distanceSquared(loc));
  }

  public void setLoc(Location loc) {
    this.loc = loc;
  }
}
