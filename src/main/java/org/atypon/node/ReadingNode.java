package org.atypon.node;


public class ReadingNode {

    private static String nodeFilePath = "ReadingNode/ReadingNode-1.0-SNAPSHOT.jar";
    private final int port;
    private final String NODE_DOCKER_RUN_COMMAND;
    private final String NODE_JAR_RUN_COMMAND;

    public ReadingNode() {
        this.port = PortGenerator.getInstance().generateNewPort();
        NODE_DOCKER_RUN_COMMAND = "docker run -p" + this.port+":"+this.port+ "--env"+"port="+this.port +" readingnode";
        NODE_JAR_RUN_COMMAND = "java -jar " +nodeFilePath+ " "+ this.port;
    }

    public void killNode() {
        String killCommand ="wmic Path win32_process Where \"CommandLine Like '%\"ReadingNode-1.0-SNAPSHOT.jar%'\" Call Terminate";
        ShellManager shellManager = ShellManager.create(killCommand);
        shellManager.run();
    }


    public void runDocker() {
        ShellManager shellManager = ShellManager.create(NODE_DOCKER_RUN_COMMAND);
        shellManager.run();
    }

    public void runJars() {
        ShellManager shellManager = ShellManager.create(NODE_JAR_RUN_COMMAND);
        shellManager.run();
    }

    public int getPort() {
        return port;
    }

}
