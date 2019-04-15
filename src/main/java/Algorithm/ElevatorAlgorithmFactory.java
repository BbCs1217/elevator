package Algorithm;

public class ElevatorAlgorithmFactory {
    public static ElevatorAlgorithm getElevatorAlgorithm(ElevatorAlgorithm.Method method) {
        switch (method) {
            case FIFO:
                return new FIFOAlghrithm();
            case COLLECTIVE:
                return new CollectiveAlgorithm();
        }
        return null;
    }
}
