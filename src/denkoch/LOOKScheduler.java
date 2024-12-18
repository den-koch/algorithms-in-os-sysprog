package denkoch;

import java.util.*;

import static denkoch.Logger.INITIAL_HEAD_POSITION;

public class LOOKScheduler extends DiskScheduler {

    private final List<Request> requests;
    private final Order order;

    public LOOKScheduler(Integer head, Order order) {
        super(head);
        this.requests = new ArrayList<>();
        this.order = order;
    }

    @Override
    public void addRequest(Request request) {
        requests.add(request);
    }

    @Override
    public void processRequests() {
        Logger.log(INITIAL_HEAD_POSITION, head);

        List<Request> requestsAsc = requests.stream()
                .filter(request -> request.getTrackNumber() >= head)
                .sorted(Comparator.comparing(Request::getTrackNumber))
                .toList();

        List<Request> requestsDesc = requests.stream()
                .filter(request -> request.getTrackNumber() < head)
                .sorted(Comparator.comparing(Request::getTrackNumber).reversed())
                .toList();

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
