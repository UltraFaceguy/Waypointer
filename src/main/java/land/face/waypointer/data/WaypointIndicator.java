package land.face.waypointer.data;


import de.oliver.fancyholograms.api.Hologram;
import java.lang.ref.WeakReference;
import org.bukkit.entity.Player;

public class WaypointIndicator {

  private WeakReference<Player> player = new WeakReference<>(null);
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

  public Player getPlayer() {
    return player.get();
  }

  public void setPlayer(Player player) {
    this.player = new WeakReference<>(player);
  }

}
