package denkoch;

import java.util.LinkedList;
import java.util.Queue;

import static denkoch.Logger.*;

public class FIFOScheduler extends DiskScheduler {

    private final Queue<Request> requests;

    public FIFOScheduler(Integer head) {
        super(head);
        this.requests = new LinkedList<>();
    }

    @Override
    public void addRequest(Request request) {
        requests.add(request);
    }

    @Override
    public void processRequests() {
        Logger.log(INITIAL_HEAD_POSITION, head);

        requests.forEach(request -> {
            performHeadMovement(request);
            requests.remove(request);
        });
    }
}
