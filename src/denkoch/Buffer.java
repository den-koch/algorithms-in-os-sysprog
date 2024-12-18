package denkoch;

import java.util.Objects;

public class Buffer {
    private final Integer bufferId;
    private Integer frequency;

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

    public void increaseFrequency() {
        frequency++;
    }

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
