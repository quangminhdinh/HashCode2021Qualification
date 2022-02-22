import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class Intersection {
  int intersectionId;

  ArrayList<Street> schedules;
  int totalCycleTime;
  int time;
  int wait = 0;

  boolean isPlaned = false;

  // start street id -> end street
  List<Street> incomingStreets = new ArrayList<>();
  HashMap<String, Street> outgoingStreets = new HashMap<>();

  public Intersection(int intersectionId) {
    this.intersectionId = intersectionId;
    this.schedules = new ArrayList<Street>();
    this.totalCycleTime = 0;
    this.time = 0;
  }

  public void addSchedule(Street street) {
    totalCycleTime += street.greenLightDuration;
    schedules.add(street);
//    System.out.println(123);
  }

  public void run() {
    if (time < totalCycleTime) {
      time++;
    }
    else {
      time = 0;
    }

    // find street based on time
    int current = 0;
    for (Street street: schedules) {
      current += street.greenLightDuration;
      if (time <= current) {
        street.activate(outgoingStreets);
        break;
      }
    }
  }
}
