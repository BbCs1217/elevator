package Algorithm;

public abstract class BaseAlgorithm implements ElevatorAlgorithm {
    protected int elevatorCounter;

    public BaseAlgorithm(int elevatorCounter) {
        this.elevatorCounter = elevatorCounter;
    }
}
