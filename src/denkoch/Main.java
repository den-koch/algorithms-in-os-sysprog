package denkoch;

import javax.swing.*;
import java.util.*;

import static denkoch.Logger.*;
import static denkoch.SystemParams.*;

/**
 * Main class for the disk scheduling simulation program.
 * It handles the initialization, simulation, and logging of processes, requests, and disk scheduling.
 */
public class Main {

    private final static Integer HEAD = 50;

    /**
     * Runs the simulation of the disk scheduler and processes.
     *
     * @param processes     the list of {@link Process} instances.
     * @param diskScheduler the {@link DiskScheduler} used to handle disk requests.
     * @param lfuCache      the {@link LFUCache} to store frequently used data.
     */
    public static void runSimulation(List<Process> processes, DiskScheduler diskScheduler, LFUCache lfuCache) {
        double totalSimulationTime = 0;
        DiskInterruptHandler diskInterruptHandler = new DiskInterruptHandler();

        while (hasPendingProcesses(processes)) {
            for (Process process : processes) {
                double currentQuantum = 0;
                LinkedList<Request> requests = process.getRequests();

                while (!requests.isEmpty() && currentQuantum < QUANTUM) {
                    Request request = requests.removeFirst();

                    int track = request.getTrackNumber();
                    Request.RequestType operation = request.getRequest();

                    lfuCache.getBuffer(track);
                    diskScheduler.addRequest(request);

                    double accessTime = AVG_ROTATION_DELAY + SECTOR_ACCESS_TIME;
                    switch (operation) {
                        case READ -> {
                            accessTime += READ_SYSTEM_TIME;
                            diskInterruptHandler.handleInterrupt(request);
                        }
                        case WRITE -> {
                            accessTime += WRITE_SYSTEM_TIME;
                            diskInterruptHandler.handleInterrupt(request);
                        }
                    }

                    currentQuantum += accessTime + diskInterruptHandler.getInterruptHandlingTime();

                    currentQuantum += PROCESSING_TIME;

                    Logger.log(REQUEST_OPERATION_INFO, process.getProcessId(), track, operation, accessTime + PROCESSING_TIME);

                    totalSimulationTime += currentQuantum;
                }

                if (currentQuantum >= QUANTUM) {
                    Logger.log(PROCESSING_QUANTUM_TIME, currentQuantum);
                }
            }
        }

        diskScheduler.processRequests();
        totalSimulationTime += diskScheduler.getScheduleTime();
        Logger.log(TOTAL_SIMULATION_TIME, totalSimulationTime);

        GraphPlotter.plot(diskScheduler.getRequestTimes(), diskScheduler.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Logger.log(SELECT_SCHEDULER);
        String scheduler = scanner.nextLine();

        DiskScheduler diskScheduler;
        switch (scheduler) {
            case "FIFO" -> diskScheduler = new FIFOScheduler(HEAD);
            case "LOOK" -> diskScheduler = new LOOKScheduler(HEAD);
            case "FLOOK" -> diskScheduler = new FLOOKScheduler(HEAD);
            default -> {
                throw new IllegalArgumentException(INVALID_SCHEDULER_ERROR);
            }
        }

        if (diskScheduler instanceof LOOKScheduler || diskScheduler instanceof FLOOKScheduler) {
            Logger.log(SELECT_ORDER);
            String order = scanner.nextLine();
            switch (order) {
                case "ASC" -> diskScheduler.setOrder(DiskScheduler.Order.ASC);
                case "DESC" -> diskScheduler.setOrder(DiskScheduler.Order.DESC);
                default -> {
                    throw new IllegalArgumentException(INVALID_SCHEDULER_ERROR);
                }
            }
        }

        Logger.log(GENERATE_REQUESTS);
        String requestsGenerated = scanner.nextLine();

        List<Process> processes = new ArrayList<>();
        if (requestsGenerated.equals(YES)) {
            for (int i = 1; i <= NUMBER_OF_PROCESSES; i++) {
                processes.add(new Process(i));
            }
            generateRequests(processes);
        } else if (requestsGenerated.equals(NO)) {
            createRequests(processes);
            System.out.println(processes + "\n");
        }


        LFUCache lfuCache = new LFUCache();

        runSimulation(processes, diskScheduler, lfuCache);
        System.out.println(lfuCache);

    }

    private static boolean hasPendingProcesses(List<Process> processes) {
        return processes.stream().anyMatch(Process::hasPendingRequests);
    }


    private static void generateRequests(List<Process> processes) {
        Random rand = new Random();
        Request.RequestType[] types = Request.RequestType.values();

        for (int i = 0; i < NUMBER_OF_REQUESTS; ++i) {
            int processId = i % processes.size();
            int track = rand.nextInt(NUMBER_OF_TRACKS);

            Request.RequestType operation = types[rand.nextInt(types.length)];
            processes.get(processId).addRequest(new Request(track, operation));
        }
    }

    private static void createRequests(List<Process> processes) {

        Process process1 = new Process(1, new LinkedList<>(List.of(
                new Request(95, Request.RequestType.READ),
                new Request(164, Request.RequestType.WRITE),
                new Request(199, Request.RequestType.WRITE),
                new Request(30, Request.RequestType.READ),
                new Request(75, Request.RequestType.READ)
        )));

        Process process2 = new Process(2, new LinkedList<>(List.of(
                new Request(130, Request.RequestType.WRITE),
                new Request(62, Request.RequestType.READ),
                new Request(11, Request.RequestType.READ),
                new Request(95, Request.RequestType.WRITE),
                new Request(119, Request.RequestType.WRITE)
        )));

        processes.addAll(List.of(process1, process2));

//        Process process1 = new Process(1, new LinkedList<>(List.of(
//                new Request(1, Request.RequestType.READ),
//                new Request(2, Request.RequestType.WRITE),
//                new Request(3, Request.RequestType.WRITE),
//                new Request(4, Request.RequestType.READ),
//                new Request(1, Request.RequestType.WRITE),
//                new Request(5, Request.RequestType.READ),
//                new Request(3, Request.RequestType.READ),
//                new Request(6, Request.RequestType.WRITE),
//                new Request(7, Request.RequestType.READ),
//                new Request(2, Request.RequestType.READ),
//                new Request(8, Request.RequestType.READ),
//                new Request(8, Request.RequestType.WRITE),
//                new Request(9, Request.RequestType.READ)
//        )));
//
//        processes.add(process1);
    }


}
