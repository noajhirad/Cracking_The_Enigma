package Users;

import DTOs.ContestStatus;
import EngineManager.EngineManager;

import java.util.*;
import static Constants.Constants.*;

public class UserManager {

    private Map<String,Uboat> allUboats;
    private Map<String,Allie> allAllies;
    private Map<String,Agent> allAgents;

    public UserManager() {
        allUboats = new HashMap<>();
        allAllies = new HashMap<>();
        allAgents = new HashMap<>();
    }

    public synchronized void addUser(String username, String role) {
        switch (role){
            case(AGENT):
                allAgents.put(username, new Agent(username));
                break;
            case (ALLIES):
                allAllies.put(username, new Allie(username));
                break;
            case (UBOAT):
                allUboats.put(username, new Uboat(username, new EngineManager()));
        } // todo: maybe exception in default
    }

    public synchronized void removeUser(String username, String role) {
        switch (role) {
            case (AGENT):
                allAgents.remove(username);
                break;
            case (ALLIES):
                allAllies.remove(username);
                break;
            case (UBOAT):
                allUboats.remove(username);
        }
    }

//    public synchronized Set<String> getUsers() {
//        return Collections.unmodifiableSet(allUboats.keySet());
//    }

    public boolean isUserExists(String username) {
        return allAgents.containsKey(username) || allAllies.containsKey(username)
                || allUboats.containsKey(username);
    }

    public synchronized Uboat getUboat(String username) {
        return allUboats.get(username);
    }

    public synchronized Allie getAllie(String username) {
        return allAllies.get(username);
    }

    public synchronized Agent getAgent(String username) {
        return allAgents.get(username);
    }

    public synchronized void addAgentUser(String username, String taskSize, String alliesTeam, String threadsNum, ContestStatus status) {
        Agent newAgent = new Agent(username, taskSize, threadsNum);
        allAgents.put(username, newAgent);
        getAllie(alliesTeam).addAgent(newAgent, status);
        // todo: if allie name illegal: throw an error
    }

    public Collection<String> getAllAlliesNames() {
        return allAllies.keySet();
    }
}
