package denkoch;

import java.util.*;

public class Process {
    private final int processId;
    private final LinkedList<Request> requests;

    public Process(int processId) {
        this.processId = processId;
        requests = new LinkedList<>();
    }

    public Process(int processId, LinkedList<Request> requests) {
        this.processId = processId;
        this.requests = requests;
    }

    public int getProcessId() {
        return processId;
    }

    public LinkedList<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public boolean hasPendingRequests(){
        return !requests.isEmpty();
    }

    @Override
    public String toString() {
        return "\nProcess {" +
                "processId = " + processId +
                ", requests = " + requests +
                "}";
    }
}
