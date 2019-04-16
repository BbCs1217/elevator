package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import resources.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiCaller {
    private String baseURL;

    public ApiCaller(String baseURL) {
        this.baseURL = baseURL;
    }

    public String start(String userKey, int problemNumber, int elevatorCounter) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        params.put("user_key", userKey);
        params.put("problem_id", String.valueOf(problemNumber));
        params.put("number_of_elevators", String.valueOf(elevatorCounter));

        HttpHelper http = new HttpHelper();
        String response = http.send(SendType.POST, baseURL + "start/" + userKey + "/" + problemNumber + "/" + elevatorCounter, null, http.getParamsString(params));
        ObjectMapper mapper = new ObjectMapper();
        try {
            StartResponse startResponse = mapper.readValue(response, StartResponse.class);
            return startResponse.getToken();
        } catch (IOException e) {
            return "";
        }
    }

    public CallResponse oncalls(String token) {
        Map<String, String> header = new HashMap<>();
        header.put("X-Auth-Token", token);
        HttpHelper helper = new HttpHelper();
        String response = helper.send(SendType.GET, baseURL + "oncalls", header, null);
        ObjectMapper mapper = new ObjectMapper();
        CallResponse callResponse = null;
        try {
            callResponse = mapper.readValue(response, CallResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return callResponse;
    }

    public ActionResponse action(String token, CommandRequest request) throws JsonProcessingException {
        return action(token, request.getCommands());
    }

    public ActionResponse action(String token, List<Command> commands) throws JsonProcessingException {
        Map<String, String> header = new HashMap<>();
        header.put("X-Auth-Token", token);
        header.put("Content-Type", "application/json");
        HttpHelper helper = new HttpHelper();
        CommandRequest request = new CommandRequest();
        request.setCommands(commands);
        String response = helper.send(SendType.POST, baseURL + "action", header, new ObjectMapper().writeValueAsString(request));
        ObjectMapper mapper = new ObjectMapper();
        ActionResponse actionResponse = null;
        try {
            actionResponse = mapper.readValue(response, ActionResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actionResponse;
    }
}
