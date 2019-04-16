package algorithm;

public class ElevatorAlgorithmFactory {
    public static ElevatorAlgorithm getElevatorAlgorithm(ElevatorAlgorithm.Method method, int elevatorCounter) {
        switch (method) {
            case FIFO:
                return new FIFOAlgorithm(elevatorCounter);
            case COLLECTIVE:
                return new CollectiveAlgorithm(elevatorCounter);
        }
        return null;
    }
}
