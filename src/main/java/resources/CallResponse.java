package resources;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

public class CallResponse implements Serializable {
    String token;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    int timestamp;
    List<Elevator> elevators;
    List<Call> calls;
    boolean is_end;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    public boolean isIs_end() {
        return is_end;
    }

    @JsonIgnore
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Elevators\n");
        for(Elevator e : getElevators()) {
            builder.append("ID[").append(e.getId()).append("] STATUS[").append(e.getStatus().name()).append("] FLOOR[").append(e.getFloor()).append("]\n");
            builder.append("\tPassengers\n");
            for(Call c : e.getPassengers()) {
                builder.append("\t[").append(c.getId()).append("] START[").append(c.getStart()).append("} END[").append(c.getEnd()).append("]\n");
            }
        }
        builder.append("Calls\n");
        for(Call c : getCalls()) {
            builder.append("\t[").append(c.getId()).append("] START[").append(c.getStart()).append("} END[").append(c.getEnd()).append("]\n");
        }
        return builder.toString();
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;

    }
}
