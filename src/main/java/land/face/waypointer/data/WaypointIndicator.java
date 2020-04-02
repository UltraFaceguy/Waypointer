package land.face.waypointer.data;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class WaypointIndicator {

  private Hologram hologram;
  private boolean moving;

  public Hologram getHologram() {
    return hologram;
  }

  public void setHologram(Hologram hologram) {
    this.hologram = hologram;
  }

  public boolean isMoving() {
    return moving;
  }

  public void setMoving(boolean moving) {
    this.moving = moving;
  }

}
