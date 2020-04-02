package land.face.waypointer.data;

public class Waypoint {

  private String id;
  private String name;
  private BasicLocation location;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BasicLocation getLocation() {
    return location;
  }

  public void setLocation(BasicLocation location) {
    this.location = location;
  }

}
