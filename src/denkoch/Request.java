package denkoch;

public class Request {

    public enum RequestType {
        READ, WRITE
    }

    private final Integer trackNumber;
    private final RequestType request;

    public Request(Integer trackNumber, RequestType request) {
        this.trackNumber = trackNumber;
        this.request = request;
    }

    public RequestType getRequest() {
        return request;
    }

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
