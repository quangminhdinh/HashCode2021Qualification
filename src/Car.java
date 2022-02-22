import java.util.ArrayList;
import java.util.List;

public class Car {
  int phase = 1;
  List<String> paths = new ArrayList<>();
  List<String> paths1 = new ArrayList<>();
  List<Integer> streetDistances = new ArrayList<>();
  int time;
  int travelTime;
  boolean isStandby;

  public Car(List<Street> streets) {
    this.time = 0;
    this.travelTime = 0;
    this.isStandby = true;

    for (Street street: streets) {
      streetDistances.add(street.length);
      this.paths.add(street.id);
      this.paths1.add(street.id);
    }
  }

  public void reset() {
    time = 0;
    travelTime = 0;
    isStandby = true;
    phase = 1;
  }

  public void changePhase() {
    phase++;
    isStandby = false;
  }

  public int getTimeUntilNextPhase() {
    return streetDistances.get(phase - 1) - time;
  }

  public void run() {
    if (!this.isStandby) {
      // time = current location on a street
      if (time < streetDistances.get(phase - 1) - 1) {
        time++;
      } else {
        isStandby = true;
        time = 0;
      }
    }
    travelTime++;
  }
}

