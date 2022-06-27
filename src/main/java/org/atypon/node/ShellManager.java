package org.atypon.node;


import java.io.File;
import java.io.IOException;

public class ShellManager{
    private final String command;


    private ShellManager(String command) {
        this.command = command;
    }

    public static ShellManager create(String command) {
        return new ShellManager(command);
    }

    private static void runShellCommand(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c",command);
            builder.start();
        } catch (IOException e) {
            throw new IllegalArgumentException("the command is not correct");
        }
    }
    public void run() {
        runShellCommand(command);
    }
}
