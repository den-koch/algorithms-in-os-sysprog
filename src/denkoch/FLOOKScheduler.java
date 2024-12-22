package denkoch;

import java.util.*;
import java.util.stream.Collectors;

import static denkoch.Logger.FLOOK_QUEUES;
import static denkoch.Logger.INITIAL_HEAD_POSITION;

/**
 * Implements the FLOOK disk scheduling algorithm, a variant of LOOK.
 * <p>
 * The FLOOK algorithm processes disk I/O requests by dividing requests into active
 * and waiting queues. Active requests are processed first in the direction of the
 * current head position, and once all active requests are processed, the waiting
 * requests are moved into the active queue and processed.
 */
public class FLOOKScheduler extends DiskScheduler {

    private final List<Request> activeRequests;
    private final List<Request> waitingRequests;

    public FLOOKScheduler(Integer head) {
        super(head);
        this.activeRequests = new ArrayList<>();
        this.waitingRequests = new ArrayList<>();
    }

    /**
     * Adds a new request to the waiting queue.
     *
     * @param request the {@link Request} to add to the waiting queue.
     */
    @Override
    public void addRequest(Request request) {
        waitingRequests.add(request);
    }

    /**
     * Processes all requests using the FLOOK algorithm.
     * <p>
     * Requests are initially divided into two queues: active and waiting. Active requests
     * are processed in the direction of the current head position. Once active requests
     * are exhausted, waiting requests are moved to the active queue and processed.
     */
    @Override
    public void processRequests() {
        Logger.log(INITIAL_HEAD_POSITION, head);

        splitRequests();
        Logger.log(FLOOK_QUEUES, activeRequests, waitingRequests);

        while (!activeRequests.isEmpty() || !waitingRequests.isEmpty()) {

            if (activeRequests.isEmpty()) {
                activeRequests.addAll(waitingRequests);
                waitingRequests.clear();
            }

            // Separate requests into ascending and descending order based on the head's position
            List<Request> requestsAsc = activeRequests.stream()
                    .filter(request -> request.getTrackNumber() >= head)
                    .sorted(Comparator.comparing(Request::getTrackNumber))
                    .collect(Collectors.toList());

            List<Request> requestsDesc = activeRequests.stream()
                    .filter(request -> request.getTrackNumber() < head)
                    .sorted(Comparator.comparing(Request::getTrackNumber).reversed())
                    .collect(Collectors.toList());

            // Process requests based on the init order
            switch (order) {
                case ASC -> {
//                    Collections.reverse(requestsDesc);
                    requestsAsc.forEach(request -> {
                        performHeadMovement(request);
                        activeRequests.remove(request);
                    });
                    requestsDesc.forEach(request -> {
                        performHeadMovement(request);
                        activeRequests.remove(request);
                    });
                }
                case DESC -> {
//                    Collections.reverse(requestsAsc);
                    requestsDesc.forEach(request -> {
                        performHeadMovement(request);
                        activeRequests.remove(request);
                    });
                    requestsAsc.forEach(request -> {
                        performHeadMovement(request);
                        activeRequests.remove(request);
                    });
                }
            }
        }
    }

    /**
     * Splits waiting requests into the active request queue. This method moves half of the
     * waiting requests into the active queue to start processing them.
     */
    private void splitRequests() {
        activeRequests.addAll(waitingRequests.subList(0, waitingRequests.size() / 2));
//        activeRequests.addAll(waitingRequests.subList(0, waitingRequests.size() / 2 + 1));
        waitingRequests.removeAll(activeRequests);
    }
}
