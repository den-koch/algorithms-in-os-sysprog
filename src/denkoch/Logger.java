package denkoch;

public interface Logger {

    String SELECT_SCHEDULER = "Select disk scheduler {FIFO, LOOK, FLOOK}: ";
    String YES = "YES";
    String NO = "NO";
    String GENERATE_REQUESTS = "Generate requests? {" + YES + ", " + NO + "}: ";

    String REQUEST_OPERATION_INFO = "DRIVER: Request {processId = %d, track = %d, requestType = %s}, Request processing time = %.3f\n";
    String TOTAL_SCHEDULING_TIME = "DRIVER: Total scheduling time = %.3f ms \n";
    String TOTAL_SIMULATION_TIME = "DRIVER: Total simulation time = %.3f ms \n";
    String PROCESSING_QUANTUM_TIME = "DRIVER: Processing time = %.3f ms exceeded quantum time!\n";

    String INITIAL_HEAD_POSITION = "SCHEDULER: Initial disk head position = %s track\n";
    String MOVE_DISK_HEAD = "SCHEDULER: Disk head moved to %s track {time = %.2f}\n";
    String FLOOK_QUEUES = "SCHEDULER: FLOOK scheduler queues = { \nactive = %s, \nwaiting = %s}\n\n";

    String ADDED_TO_CACHE = "CACHE: %s added to cache\n";
    String MOVED_WITHIN_CACHE = "CACHE: %s moved within cache\n";

    String INVALID_SCHEDULER_ERROR = "Invalid disk scheduler\n";

    static void log(String message, Object... args) {
        System.out.printf(message, args);
    }

}
