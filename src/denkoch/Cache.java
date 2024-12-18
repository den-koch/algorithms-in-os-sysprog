package denkoch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static denkoch.SystemParams.NUMBER_OF_BUFFERS;

public class Cache {

    private final Map<Integer, Buffer> cache;

    public Cache() {
        this.cache = new LinkedHashMap<>();;
    }

    public Buffer getBuffer(Integer trackId) {
        return cache.get(trackId);
    }

    public boolean containsBuffer(Integer trackId) {
        return cache.containsKey(trackId);
    }

    public void putBuffer(Integer trackId, Buffer buffer) {
        cache.put(trackId, buffer);
    }

    public void removeBuffer(Integer trackId) {
        cache.remove(trackId);
    }
}
