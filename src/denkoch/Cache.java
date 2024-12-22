package denkoch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a cache for storing and retrieving {@link Buffer} objects by their track IDs.
 * Implements basic cache operations such as adding, retrieving, and removing buffers.
 */
public class Cache {

    private final Map<Integer, Buffer> cache;

    public Cache() {
        this.cache = new LinkedHashMap<>();
    }

    /**
     * Retrieves the {@link Buffer} associated with the specified track ID.
     *
     * @param trackId the track ID of the buffer to retrieve.
     * @return the {@link Buffer} associated with the track ID, or {@code null} if not found.
     */
    public Buffer getBuffer(Integer trackId) {
        return cache.get(trackId);
    }

    /**
     * Checks if the cache contains a {@link Buffer} associated with the specified track ID.
     *
     * @param trackId the track ID to check.
     * @return {@code true} if the buffer exists in the cache; {@code false} otherwise.
     */
    public boolean containsBuffer(Integer trackId) {
        return cache.containsKey(trackId);
    }

    /**
     * Adds a {@link Buffer} to the cache with the specified track ID as the key.
     *
     * @param trackId the track ID to associate with the buffer.
     * @param buffer  the {@link Buffer} to add to the cache.
     */
    public void putBuffer(Integer trackId, Buffer buffer) {
        cache.put(trackId, buffer);
    }

    /**
     * Removes the {@link Buffer} associated with the specified track ID from the cache.
     *
     * @param trackId the track ID of the buffer to remove.
     */
    public void removeBuffer(Integer trackId) {
        cache.remove(trackId);
    }
}
