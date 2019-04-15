package resources;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Command {
    int elevator_id;

    public int getElevator_id() {
        return elevator_id;
    }

    public void setElevator_id(int elevator_id) {
        this.elevator_id = elevator_id;
    }

    public CommandEnum getCommand() {
        return command;
    }

    public void setCommand(CommandEnum command) {
        this.command = command;
    }

    public int[] getCall_ids() {
        return call_ids;
    }

    public void setCall_ids(int[] call_ids) {
        this.call_ids = call_ids;
    }

    CommandEnum command;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    int[] call_ids;
}
