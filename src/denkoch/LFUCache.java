package denkoch;

import java.util.Comparator;
import java.util.LinkedList;

import static denkoch.Logger.ADDED_TO_CACHE;
import static denkoch.Logger.MOVED_WITHIN_CACHE;
import static denkoch.SystemParams.*;

/**
 * Represents the Least Frequently Used (LFU) cache with three segments:
 * left, middle, and right, for managing cache buffers based on their frequency of usage.
 * <p>
 * Buffers are shifted between segments as their frequency of usage changes.
 * The cache evicts the least frequently used buffers when capacity limits are reached.
 */
public class LFUCache extends Cache {

    private final LinkedList<Buffer> leftSegment;
    private final LinkedList<Buffer> middleSegment;
    private final LinkedList<Buffer> rightSegment;

    public LFUCache() {
        this.leftSegment = new LinkedList<>();
        this.middleSegment = new LinkedList<>();
        this.rightSegment = new LinkedList<>();
    }

    /**
     * Retrieves a buffer by its track ID. If the buffer is not present, a new buffer is created
     * and added to the cache. Buffers are shifted between segments as their frequency changes.
     *
     * @param trackId the track ID of the buffer to retrieve.
     * @return the {@link Buffer} associated with the track ID.
     */
    @Override
    public Buffer getBuffer(Integer trackId) {
        if (containsBuffer(trackId)) {
            Buffer buffer = super.getBuffer(trackId);
            if (leftSegment.contains(buffer)) {
                leftSegment.remove(buffer);
                leftSegment.addFirst(buffer);
            } else if (middleSegment.contains(buffer)) {
                middleSegment.remove(buffer);
                shiftAndAddBuffer(buffer);
                buffer.increaseFrequency();
            } else {
                shiftAndAddBuffer(buffer);
                rightSegment.remove(buffer);
                buffer.increaseFrequency();
            }
            Logger.log(MOVED_WITHIN_CACHE, buffer);

        } else {
            Buffer buffer = new Buffer(trackId);
            shiftAndAddBuffer(buffer);
            putBuffer(trackId, buffer);
            Logger.log(ADDED_TO_CACHE, buffer);
        }

        return super.getBuffer(trackId);
    }

    /**
     * Shifts buffers between segments and adds a new buffer to the left segment.
     * Evicts the least frequently used buffers if capacity is exceeded.
     *
     * @param buffer the buffer to add to the cache.
     */
    private void shiftAndAddBuffer(Buffer buffer) {
        if (leftSegment.size() == LEFT_SEGMENT_SIZE) {
            Buffer lastLeftSegment = leftSegment.removeLast();
            if (middleSegment.size() == MIDDLE_SEGMENT_SIZE) {
                Buffer lastMiddleSegment = middleSegment.removeLast();
                if (rightSegment.size() == RIGHT_SEGMENT_SIZE) {
                    removeBufferFromRightSegment();
                }
                rightSegment.addFirst(lastMiddleSegment);
            }
            middleSegment.addFirst(lastLeftSegment);
        }
        leftSegment.addFirst(buffer);
    }

    /**
     * Removes the least frequently used buffer from the right segment.
     */
    private void removeBufferFromRightSegment() {
        if (rightSegment.isEmpty()) return;
        rightSegment.stream()
                .min(Comparator.comparingInt(Buffer::getFrequency))
                .ifPresent(buffer -> {
                    rightSegment.removeLastOccurrence(buffer);
                    removeBuffer(buffer.getBufferId());
                });
    }

    @Override
    public String toString() {
        return "\nLFUCache {" +
                "\n\tleftSegment = " + leftSegment +
                ", \n\tmiddleSegment = " + middleSegment +
                ", \n\trightSegment = " + rightSegment +
                "\n}";
    }
}
