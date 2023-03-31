package Users;

import BruteForce.BruteForceSettings;
import BruteForce.DecryptionManager;
import DTOs.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Allie {
    private String userName;
    private Set<Agent> activeAgents;
    private Set<Agent> waitingAgents;
    private DecryptionManager dm;
    private String uboatName = null;
    private int taskSize = 0;
    private boolean isReady = false;
    private List<DTOBruteForceResult> allCandidates;
    private int version;
    private AtomicLong totalMissions = new AtomicLong(0);
    private AtomicLong createdMissions = new AtomicLong(0);
    private AtomicLong completedMissions = new AtomicLong(0);
    private final Object candidatesAndVersionLock = new Object();


    public Allie(String username){
        this.userName = username;
        activeAgents = new HashSet<>();
        allCandidates = new ArrayList<>();
        version = 0;
        waitingAgents = new HashSet<>();
    }

    public void setUboatName(String uboat){ this.uboatName = uboat; }

    public String getUboatName() {
        return uboatName;
    }

    public void addAgent(Agent agent, ContestStatus status){
        if (status == ContestStatus.WAITING)
            activeAgents.add(agent);
        else
            waitingAgents.add(agent);
    }

    public void setDM(DecryptionManager dm){
        this.dm = dm;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Agent> getAllAgents() {
        return activeAgents;
    }

    public DecryptionManager getDm() {
        return dm;
    }

    public List<DTOAgentRow> getAllAgentsDTO() {
        List<DTOAgentRow> res = new ArrayList<>();
        for (Agent agent : activeAgents){
            res.add(new DTOAgentRow(agent.getUserName(),
                    agent.getThreadsNumber(),
                    agent.getTaskSize()));
        }
        return res;
    }

    public String getAgentsNumber() {
        return Integer.toString(activeAgents.size());
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public void startBruteForce(Uboat uboat) throws Exception{
        BruteForceSettings bruteForceSettings = uboat.getBruteForceSettings();
        bruteForceSettings.setTaskSize(taskSize);
        bruteForceSettings.setAgentsNumber(activeAgents.size());
        dm = new DecryptionManager(bruteForceSettings, uboat.getEnigmaCopy());
        dm.startBruteForce();
        isReady = true;
    }

    public synchronized DTOBruteForceTasksBatch getMissionsFromQueue(int batchSize) throws InterruptedException {
//        System.out.println(dm.getMissionsBatch(batchSize).getToDecrypt());
        return dm.getMissionsBatch(batchSize);
    }

    public boolean isReady() {
        return isReady;
    }

    public synchronized void receiveCandidates(List<DTOBruteForceResult> allCandidates, Uboat uboat) {
        synchronized (candidatesAndVersionLock) {
            this.allCandidates.addAll(allCandidates);
            this.version = this.allCandidates.size();
        }
        uboat.receiveCandidates(allCandidates);
    }

    public List<DTOBruteForceResult> getAllCandidates(int fromIndex){
        if (fromIndex < 0 || fromIndex > version)
            fromIndex = 0;

        return allCandidates.subList(fromIndex, version);
    }

    public int getVersion(){
        return version;
    }

    public boolean hasAgents() {
        return activeAgents.size() > 0;
    }

    private List<DTOProgressInfo> getAgentsProgress() {
        completedMissions.set(0);
        List<DTOProgressInfo> res = new ArrayList<>();
        for(Agent agent: activeAgents){
            DTOProgressInfo agentProgress = agent.getProgressInfo();
            if(agentProgress != null) {
                completedMissions.addAndGet(Long.parseLong(agentProgress.getCompletedMissions()));
            }
            res.add(agentProgress);
        }
        return res;
    }

    public DTOAllieProgressInfo getProgress() {
        if(dm == null)
            return null;

        totalMissions.set(dm.getTotalMissions());
        createdMissions.set(dm.getCreatedMissions());
        return new DTOAllieProgressInfo(getAgentsProgress(), totalMissions.get(), createdMissions.get(), completedMissions.get());
    }

    public void clearContestData() {
        dm.interruptMissionDivider();
        dm = null;
        uboatName = null;
        taskSize = 0;
        isReady = false;
        allCandidates = new ArrayList<>();
        version =0;
        totalMissions = new AtomicLong(0);
        createdMissions = new AtomicLong(0);
        completedMissions = new AtomicLong(0);
        activeAgents.addAll(waitingAgents);
        waitingAgents = new HashSet<>();

        for(Agent agent: activeAgents)
            agent.setProgressInfo(null);
    }

    public boolean isOnWaitingAgent(Agent agent) {
        return waitingAgents.contains(agent);
    }

    public Object getCandidatesAndVersionLock() {
        return candidatesAndVersionLock;
    }

    public void setIsReady(boolean b) {
        isReady = false;
    }
}
