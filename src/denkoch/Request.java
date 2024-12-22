package denkoch;

/**
 * Represents a disk I/O request with a specified track number and operation type.
 */
public class Request {

    /**
     * Enum representing the type of disk operation: READ or WRITE.
     */
    public enum RequestType {
        READ, WRITE
    }

    private final Integer trackNumber;
    private final RequestType request;

    /**
     * Constructs a new {@code Request} with the specified track number and request type.
     *
     * @param trackNumber the track number for the request.
     * @param request     the type of the request (READ or WRITE).
     */
    public Request(Integer trackNumber, RequestType request) {
        this.trackNumber = trackNumber;
        this.request = request;
    }

    public RequestType getRequest() {
        return request;
    }

    /**
     * Gets the track number associated with the request.
     *
     * @return the track number.
     */
    public Integer getTrackNumber() {
        return trackNumber;
    }

    @Override
    public String toString() {
        return "\n\tRequest {" +
                "trackNumber = " + trackNumber +
                ", request = " + request +
                "}";
    }
}
