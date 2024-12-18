package denkoch;

import java.util.*;

import static denkoch.Logger.*;
import static denkoch.SystemParams.*;

public class Main {

    private final static Integer HEAD = 50;
    private static final DiskScheduler.Order ORDER = DiskScheduler.Order.ASC;

    public static void runSimulation(List<Process> processes, DiskScheduler diskScheduler, LFUCache lfuCache) {
        double totalSimulationTime = 0;

        while (hasPendingProcesses(processes)) {
            for (Process process : processes) {
                double processTime = 0;
                LinkedList<Request> requests = process.getRequests();

                while (!requests.isEmpty() && processTime < QUANTUM) {
                    Request request = requests.removeFirst();

                    int track = request.getTrackNumber();
                    Request.RequestType operation = request.getRequest();

                    lfuCache.getBuffer(track);
                    diskScheduler.addRequest(request);

                    double accessTime = AVG_ROTATION_DELAY + SECTOR_ACCESS_TIME + INTERRUPT_TIME;
                    switch (operation) {
                        case READ -> accessTime += READ_SYSTEM_TIME;
                        case WRITE -> accessTime += WRITE_SYSTEM_TIME;
                    }
                    processTime += accessTime + PROCESSING_TIME;

                    Logger.log(REQUEST_OPERATION_INFO, process.getProcessId(), track, operation, accessTime + PROCESSING_TIME);

                    totalSimulationTime += processTime;
                }

                if (processTime >= QUANTUM) {
                    Logger.log(PROCESSING_QUANTUM_TIME, processTime);
                }
            }
        }

        diskScheduler.processRequests();
        totalSimulationTime += diskScheduler.getScheduleTime();
        Logger.log(TOTAL_SIMULATION_TIME, totalSimulationTime);

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Logger.log(SELECT_SCHEDULER);
        String scheduler = scanner.nextLine();

        DiskScheduler diskScheduler;
        switch (scheduler) {
            case "FIFO" -> diskScheduler = new FIFOScheduler(HEAD);
            case "LOOK" -> diskScheduler = new LOOKScheduler(HEAD, ORDER);
            case "FLOOK" -> diskScheduler = new FLOOKScheduler(HEAD, ORDER);
            default -> {
                throw new IllegalArgumentException(INVALID_SCHEDULER_ERROR);
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

//        Process process1 = new Process(1, new LinkedList<>(List.of(
//                new Request(95, Request.RequestType.READ),
//                new Request(164, Request.RequestType.WRITE),
//                new Request(199, Request.RequestType.WRITE),
//                new Request(30, Request.RequestType.READ),
//                new Request(75, Request.RequestType.READ)
//        )));
//
//        Process process2 = new Process(2, new LinkedList<>(List.of(
//                new Request(130, Request.RequestType.WRITE),
//                new Request(62, Request.RequestType.READ),
//                new Request(11, Request.RequestType.READ),
//                new Request(95, Request.RequestType.WRITE),
//                new Request(119, Request.RequestType.WRITE)
//        )));
        Process process1 = new Process(1, new LinkedList<>(List.of(
                new Request(95, Request.RequestType.READ),
                new Request(164, Request.RequestType.WRITE),
                new Request(11, Request.RequestType.WRITE),
                new Request(95, Request.RequestType.READ),
                new Request(199, Request.RequestType.READ)
        )));

        Process process2 = new Process(2, new LinkedList<>(List.of(
                new Request(130, Request.RequestType.WRITE),
                new Request(119, Request.RequestType.READ),
                new Request(62, Request.RequestType.READ),
                new Request(75, Request.RequestType.WRITE),
                new Request(30, Request.RequestType.WRITE)
        )));

        Process process3 = new Process(3, new LinkedList<>(List.of(
                new Request(75, Request.RequestType.READ),
                new Request(199, Request.RequestType.WRITE),
                new Request(30, Request.RequestType.WRITE)
        )));

        Process process4 = new Process(4, new LinkedList<>(List.of(
                new Request(11, Request.RequestType.READ),
                new Request(130, Request.RequestType.WRITE),
                new Request(30, Request.RequestType.WRITE)
        )));

        processes.addAll(List.of(process1, process2, process3, process4));

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
    }


}
