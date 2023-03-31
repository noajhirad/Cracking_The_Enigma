package Contest;

import DTOs.DTOContestRow;
import Exceptions.ContestException;
import Users.Uboat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllContests {

    Set<Contest> allContests = new HashSet<>();

    public boolean isContestExists(String battleName){
        for (Contest contest: allContests)
            if (contest.getBattleName().equals(battleName))
                return true;

        return false;
    }

    public synchronized Contest createNewContest(BattleField battleField, Uboat uboat) throws Exception {
        Contest newContest = new Contest(battleField, uboat);
        if (isContestExists(battleField.getName()))
            throw new ContestException(ContestException.ErrorType.CONTEST_ALREADY_EXIST, battleField.getName());
        allContests.add(newContest);
        
        return newContest;
    }

    public List<DTOContestRow> getAllContestsDTO(){
        List<DTOContestRow> allDTOS = new ArrayList<>();

        for(Contest contest: allContests)
            allDTOS.add(contest.getContestDTO());

        return allDTOS;
    }

    public Set<Contest> getAllContests() {
        return allContests;
    }

    public DTOContestRow getDTOContestByUboat(String uboatName) {
        for (Contest contest: allContests){
            if(contest.getUboat().getUserName().equals(uboatName))
                return contest.getContestDTO();
        }
        return null;
    }

    public Contest getContestByUboat(String uboatName) {
        for (Contest contest: allContests){
            if(contest.getUboat().getUserName().equals(uboatName))
                return contest;
        }
        return null;
    }

    public void deleteContest(String uboatName) {
        Contest contestToDelete = getContestByUboat(uboatName);
        if(contestToDelete != null)
            allContests.remove(contestToDelete);
    }
}
