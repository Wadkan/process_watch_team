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

        processStream.forEach(process -> addProcessToList(process));

        return stream;

    }

    private static List<Process> processList = new ArrayList<Process>();

    private static Stream<Process> stream = processList.stream();

    private static void addProcessToList(ProcessHandle process) {
        long processID = process.pid();
        Optional<ProcessHandle> parentProcess = process.parent();
        long parentPID;
        if (parentProcess.isPresent()) {
            parentPID = parentProcess.get().pid();
        } else {
            parentPID = 0;
        }
        ProcessHandle.Info processInfo = process.info();
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
        Process p = new Process(processID, parentPID, new User(userName), command, arguments);
        processList.add(p);
    }
}
