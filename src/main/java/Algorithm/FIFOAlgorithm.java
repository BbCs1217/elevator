package Algorithm;

import resources.*;

import java.util.*;

public class FIFOAlgorithm extends BaseAlgorithm {
    List<Queue<Command>> elevatorCommands = new ArrayList<>();
    Map<Integer, Call> checker = new HashMap<>();
    Map<Integer, Integer> processing = new HashMap<>();
    Queue<Call> calls = new LinkedList<>();
    AlgorithmUtil algorithmUtil = new AlgorithmUtil();

    public FIFOAlgorithm(int elevatorCounter) {
        super(elevatorCounter);
        for(int i = 0; i < elevatorCounter; i++) {
            elevatorCommands.add(new LinkedList<>());
        }
    }

    @Override
    public CommandRequest getNextCommand(CallResponse allCalls) {
        for(Call c : allCalls.getCalls()) {
            if(!checker.containsKey(c.getId())) {
                boolean cont = false;
                for(Map.Entry<Integer, Integer> e : processing.entrySet()) {
                    if(e.getValue() == c.getId())
                        cont = true;
                }
                if(cont)
                    continue;
                calls.add(c);
                checker.put(c.getId(), c);
            }
        }
        for(int i = 0; i < elevatorCounter; i++) {
            Queue<Command> q = elevatorCommands.get(i);
            if(q.size() == 0) {
                processing.remove(i);
                Call c = calls.peek();
                if(c == null) {
                    Command stop = new Command();
                    stop.setElevator_id(i);
                    stop.setCommand(CommandEnum.STOP);
                    q.add(stop);
                   continue;
                }
                calls.remove(c);
                checker.remove(c.getId());
                processing.put(i, c.getId());
                q.addAll(algorithmUtil.getCommandsForDestFloor(i, algorithmUtil.getElevator(allCalls, i).getFloor(), c.getStart()));
                Command open = new Command();
                open.setElevator_id(i);
                open.setCommand(CommandEnum.OPEN);
                q.add(open);
                Command enter = new Command();
                enter.setElevator_id(i);
                enter.setCommand(CommandEnum.ENTER);
                enter.setCall_ids(new int[]{c.getId()});
                q.add(enter);
                Command close = new Command();
                close.setElevator_id(i);
                close.setCommand(CommandEnum.CLOSE);
                q.add(close);
                q.addAll(algorithmUtil.getCommandsForDestFloor(i, c.getStart(), c.getEnd()));
                q.add(open);
                Command exit = new Command();
                exit.setElevator_id(i);
                exit.setCall_ids(new int[]{c.getId()});
                exit.setCommand(CommandEnum.EXIT);
                q.add(exit);
                q.add(close);
            }
        }

        CommandRequest request = new CommandRequest();
        List<Command> commands = new ArrayList<>();
        for(Queue<Command> q : elevatorCommands) {
            if(q.size() != 0) {
                Command c = q.poll();
                commands.add(c);
            }
        }
        request.setCommands(commands);
        return request;
    }
}
