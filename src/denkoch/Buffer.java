package denkoch;

import java.util.Objects;

/**
 * Represents a buffer in the system with a unique identifier and access frequency.
 */
public class Buffer {
    private final Integer bufferId;
    private Integer frequency;

    /**
     * Constructs a new {@code Buffer} with the specified buffer ID.
     * The initial frequency is set to 1.
     *
     * @param bufferId the unique identifier of the buffer.
     */
    public Buffer(Integer bufferId) {
        this.bufferId = bufferId;
        this.frequency = 1;
    }

    public Integer getBufferId() {
        return bufferId;
    }

    public Integer getFrequency() {
        return frequency;
    }

    /**
     * Increases the access frequency of the buffer by 1.
     */
    public void increaseFrequency() {
        frequency++;
    }

    /**
     * Compares this buffer to another object for equality.
     * Two buffers are considered equal if their buffer IDs are the same.
     *
     * @param obj the object to compare with.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Buffer buffer = (Buffer) obj;
        return Objects.equals(bufferId, buffer.getBufferId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(bufferId);
    }

    @Override
    public String toString() {
        return "Buffer {" +
                "bufferId = " + bufferId +
                ", frequency = " + frequency +
                '}';
    }
}
