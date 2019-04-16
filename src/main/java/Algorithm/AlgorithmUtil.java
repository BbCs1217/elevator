package Algorithm;

import resources.CallResponse;
import resources.Command;
import resources.CommandEnum;
import resources.Elevator;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmUtil {
    public List<Command> getCommandsForDestFloor(int elevatorId, int current, int dest) {
        List<Command> ret = new ArrayList<>();
        if (current == dest)
            return ret;
        Command c = new Command();
        c.setElevator_id(elevatorId);
        if (dest > current)
            c.setCommand(CommandEnum.UP);
        else
            c.setCommand(CommandEnum.DOWN);

        int diff = Math.abs(current - dest);
        for(int i = 0; i < diff; i++) {
            ret.add(c);
        }
        Command stop = new Command();
        stop.setElevator_id(elevatorId);
        stop.setCommand(CommandEnum.STOP);
        ret.add(stop);
        return ret;
    }

    public Elevator getElevator(CallResponse response, int elevatorId) {
        for(Elevator e : response.getElevators()) {
            if(e.getId() == elevatorId)
                return e;
        }
        return null;
    }
}
