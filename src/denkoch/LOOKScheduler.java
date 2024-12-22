package denkoch;

import java.util.*;

import static denkoch.Logger.INITIAL_HEAD_POSITION;

/**
 * Implements the LOOK disk scheduling algorithm.
 * <p>
 * The LOOK algorithm processes disk I/O requests by moving the disk head
 * in one direction (ascending or descending) until no more requests remain
 * in that direction, then reverses if necessary. It avoids unnecessary
 * traversal to tracks with no requests.
 */
public class LOOKScheduler extends DiskScheduler {

    private final List<Request> requests;

    public LOOKScheduler(Integer head) {
        super(head);
        this.requests = new ArrayList<>();
    }

    /**
     * Adds a new request to the list of requests.
     *
     * @param request the {@link Request} to add to the scheduler.
     */
    @Override
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * Processes all requests using the LOOK algorithm.
     * <p>
     * Requests are first divided into two lists: those with tracks greater than
     * or equal to the current head position, and those with tracks less than the
     * current head position. Requests are then processed based on the scheduler's
     * current order (`ASC` or `DESC`).
     */
    @Override
    public void processRequests() {
        Logger.log(INITIAL_HEAD_POSITION, head);

        // Separate requests into ascending and descending order based on the head's position
        List<Request> requestsAsc = requests.stream()
                .filter(request -> request.getTrackNumber() >= head)
                .sorted(Comparator.comparing(Request::getTrackNumber))
                .toList();

        List<Request> requestsDesc = requests.stream()
                .filter(request -> request.getTrackNumber() < head)
                .sorted(Comparator.comparing(Request::getTrackNumber).reversed())
                .toList();

        // Process requests based on the init order
        switch (order) {
            case ASC -> {
                requestsAsc.forEach(request -> {
                    performHeadMovement(request);
                    requests.remove(request);
                });
                requestsDesc.forEach(request -> {
                    performHeadMovement(request);
                    requests.remove(request);
                });
            }
            case DESC -> {
                requestsDesc.forEach(request -> {
                    performHeadMovement(request);
                    requests.remove(request);
                });
                requestsAsc.forEach(request -> {
                    performHeadMovement(request);
                    requests.remove(request);
                });
            }
        }

    }
}
