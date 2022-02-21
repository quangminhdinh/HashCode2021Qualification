import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 3
 * 1
 * 2
 * rue-d-athenes 2
 * rue-d-amsterdam 1
 * 0
 * 1
 * rue-de-londres 2
 * 2
 * 1
 * rue-de-moscou 1
 */

public class Processor {
  Street[] streets;
  Intersection[] intersections;
  Car[] cars;
  HashMap<String, Street> streetmap;

  int D;
  int F;
  int score;

  public static Processor processor = null;


  public Processor(String filepath) {
    try {
      parseInput(filepath);
    } catch (FileNotFoundException e) {
      System.out.println("File not found!");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static Processor getInstance() {
    if (processor == null) {
      processor = new Processor("f.txt");
    }
    return processor;
  }

  public static void main(String[] args) {
    Processor optimizer = Processor.getInstance();
    optimizer.optimizeSleepAssign();
    System.out.println(optimizer.score);
    optimizer.reset();
    optimizer.run();
    System.out.println(optimizer.score);
  }

  public void optimizeTest() {
    streets[0].greenLightDuration = 2;
    intersections[0].addSchedule(streets[0]);
    streets[1].greenLightDuration = 1;
    intersections[1].addSchedule(streets[1]);
    streets[2].greenLightDuration = 2;
    intersections[1].addSchedule(streets[2]);
    streets[4].greenLightDuration = 1;
    intersections[2].addSchedule(streets[4]);
//    for (Street street : streets) {
//      System.out.println(street.id);
//
//      if (street.id == "rue-de-moscou") {
//        street.greenLightDuration = 1;
//        intersections[2].addSchedule(street);
//      }
//    }
//    System.out.println(intersections[1].schedules);
    for (int i = 1; i <= D; i++) {
      for (Street street : streets) street.run();
      for (Intersection intersection : intersections) intersection.run();
      for (Street street : streets) street.updateStandby();
    }

  }

  public void optimizeSleepAssign() {
    HashMap<String, Integer> durations = new HashMap<>();
    for (Car car : cars) {
      for (String path : car.paths) {
        if (durations.get(path) == null) durations.put(path, 1);
        else durations.put(path, durations.get(path) + 1);
      }
    }
    for (Street street : streets) {
      if (durations.get(street.id) != null) street.greenLightDuration = durations.get(street.id);
    }

//    int wait = 0;

    for (int i = 1; i <= D; i++) {
      for (Street street : streets) street.run();
      for (Intersection intersection : intersections) {
        if (intersection.time == intersection.totalCycleTime && !intersection.isPlaned) {
          Street maxStreet = intersection.incomingStreets.get(0);
          for (Street street : intersection.incomingStreets) {
            if (street.getPotentialCars() > maxStreet.getPotentialCars()) maxStreet = street;
          }
          if (intersection.schedules.contains(maxStreet) || maxStreet.getPotentialCars() == 0) {
            intersection.isPlaned = true;
          } else {
            intersection.addSchedule(maxStreet);
//            wait = maxStreet.greenLightDuration;
          }

        }
        intersection.run();
      }
      for (Street street : streets) street.updateStandby();
//      if (wait > 0) wait--;
    }
  }

  public void optimizeGreedy() {
    for (int i = 1; i <= D; i++) {
      for (Street street : streets) street.run();
      for (Intersection intersection : intersections) {
        if (intersection.time == intersection.totalCycleTime && !intersection.isPlaned) {
          Street maxStreet = intersection.incomingStreets.get(0);
          for (Street street : intersection.incomingStreets) {
            if (street.getPotentialCars() > maxStreet.getPotentialCars()) maxStreet = street;
          }
          if (intersection.schedules.contains(maxStreet) || maxStreet.getPotentialCars() == 0) {
            intersection.isPlaned = true;
          } else {
            intersection.addSchedule(maxStreet);
          }
          intersection.run();
        }
      }
      for (Street street : streets) street.updateStandby();
    }
  }

  public void reset() {
    score = 0;
    for (Car car: cars) car.reset();
    for (Intersection intersection: intersections) intersection.time = 0;
    for (Street street: streets) {
      street.carsOnStandby = street.carsOnStandby1;
      street.carsOnStreet.clear();
    }
  }

  public void run() {
    for (int i = 1; i <= D; i++) {
      for (Street street : streets) street.run();
      for (Intersection intersection : intersections) intersection.run();
      for (Street street : streets) street.updateStandby();
    }
  }

  public void updateScore(int T) {
    score += (F + D - T);
    System.out.println(123);
  }

  public void parseInput(String filepath) throws IOException {
    String local = "input/";
    File file = new File(local + filepath);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String str;
    str = br.readLine();
    String[] splitted = str.split("\\s+");

    D = Integer.parseInt(splitted[0]);
    int I = Integer.parseInt(splitted[1]);
    int S = Integer.parseInt(splitted[2]);
    int V = Integer.parseInt(splitted[3]);
    F = Integer.parseInt(splitted[4]);

    this.intersections = new Intersection[I];
    cars = new Car[V];
    streetmap = new HashMap<>();
    streets = new Street[S];

    for (int i = 0; i < I; i++)
      intersections[i] = new Intersection(i);

    for (int i = 0; i < S; i++) {
      splitted = br.readLine().split("\\s+");
      int B = Integer.parseInt(splitted[0]);
      int E = Integer.parseInt(splitted[1]);
      String name = splitted[2];
      int L = Integer.parseInt(splitted[3]);
      Street street = new Street(name, B, E, L);
      intersections[B].outgoingStreets.put(name, street);
      intersections[E].incomingStreets.add(street);
      streetmap.put(name, street);
      streets[i] = street;
    }

    for (int i = 0; i < V; i++) {
      splitted = br.readLine().split("\\s+");
      int P = Integer.parseInt(splitted[0]);
      ArrayList<Street> vstreets = new ArrayList<Street>();
      for (int j = 0; j < P; j++) {
        vstreets.add(streetmap.get(splitted[j+1]));
      }
      cars[i] = new Car(vstreets);
      vstreets.get(0).carsOnStandby.add(cars[i]);
      vstreets.get(0).carsOnStandby1.add(cars[i]);
    }
  }

  public void output(String inputFile){
    String filename = "output/" + inputFile.substring(0, inputFile.length() - 4) + ".txt";
    try {
      File file = new File(filename);
      if (file.createNewFile()) {
        System.out.println("File is created!");
      } else {
        System.out.println("File already exists.");
      }
      // Write to file
      try (FileWriter writer = new FileWriter(file)) {
        writer.write("Hello World!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
