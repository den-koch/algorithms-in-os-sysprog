package denkoch;

import java.util.*;
import java.util.stream.Collectors;

import static denkoch.Logger.FLOOK_QUEUES;
import static denkoch.Logger.INITIAL_HEAD_POSITION;

public class FLOOKScheduler extends DiskScheduler {

    private final List<Request> activeRequests;
    private final List<Request> waitingRequests;
    private final Order order;

    public FLOOKScheduler(Integer head, Order order) {
        super(head);
        this.activeRequests = new ArrayList<>();
        this.waitingRequests = new ArrayList<>();
        this.order = order;
    }

    @Override
    public void addRequest(Request request) {
        waitingRequests.add(request);
    }

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

            List<Request> requestsAsc = activeRequests.stream()
                    .filter(request -> request.getTrackNumber() >= head)
                    .sorted(Comparator.comparing(Request::getTrackNumber))
                    .collect(Collectors.toList());

            List<Request> requestsDesc = activeRequests.stream()
                    .filter(request -> request.getTrackNumber() < head)
                    .sorted(Comparator.comparing(Request::getTrackNumber).reversed())
                    .collect(Collectors.toList());

            switch (order) {
                case ASC -> {
                    Collections.reverse(requestsDesc);
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
                    Collections.reverse(requestsAsc);
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

    private void splitRequests() {
        activeRequests.addAll(waitingRequests.subList(0, waitingRequests.size() / 2));
//        activeRequests.addAll(waitingRequests.subList(0, waitingRequests.size() / 2 + 1));
        waitingRequests.removeAll(activeRequests);
    }
}
