package com.codecool.processwatch.os;

import java.util.stream.Stream;
import java.util.*;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.domain.User;
import com.codecool.processwatch.gui.App;
import com.codecool.processwatch.gui.FxMain;

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

        processList.clear();

        processStream.forEach(process -> addProcessToList(process));

        Stream<Process> stream = processList.stream();

        return stream;

    }

    private static List<Process> processList = new ArrayList<>();

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
            userName = "Not available";
        }
        Optional<String> cmd = processInfo.command();
        String command;
        if (cmd.isPresent()) {
            String commandLong = cmd.get();
            String[] parts = commandLong.split("/");
            command = parts[parts.length - 1];
        } else {
            command = "Not available";
        }
        Optional<String[]> args = processInfo.arguments();
        String[] arguments;
        if (args.isPresent()) {
            if (args.get().length == 0) {
                arguments = new String[] {"Not available"};
            } else {
                arguments = args.get();
            }
        } else {
            arguments = new String[] {"Not available"};
        }
        Process p = new Process(processID, parentPID, new User(userName), command, arguments);

        if (ProcessWatchApp.option.equals("all")) {
            processList.add(p);
            System.out.println("valami");
        } else if (ProcessWatchApp.option.equals("user")) {
            if (ProcessWatchApp.userArg.equals(userName)) {
                processList.add(p);
                System.out.println("hellobello " +  ProcessWatchApp.userArg);
            }
        } else if (ProcessWatchApp.option.equals("ppid")) {
            if (ProcessWatchApp.ppidInput == parentPID) {
                processList.add(p);
            }
        }

    }
}
