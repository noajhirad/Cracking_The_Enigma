package SecondPageUBoat.ResultsArea;

import DTOs.DTOBruteForceResult;
import DTOs.DTOMachineConfig;
import MainUBoat.DTOParser;

public class ResultRow {
    private String str;
    private String allies;
    private DTOMachineConfig config;
    private String configStr;

    public ResultRow(String str, String allies, DTOMachineConfig config) {
        this.str = str;
        this.allies = allies;
        this.config = config;
        this.configStr = DTOParser.parseDTOMachineConfigToString(config);
    }

    public ResultRow(DTOBruteForceResult res) {
        this.str = res.getOutput();
        this.allies = res.getAllie();
        this.configStr = DTOParser.parseDTOMachineConfigToString(res.getConfig());
    }

    // another constructor that gets only the formatted string - if it helps.
    public ResultRow(String str, String allies, String configStr) {
        this.str = str;
        this.allies = allies;
        this.config = null;
        this.configStr = configStr;
    }

    public String getAllies() {
        return allies;
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

    public void setAllies(String allies) {
        this.allies = allies;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}