package resources;

import java.io.Serializable;
import java.util.List;

public class Elevator implements Serializable {
    int id;
    int floor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Call> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Call> passengers) {
        this.passengers = passengers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    List<Call> passengers;
    Status status;
}
