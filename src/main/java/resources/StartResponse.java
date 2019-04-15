package resources;

import java.io.Serializable;
import java.util.List;

public class StartResponse implements Serializable {
    String token;
    int timestamp;
    List<Elevator> elevators;
    boolean is_end;
}
