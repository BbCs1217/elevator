package main;

import algorithm.ElevatorAlgorithm;
import algorithm.ElevatorAlgorithmFactory;
import api.ApiCaller;
import com.fasterxml.jackson.core.JsonProcessingException;
import resources.ActionResponse;
import resources.CallResponse;
import resources.CommandRequest;

import java.io.UnsupportedEncodingException;

public class ElevatorClient {
    public static void main(String[] args) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String baseURL = System.getenv().getOrDefault("ELEVATOR_BASE_URL", "http://localhost/");
        String userKey = System.getenv().getOrDefault("ELEVATOR_USER_KEY", "guest");
        int problemNumber = 2;
        int[] maxFloors = {6, 25, 25};
        int elevatorCounter = 4;
        ApiCaller caller = new ApiCaller(baseURL);
        String token = caller.start(userKey, problemNumber, elevatorCounter);
        System.out.println("TOKEN : " + token);

        int timestamp = 0;
//        ElevatorAlgorithm algorithm = ElevatorAlgorithmFactory.getElevatorAlgorithm(ElevatorAlgorithm.Method.FIFO, elevatorCounter);
        ElevatorAlgorithm algorithm = ElevatorAlgorithmFactory.getElevatorAlgorithm(ElevatorAlgorithm.Method.COLLECTIVE, elevatorCounter, maxFloors[problemNumber]);
        boolean end = false;
        while (end == false) {
            Thread.sleep(10);
            System.out.println(timestamp);
            CallResponse callResponse = caller.oncalls(token);
            CommandRequest request = algorithm.getNextCommand(callResponse);
            ActionResponse actionResponse = caller.action(token, request);
            end = actionResponse.isIs_end();
            timestamp = actionResponse.getTimestamp();
        }
        System.out.println("ENDS");
//        System.out.println(callResponse.toString());
//
//        List<Command> commands = new ArrayList<>();
//        Command c = new Command();
//        c.setCommand(CommandEnum.UP);
//        commands.add(c);
//        ActionResponse actionResponse = caller.action(token, commands);
//        callResponse = caller.oncalls(token);
//        System.out.println(callResponse.toString());
    }
}
