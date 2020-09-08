package com.codecool.processwatch.os;

import java.util.stream.Stream;
import java.util.*;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.User;

/**
 * A process source using the Java {@code ProcessHandle} API to retrieve information
 * about the current processes.
 */
public class OsProcessSource implements ProcessSource {
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Process> getProcesses() {

        Stream<ProcessHandle> processStream = ProcessHandle.allProcesses();

        //processStream.forEach(process -> printInfo(process));
        processStream.forEach(process -> addProcessToList(process));


        return Stream.of(new Process(1, 1, new User("root"), "init", new String[0]),
                new Process(42, 1, new User("Codecooler"), "processWatch", new String[]{"--cool=code", "-v"}),
                new Process(1, 2, new User("hula"), "hello", new String[0]));
    }

    private static List<Process> processList = new ArrayList<Process>();

    private static Stream<Process> stream = processList.stream();

    private static void addProcessToList(ProcessHandle process) {
        long processID = process.pid();
        Optional<ProcessHandle> parentProcess = process.parent();
        System.out.println(parentProcess);
        long parentPID;
        if (parentProcess.isPresent()) {
            parentPID = parentProcess.get().pid();
        } else {
            parentPID = 0;
        }
        ProcessHandle.Info processInfo = process.info();
        System.out.println(processInfo);
        Optional<String> user = processInfo.user();
        String userName;
        if (user.isPresent()) {
            userName = user.get();
        } else {
            userName = "";
        }
        Optional<String> cmd = processInfo.command();
        String command;
        if (cmd.isPresent()) {
            String commandLong = cmd.get();
            String[] parts = commandLong.split("/");
            command = parts[parts.length - 1];
        } else {
            command = "";
        }
        Optional<String[]> args = processInfo.arguments();
        String[] arguments;
        if (args.isPresent()) {
            arguments = args.get();
        } else {
            arguments = new String[0];
        }
        System.out.println("pid: " + processID + " parent pid: " + parentPID + " user: " + userName + " command: " + command + " arguments: " + Arrays.toString(arguments));
        Process p = new Process(1, 2, new User("hula"), "hello", new String[0]);
        processList.add(p);
    }

    private static void printInfo(ProcessHandle processHandle) {
        System.out.println("---------");
        System.out.println("Id: " + processHandle.pid());
        System.out.println("isAlive(): " + processHandle.isAlive());
        System.out.println("number of childrens: " + processHandle.children().count());
        System.out.println("isSupportsNormalTermination(): " + processHandle.supportsNormalTermination());

        ProcessHandle.Info processInfo = processHandle.info();
        System.out.println("Info: " + processInfo.toString());
        System.out.println("Info arguments().isPresent(): " + processInfo.arguments().isPresent());
        System.out.println("Info command().isPresent(): " + processInfo.command().isPresent());
        System.out.println("Info totalCpuDuration().isPresent(): " + processInfo.totalCpuDuration().isPresent());
        System.out.println("Info user().isPresent(): " + processInfo.user().isPresent());
    }
}
