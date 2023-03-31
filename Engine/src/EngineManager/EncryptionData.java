package EngineManager;

import java.io.Serializable;

public class EncryptionData implements Serializable {
    private String input;
    private String output;
    private long time;

    public EncryptionData(){
        this.input = "";
        this.output = "";
        this.time = 0;
    }

    public EncryptionData(String input, String output, long time){
        this.input = input;
        this.output = output;
        this.time = time;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public long getTime() {
        return time;
    }

    public void updateEncryptionData(long time, String input, String output){
        this.input += input;
        this.output += output;
        this.time += time;
    }
}
