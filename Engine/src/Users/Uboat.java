package Users;

import BruteForce.BruteForceSettings;
import Contest.BattleField;
import Contest.Contest;
import DTOs.*;
import EngineManager.UBoatEngineManager;
import EnigmaMachine.EnigmaMachine;
import Exceptions.ContestException;
import Exceptions.GeneralException;

import java.util.*;

public class Uboat {
    private String userName;
    private UBoatEngineManager engineManager;
    private Set<Allie> allAllies;
    private Map<Allie,Boolean> areAlliesReady;
    private BattleField battleField;
    private Boolean isReady = false;
    private List<DTOBruteForceResult> allCandidates;
    private int version = 0;
    private String winningTeam = null;
    private String input;
    private DTOMachineConfig machineConfig;
    private Contest contest;
    private final Object candidateAndVersionLock = new Object();

    public Uboat(String userName, UBoatEngineManager engineManager) {
        this.userName = userName;
        this.engineManager = engineManager;
        this.allAllies = new HashSet<>();
        this.areAlliesReady = new HashMap<>();
        this.allCandidates = new ArrayList<>();
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Allie> getAllAllies() {
        return allAllies;
    }

    public UBoatEngineManager getEngineManager() {
        return engineManager;
    }

    public Map<Allie,Boolean> getAreAlliesReady() {
        return areAlliesReady;
    }

    public synchronized void addAllie(Allie allie) throws Exception{
        if (battleField.getNumOfAllies() == allAllies.size())
            throw new ContestException(ContestException.ErrorType.MAX_ALLIES_NUM_REACHED);
        allAllies.add(allie);
        areAlliesReady.put(allie,false);
    }

    public void setAllieToReady(Allie allie) throws Exception {
        if(!allie.hasAgents())
            throw new GeneralException(GeneralException.ErrorType.NO_AGENTS_FOUND);
        areAlliesReady.put(allie,true);

        if(checkIfReady())
            contest.setContestStatus(ContestStatus.ACTIVE);
    }

    public List<DTOTeamRow> getAllTeamsDTO() {
        List<DTOTeamRow> res = new ArrayList<>();
        for (Allie allie : allAllies){
            res.add(new DTOTeamRow(allie.getUserName(),
                    allie.getAgentsNumber(),
                    Integer.toString(allie.getTaskSize())));
        }
        return res;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public void markAsReady() {
        isReady = true;

        if(checkIfReady())
            contest.setContestStatus(ContestStatus.ACTIVE);
    }

    private boolean checkIfAlliesReady() {
        for (boolean bool : areAlliesReady.values()) {
            if(!bool)
                return false;
        }

        return true;
    }

    private boolean checkNumOfTeams(){
        return allAllies.size() == battleField.getNumOfAllies();
    }

    public boolean checkIfReady(){
        return isReady && checkNumOfTeams() && checkIfAlliesReady();
    }

    // get enigmaCopy
    public EnigmaMachine getEnigmaCopy() throws Exception{
        return engineManager.getEnigmaCopy();
    }
    // get BruteForceSetting;
    public BruteForceSettings getBruteForceSettings() {
        return new BruteForceSettings(battleField.getDifficulty(),engineManager.getToDecrypt());
    }

    public List<DTOBruteForceResult> getAllCandidates(Integer fromIndex) {
        if (fromIndex < 0 || fromIndex > version)
            fromIndex = 0;

        return allCandidates.subList(fromIndex, version);
    }

    public int getVersion() {
        return version;
    }

    public synchronized void receiveCandidates(List<DTOBruteForceResult> allCandidates) {
        synchronized (candidateAndVersionLock) {
            this.allCandidates.addAll(allCandidates);
            this.version = this.allCandidates.size();
        }
        for(DTOBruteForceResult res : allCandidates){
            if(isCorrectCandidate(res) && winningTeam == null){
                winningTeam = res.getAllie();
                contest.setContestStatus(ContestStatus.FINISHED);
            }
        }
    }

    private boolean isCorrectCandidate(DTOBruteForceResult res) {
        DTOMachineConfig config = res.getConfig();
        String output = res.getOutput();

        return (input.equals(output) && machineConfig.equals(config));
    }

    public String checkForWinner() {
        return winningTeam;
    }

    public DTOEncryptResponse encrypt(String input) throws Exception {
        // save original config & string
        this.input = input;
        this.machineConfig = engineManager.getCurrentMachineConfig();

        // encrypt
        String output = engineManager.bruteForceEncryption(input);
        DTOMachineConfig machineConfig = engineManager.getCurrentMachineConfig();
        return new DTOEncryptResponse(machineConfig, output);
    }

    public void removeAllie(Allie allie) {

        allAllies.remove(allie);
        areAlliesReady.remove(allie);

        if(allAllies.size() == 0){
            contest.setContestStatus(ContestStatus.WAITING);
        }
    }

    public void clearContestData(){
        isReady = false;
        allCandidates = new ArrayList<>();
        version = 0;
        winningTeam = null;
        input = null;
        machineConfig = null;
    }

    public Object getCandidateAndVersionLock() {
        return candidateAndVersionLock;
    }
}