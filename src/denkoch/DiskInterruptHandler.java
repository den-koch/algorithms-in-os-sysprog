package denkoch;

import static denkoch.Logger.INTERRUPT_HANDLED;
import static denkoch.SystemParams.INTERRUPT_TIME;

/**
 * The DiskInterruptHandler class handles disk-related interrupts and provides information
 * about the handling process.
 */
public class DiskInterruptHandler {

    /**
     * Handles the interrupt by logging the provided request.
     *
     * @param request the request associated with the disk interrupt.
     */
    public void handleInterrupt(Request request) {
        Logger.log(INTERRUPT_HANDLED, request);
    }

    /**
     * Retrieves the time required to handle an interrupt.
     *
     * @return the interrupt handling time in seconds.
     */
    public double getInterruptHandlingTime() {
        return INTERRUPT_TIME;
    }
}
