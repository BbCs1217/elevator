package algorithm;

import resources.Command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class BaseAlgorithm implements ElevatorAlgorithm {
    int minFloor = 1;
    int maxFloor;
    int elevatorMaxPassenger = 8;
    int elevatorCounter;
    List<Queue<Command>> elevatorCommands = new ArrayList<>();

    public BaseAlgorithm(int elevatorCounter, int maxFloor) {
        this.elevatorCounter = elevatorCounter;
        this.maxFloor = maxFloor;
        for (int i = 0; i < elevatorCounter; i++) {
            elevatorCommands.add(new LinkedList<>());
        }
    }
}
