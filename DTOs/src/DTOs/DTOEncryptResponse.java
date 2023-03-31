package DTOs;

public class DTOEncryptResponse {
    DTOMachineConfig machineConfig;
    String output;

    public DTOEncryptResponse(DTOMachineConfig machineConfig, String output) {
        this.machineConfig = machineConfig;
        this.output = output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setMachineConfig(DTOMachineConfig machineConfig) {
        this.machineConfig = machineConfig;
    }

    public String getOutput() {
        return output;
    }

    public DTOMachineConfig getMachineConfig() {
        return machineConfig;
    }
}
