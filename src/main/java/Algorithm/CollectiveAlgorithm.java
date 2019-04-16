package Algorithm;

import resources.CallResponse;
import resources.CommandRequest;

public class CollectiveAlgorithm extends BaseAlgorithm {
    public CollectiveAlgorithm(int elevatorCounter) {
        super(elevatorCounter);
    }

    @Override
    public CommandRequest getNextCommand(CallResponse allCalls) {
        return null;
    }
}
