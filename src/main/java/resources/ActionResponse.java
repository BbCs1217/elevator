package resources;

import java.io.Serializable;
import java.util.List;

public class ActionResponse implements Serializable {
    String token;
    int timestamp;
    List<Elevator> elevators;
    boolean is_end;
}
