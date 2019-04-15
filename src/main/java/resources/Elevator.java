package resources;

import java.io.Serializable;
import java.util.List;

public class Elevator implements Serializable {
    int id;
    int floor;
    List<Call> passengers;
    Status status;
}
