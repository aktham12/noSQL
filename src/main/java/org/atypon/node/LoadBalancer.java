package org.atypon.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LoadBalancer {
    private static final Queue<ReadingNode> READING_NODES = new ConcurrentLinkedQueue<>();

    private static final List<Integer> PORTS = new ArrayList<>();
    private static int  runningOnDocker;
    private LoadBalancer(){ }

    public static void runNodes(int runningOnDocker) {
        LoadBalancer.runningOnDocker = runningOnDocker;
        if(runningOnDocker ==1) {
            buildDockerImage();
        }


        for(int i=0;i<3;i++) {
            ReadingNode node1 = new ReadingNode();
            if(runningOnDocker == 1) {
                node1.runDocker();
            }
            node1.runJars();
            READING_NODES.add(node1);
            PORTS.add(node1.getPort());
        }
    }

    private static void buildDockerImage() {
        String dockerBuildCommand = "docker build -f NodeDockerfile -t readingnode";
        ShellManager manager =ShellManager.create(dockerBuildCommand);
        manager.run();
    }

    public static void scaleHorizontal (int n) {
        for(int i=0;i<n;i++) {
            ReadingNode node1 = new ReadingNode();
            if(runningOnDocker == 1) {
                node1.runDocker();
            }
            node1.runJars();
            READING_NODES.add(node1);
            PORTS.add(node1.getPort());
        }
    }

    public static ArrayList<Integer> getPorts() {
        return (ArrayList<Integer>) PORTS;
    }

    public static void killNodes() {
        for(ReadingNode node :READING_NODES) {
            node.killNode();
        }
    }

    public static int getRandomPort() {
        return PORTS.get( (int)(Math.random() * PORTS.size()));
    }

}
