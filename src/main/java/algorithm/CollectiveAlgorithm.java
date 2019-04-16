package algorithm;

import resources.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectiveAlgorithm extends BaseAlgorithm {
    public enum Direction {
        UP, DOWN, STOP
    }

    Direction[] elevatorDirection;
    Direction[] elevatorDirectionPrev;
    Map<Integer, List<Call>> allCalls = new HashMap<>();

    public CollectiveAlgorithm(int elevatorCounter) {
        super(elevatorCounter);
        elevatorDirection = new Direction[elevatorCounter];
        elevatorDirectionPrev = new Direction[elevatorCounter];
        for (int i = 0; i < elevatorCounter; i++) {
            elevatorDirection[i] = elevatorDirectionPrev[i] = Direction.STOP;
        }
    }

    @Override
    public CommandRequest getNextCommand(CallResponse callResponse) {
        CommandRequest request = new CommandRequest();
        List<Command> commands = new ArrayList<>();
        allCalls.clear();
        //build allCalls
        for (Call c : callResponse.getCalls()) {
            List<Call> list;
            if (!allCalls.containsKey(c.getStart())) {
                list = new ArrayList<>();
                allCalls.put(c.getStart(), list);
            } else {
                list = allCalls.get(c.getStart());
            }
            list.add(c);
        }

        for (Elevator e : callResponse.getElevators()) {
            CommandEnum nextCommand = null;
            int[] exitCalls = getExitCall(e);
            int[] enterCalls = getEnterCall(e);
            int[] enterReverseCalls = getReverseEnterCall(e);
            boolean reverseEnter = (e.getPassengers().size() == 0 && enterReverseCalls.length != 0);
            Status s = e.getStatus();
            //올라가거나 내려가는중이면
            if (s == Status.UPWARD || s == Status.DOWNWARD) {
                //현재층에 내리거나 탈사람이 있으면 중지
                if ((exitCalls.length != 0 || enterCalls.length != 0) || reverseEnter) {
                    if (reverseEnter)
                        elevatorDirectionPrev[e.getId()] = elevatorDirectionPrev[e.getId()] == Direction.UP ? Direction.DOWN : Direction.UP;
                    elevatorDirection[e.getId()] = Direction.STOP;
                    nextCommand = CommandEnum.STOP;
                } else {
                    //내리거나 탈사람 없으면 계속 감
                    nextCommand = e.getStatus() == Status.UPWARD ? CommandEnum.UP : CommandEnum.DOWN;
                }
            } else { //멈춤상태이면
                switch (s) {
                    //문이 닫긴상태이면
                    case STOPPED:
                        //내리거나 탈사람 있으면 문 오픈
                        if (exitCalls.length != 0 || enterCalls.length != 0) {
                            nextCommand = CommandEnum.OPEN;
                        } else {//내리거나 탈사람 없으면
                            //계속 멈춰있었으면, 올라가는 방향으로 설정하고 find
                            if (elevatorDirectionPrev[e.getId()] == Direction.STOP)
                                elevatorDirectionPrev[e.getId()] = Direction.UP;
                            //승객이 있으면 원래 방향 유지하여 계속 ㄱ
                            //승객이 없으나 가던방향에 태울 사람이 있으면 계속 ㄱ
                            if (e.getPassengers().size() != 0 || findSameDirectionCall(e)) {
                                nextCommand = elevatorDirectionPrev[e.getId()] == Direction.UP ? CommandEnum.UP : CommandEnum.DOWN;
                                elevatorDirection[e.getId()] = elevatorDirectionPrev[e.getId()];
                            } else {
                                //반대로 ㄱ
                                if (findInverseDirectionCall(e)) {
                                    nextCommand = elevatorDirectionPrev[e.getId()] == Direction.UP ? CommandEnum.DOWN : CommandEnum.UP;
                                    elevatorDirection[e.getId()] = elevatorDirectionPrev[e.getId()] = elevatorDirectionPrev[e.getId()] == Direction.UP ? Direction.DOWN : Direction.UP;
                                } else if (reverseEnter) {     //현재층에 반대로 가는친구들이 있다
                                    nextCommand = CommandEnum.OPEN;
                                    elevatorDirection[e.getId()] = elevatorDirectionPrev[e.getId()] = elevatorDirectionPrev[e.getId()] == Direction.UP ? Direction.DOWN : Direction.UP;
                                } else { //반대도 없으면 stop
                                    nextCommand = CommandEnum.STOP;
                                    elevatorDirection[e.getId()] = Direction.STOP;
                                    elevatorDirectionPrev[e.getId()] = Direction.STOP;
                                }
                            }
                        }
                        break;
                    case OPENED:
                        if (enterCalls.length != 0) {
                            nextCommand = CommandEnum.ENTER;
                        } else if (exitCalls.length != 0) {
                            nextCommand = CommandEnum.EXIT;
                        } else {
                            //열어둔상태에서 태우기/내리기 완료했는데, 엘리베이터에 사람이 아무도 없는경우
                            //연 김에 반대방향으로 갈사람 태우자
                            if (reverseEnter && elevatorDirectionPrev[e.getId()] != Direction.STOP) {
                                elevatorDirectionPrev[e.getId()] = elevatorDirectionPrev[e.getId()] == Direction.UP ? Direction.DOWN : Direction.UP;
                                nextCommand = CommandEnum.ENTER;
                            } else
                                nextCommand = CommandEnum.CLOSE;
                        }
                        break;
                }
            }
            if (nextCommand == null) {
                //심각한 오류..
                return null;
            }
            Command eCommand = new Command();
            eCommand.setElevator_id(e.getId());
            eCommand.setCommand(nextCommand);
            if (nextCommand == CommandEnum.EXIT) {
                eCommand.setCall_ids(exitCalls);
            } else if (nextCommand == CommandEnum.ENTER) {
                eCommand.setCall_ids(enterCalls);
            }
            commands.add(eCommand);
        }
        request.setCommands(commands);
        return request;
    }

    private int[] getReverseEnterCall(Elevator e) {
        if (!allCalls.containsKey(e.getFloor()))
            return new int[0];
        Direction direction = elevatorDirectionPrev[e.getId()];
        List<Integer> calls = new ArrayList<>();
        for (Call call : allCalls.get(e.getFloor())) {
            if (direction != getDirection(call.getStart(), call.getEnd())) {
                calls.add(call.getId());
            }
        }
        return calls.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean findInverseDirectionCall(Elevator e) {
        if (elevatorDirectionPrev[e.getId()] == Direction.STOP)
            return false;
        Direction dir = elevatorDirectionPrev[e.getId()];
        int currentFloor = e.getFloor();
        for (Map.Entry<Integer, List<Call>> entry : allCalls.entrySet()) {
            if (dir == Direction.UP) {
                if (entry.getKey() < currentFloor)
                    return true;
            } else {
                if (entry.getKey() > currentFloor)
                    return true;
            }
        }
        return false;
    }

    private boolean findSameDirectionCall(Elevator e) {
        if (elevatorDirectionPrev[e.getId()] == Direction.STOP)
            return false;
        Direction dir = elevatorDirectionPrev[e.getId()];
        int currentFloor = e.getFloor();
        for (Map.Entry<Integer, List<Call>> entry : allCalls.entrySet()) {
            if (dir == Direction.UP) {
                if (entry.getKey() > currentFloor)
                    return true;
            } else {
                if (entry.getKey() < currentFloor)
                    return true;
            }
        }
        return false;
    }

    public int[] getExitCall(Elevator e) {
        List<Integer> calls = new ArrayList<>();
        for (Call c : e.getPassengers()) {
            if (c.getEnd() == e.getFloor()) {
                calls.add(c.getId());
            }
        }
        return calls.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getEnterCall(Elevator e) {
        if (!allCalls.containsKey(e.getFloor()))
            return new int[0];
        int passengerCnt = e.getPassengers().size();
        Direction direction = elevatorDirectionPrev[e.getId()];
        List<Integer> calls = new ArrayList<>();
        for (Call call : allCalls.get(e.getFloor())) {
            if (direction == getDirection(call.getStart(), call.getEnd())) {
                if(++passengerCnt > elevatorMaxPassenger)
                    break;
                calls.add(call.getId());
            }
        }
        return calls.stream().mapToInt(Integer::intValue).toArray();
    }

    public Direction getDirection(int start, int end) {
        if (end > start) return Direction.UP;
        else if (end < start) return Direction.DOWN;
        else return Direction.STOP;
    }

    public Direction getElevatorDirection(Elevator e) {
        if (e.getPassengers().size() == 0)
            return Direction.STOP;
        return getDirection(e.getPassengers().get(0).getStart(), e.getPassengers().get(0).getEnd());
    }
}
