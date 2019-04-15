package resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class CommandRequest {
    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    List<Command> commands;
}
