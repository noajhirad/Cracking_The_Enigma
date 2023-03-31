package DTOs;

import java.util.Objects;

public class DTOBruteForceResult {
    private DTOMachineConfig config;
    private String output;
    private String agent;
    private String allie;


    public DTOBruteForceResult(DTOMachineConfig config, String output, String agent, String allie){
        this.config = config;
        this.output = output;
        this.agent = agent;
        this.allie = allie;
    }

    public DTOMachineConfig getConfig() {
        return config;
    }

    public String getOutput() {
        return output;
    }

    public String getAgent() {
        return agent;
    }

    public String getAllie() {
        return allie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOBruteForceResult that = (DTOBruteForceResult) o;
        return Objects.equals(config, that.config) && Objects.equals(output, that.output) &&
                Objects.equals(agent, that.agent) && Objects.equals(allie, that.allie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config, output, agent, allie);
    }
}
