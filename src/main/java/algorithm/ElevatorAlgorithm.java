package algorithm;

import resources.CallResponse;
import resources.CommandRequest;

public interface ElevatorAlgorithm {
    CommandRequest getNextCommand(CallResponse allCalls);

    public enum Method {FIFO, COLLECTIVE}
}
