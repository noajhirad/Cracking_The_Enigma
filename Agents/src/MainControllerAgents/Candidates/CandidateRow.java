package MainControllerAgents.Candidates;

import DTOs.DTOBruteForceResult;
import DTOs.DTOMachineConfig;
import MainAgents.DTOParser;

public class CandidateRow {
    private String str;
    private DTOMachineConfig config;
    private String configStr;

    public CandidateRow(String str, String missionName, DTOMachineConfig config) {
        this.str = str;
        this.config = config;
        this.configStr = DTOParser.parseDTOMachineConfigToString(config);
    }

    // another constructor that gets only the formatted string - if it helps.
    public CandidateRow(String str, String missionName, String configStr) {
        this.str = str;
        this.config = null;
        this.configStr = configStr;
    }

    public CandidateRow(DTOBruteForceResult res) {
        this.str = res.getOutput();
        this.configStr = DTOParser.parseDTOMachineConfigToString(res.getConfig());
    }

    public String getStr() {
        return str;
    }

    public String getConfigStr() {
        return configStr;
    }

    public void setConfig(DTOMachineConfig config) {
        this.config = config;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}