package denkoch;

import java.util.LinkedList;
import java.util.Queue;

import static denkoch.Logger.*;

/**
 * Implements the First-In-First-Out (FIFO) disk scheduling algorithm.
 * <p>
 * Requests are processed in the order they are added, without reordering.
 * The disk head processes requests sequentially as they arrive in the queue.
 */
public class FIFOScheduler extends DiskScheduler {

    private final Queue<Request> requests;

    public FIFOScheduler(Integer head) {
        super(head);
        this.requests = new LinkedList<>();
    }

    /**
     * Adds a new request to the FIFO queue.
     *
     * @param request the {@link Request} to add to the scheduler.
     */
    @Override
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * Processes all requests in the FIFO queue in the order they were added.
     * The disk head is moved to each request's track, and the time spent on each request is logged.
     */
    @Override
    public void processRequests() {
        Logger.log(INITIAL_HEAD_POSITION, head);

        while (!requests.isEmpty()) {
            Request request = requests.poll();
            if (request != null) {
                performHeadMovement(request);
            }
        }
    }
}
