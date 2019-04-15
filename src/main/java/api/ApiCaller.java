package api;

import resources.ActionResponse;
import resources.CallResponse;
import resources.Command;

import java.util.List;

public interface ApiCaller {
    String start(String userKey, int problemNumber, int elevatorCounter);
    List<CallResponse> oncalls(String token);
    ActionResponse action(String token, List<Command> commands);
}
