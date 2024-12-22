package denkoch;

import java.util.*;

/**
 * Represents a process that generates and handles disk I/O requests.
 */
public class Process {
    private final int processId;
    private final LinkedList<Request> requests;

    /**
     * Constructs a new {@code Process} with the specified process ID.
     * Initializes an empty list of requests.
     *
     * @param processId the unique identifier of the process.
     */
    public Process(int processId) {
        this.processId = processId;
        requests = new LinkedList<>();
    }

    /**
     * Constructs a new {@code Process} with the specified process ID and initial list of requests.
     *
     * @param processId the unique identifier of the process.
     * @param requests  the initial list of {@link Request} instances for the process.
     */
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

    /**
     * Checks if the process has any pending requests.
     *
     * @return {@code true} if there are pending requests; {@code false} otherwise.
     */
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
