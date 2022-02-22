import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class Street {
    String id;
    int startIntersectionId;
    int endIntersectionId;
    int length;
    int greenLightDuration = 1;
    List<Car> carsOnStreet = new ArrayList<>();
    List<Car> carsOnStandby = new ArrayList<>();
    List<Car> carsOnStandby1 = new ArrayList<>();

    public Street(String streetId, int startIntersectionId, int endIntersectionId, int length) {
        this.id = streetId;
        this.startIntersectionId = startIntersectionId;
        this.endIntersectionId = endIntersectionId;
        this.length = length;
    }

    public int getPotentialCars() {
        int count = carsOnStandby.size();
        for (Car car : carsOnStreet) {
            if (car.getTimeUntilNextPhase() < greenLightDuration) count++;
        }
        return count;
    }

    public void run() {
        for (Car car : carsOnStreet) {
            car.run();
        }
    }

    public void updateStandby() {
        if (carsOnStreet.size() > 0 && carsOnStreet.get(0).isStandby) {
            carsOnStandby.add(carsOnStreet.remove(0));
        }
    }

    public void activate(HashMap<String, Street> out) {
        if (carsOnStandby.size() == 0) return;
        Car temp = carsOnStandby.remove(0);
        if (temp.phase + 1  == temp.paths.size()) {
            System.out.println(456);
            Processor.getInstance().updateScore(temp.travelTime);
            return;
        }
//        System.out.println(temp.phase);
        temp.changePhase();
//        System.out.println(out);
//        System.out.println(temp.paths.get(temp.phase - 1));
        out.get(temp.paths.get(temp.phase - 1)).carsOnStreet.add(temp);
    }
}