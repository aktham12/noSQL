package org.atypon.node;



public class PortGenerator {
    private int port;
    private PortGenerator() {
        this.port = 12000;
    }

    public static PortGenerator getInstance() {
        return PortGeneratorHolder.INSTANCE;
    }
    public int generateNewPort() {
        return ++port;
    }

    public int getPort() {
        return port;
    }
    private static class PortGeneratorHolder {
        private static final PortGenerator INSTANCE = new PortGenerator();
    }
}
