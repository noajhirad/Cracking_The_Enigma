package SecondPageAllies.TeamsCandidates;

import DTOs.DTOBruteForceResult;
import DTOs.DTOMachineConfig;
import MainAllies.DTOParser;

public class CandidatesRow {
    private String str;
    private String agent;
    private DTOMachineConfig config;
    private String configStr;

    public CandidatesRow(String str, String agent, DTOMachineConfig config) {
        this.str = str;
        this.agent = agent;
        this.config = config;
        this.configStr = DTOParser.parseDTOMachineConfigToString(config);
    }

    // another constructor that gets only the formatted string - if it helps.
    public CandidatesRow(String str, String agent, String configStr) {
        this.str = str;
        this.agent = agent;
        this.config = null;
        this.configStr = configStr;
    }

    public CandidatesRow(DTOBruteForceResult dtoBruteForceResult) {
        this.str = dtoBruteForceResult.getOutput();
        this.agent = dtoBruteForceResult.getAgent();
        this.configStr = DTOParser.parseDTOMachineConfigToString(dtoBruteForceResult.getConfig());
    }

    public String getAgent() {
        return agent;
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