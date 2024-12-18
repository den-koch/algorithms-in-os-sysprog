package denkoch;

import static denkoch.Logger.MOVE_DISK_HEAD;
import static denkoch.Logger.TOTAL_SCHEDULING_TIME;
import static denkoch.SystemParams.*;
import static denkoch.SystemParams.AVG_ROTATION_DELAY;

public abstract class DiskScheduler {

    enum Order {
        ASC, DESC
    }

    protected double scheduleTime;
    protected Integer head;

    public DiskScheduler(Integer head) {
        this.head = head;
    }

    public abstract void addRequest(Request request);
    public abstract void processRequests();

    public double getScheduleTime() {
        Logger.log(TOTAL_SCHEDULING_TIME, scheduleTime);
        return scheduleTime;
    }

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
