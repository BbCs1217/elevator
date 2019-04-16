package algorithm;

public class ElevatorAlgorithmFactory {
    public static ElevatorAlgorithm getElevatorAlgorithm(ElevatorAlgorithm.Method method, int elevatorCounter, int maxFloor) {
        switch (method) {
            case FIFO:
                return new FIFOAlgorithm(elevatorCounter, maxFloor);
            case COLLECTIVE:
                return new CollectiveAlgorithm(elevatorCounter, maxFloor);
        }
        return null;
    }
}
