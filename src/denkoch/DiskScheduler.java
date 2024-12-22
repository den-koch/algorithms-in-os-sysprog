package denkoch;

import static denkoch.Logger.MOVE_DISK_HEAD;
import static denkoch.Logger.TOTAL_SCHEDULING_TIME;
import static denkoch.SystemParams.*;
import static denkoch.SystemParams.AVG_ROTATION_DELAY;

/**
 * Abstract base class for disk scheduling algorithms.
 * Handles common functionality such as managing the disk head position and calculating scheduling times.
 */
public abstract class DiskScheduler {

    /**
     * Enumeration defining the possible ordering of requests.
     */
    enum Order {
        ASC, DESC
    }

    protected double scheduleTime;
    protected Integer head;
    protected Order order;

    /**
     * Constructs a new {@code DiskScheduler} with the specified initial disk head position.
     *
     * @param head the initial position of the disk head.
     */
    public DiskScheduler(Integer head) {
        this.head = head;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public abstract void addRequest(Request request);
    public abstract void processRequests();

    /**
     * Gets and logs the total scheduling time accumulated during processing.
     *
     * @return the total scheduling time.
     */
    public double getScheduleTime() {
        Logger.log(TOTAL_SCHEDULING_TIME, scheduleTime);
        return scheduleTime;
    }

    /**
     * Performs the disk head movement to the track specified by a request.
     * Calculates and accumulates the time taken for the movement.
     *
     * @param request the {@link Request} containing the target track.
     */
    public void performHeadMovement(Request request){
        int track = request.getTrackNumber();
        int distance = Math.abs(track - head);
        double request_time;

        if (track == 0 || track == NUMBER_OF_TRACKS) {
            request_time = TIME_TO_BOUNDARY_TRACK;
        } else {
            request_time = distance * TIME_PER_TRACK;
        }
        request_time += AVG_ROTATION_DELAY;

        Logger.log(MOVE_DISK_HEAD, track, request_time);

        scheduleTime += request_time;
        head = track;
    }

}
