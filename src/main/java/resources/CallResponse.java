package resources;

import java.io.Serializable;
import java.util.List;

public class CallResponse implements Serializable {
    String token;
    List<Elevator> elevators;
    List<Call> calls;
    boolean is_end;
}