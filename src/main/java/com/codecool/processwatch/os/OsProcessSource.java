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

        processStream.forEach(process -> printInfo(process));
        processStream.forEach(process -> addProcessToList(process));


        return Stream.of(new Process(1,  1, new User("root"), "init", new String[0]),
                         new Process(42, 1, new User("Codecooler"), "processWatch", new String[] {"--cool=code", "-v"}));
    }

    private static List<Process> processList = new LinkedList<Process>();

    private static void addProcessToList(ProcessHandle process){
        Process p = new Process(1,2, new User("hula"), "hello", new String[0]);
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
