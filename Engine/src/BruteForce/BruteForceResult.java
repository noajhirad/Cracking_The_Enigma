package BruteForce;

import DTOs.DTOMachineConfig;

public class BruteForceResult {
    private DTOMachineConfig config;
    private String output;
    private String agentNumber;

    public BruteForceResult(DTOMachineConfig config, String output, String agentNumber){
        this.config = config;
        this.output = output;
        this.agentNumber = agentNumber;
    }

    public DTOMachineConfig getConfig() {
        return config;
    }

    public String getOutput() {
        return output;
    }

    public String getAgentNumber() {
        return agentNumber;
    }
}
