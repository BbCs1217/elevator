package main;

import api.ApiCaller;
import com.fasterxml.jackson.core.JsonProcessingException;
import resources.ActionResponse;
import resources.CallResponse;
import resources.Command;
import resources.CommandEnum;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorClient {
    public static void main(String[] args) throws UnsupportedEncodingException, JsonProcessingException {
        String baseURL = System.getenv().getOrDefault("ELEVATOR_BASE_URL", "http://localhost/");
        String userKey = System.getenv().getOrDefault("ELEVATOR_USER_KEY", "guest");
        int problemNumber = 0;
        int elevatorCounter = 1;
        ApiCaller caller = new ApiCaller(baseURL);
        String token = caller.start(userKey, problemNumber, elevatorCounter);
        System.out.println("TOKEN : " + token);

        CallResponse callResponse = caller.oncalls(token);
        System.out.println(callResponse.toString());

        List<Command> commands = new ArrayList<>();
        Command c = new Command();
        c.setCommand(CommandEnum.UP);
        commands.add(c);
        ActionResponse actionResponse = caller.action(token, commands);
        callResponse = caller.oncalls(token);
        System.out.println(callResponse.toString());
    }
}
