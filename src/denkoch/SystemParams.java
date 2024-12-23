package denkoch;

public interface SystemParams {

    Integer NUMBER_OF_PROCESSES = 7;
    Integer NUMBER_OF_REQUESTS = 100;

    Integer NUMBER_OF_TRACKS = 512;
    Integer SECTORS_PER_TRACK = 500;
    Double TIME_PER_TRACK = 0.5;
    Double TIME_TO_BOUNDARY_TRACK = 10.0;
    Integer RPM = 7500;
    Double AVG_ROTATION_DELAY = (double) (60 * 1000) / (2 * RPM);
    Double SECTOR_ACCESS_TIME = (double) (60 * 1000) / (SECTORS_PER_TRACK * RPM);

    Integer NUMBER_OF_BUFFERS = 10;
//    Integer LEFT_SEGMENT_SIZE = 2;
//    Integer MIDDLE_SEGMENT_SIZE = 2;
    Integer LEFT_SEGMENT_SIZE = 3;
    Integer MIDDLE_SEGMENT_SIZE = 3;
    Integer RIGHT_SEGMENT_SIZE = NUMBER_OF_BUFFERS - LEFT_SEGMENT_SIZE - MIDDLE_SEGMENT_SIZE;

    Double READ_SYSTEM_TIME = 0.15;
    Double WRITE_SYSTEM_TIME = 0.15;
    Double INTERRUPT_TIME = 0.05;
    Double QUANTUM = 20.0;
    Double PROCESSING_TIME = 7.0;

}
