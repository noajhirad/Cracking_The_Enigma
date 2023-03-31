package BruteForce;

import DTOs.DTOBruteForceQueueItem;
import DTOs.DTOBruteForceTasksBatch;
import EnigmaMachine.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class DecryptionManager {
    private int taskSize;
    private BruteForceDifficulty difficulty;
    private EnigmaMachine enigmaMachine;
    private String toDecrypt;
    private boolean isAllMissionsInserted = false;
    private ArrayBlockingQueue<DTOBruteForceQueueItem> queue;
    private AtomicLong totalMissions;
    private AtomicLong missionsTakenFromQueue;
    private AtomicLong missionsCreated;
    private Thread missionDividerThread;

    public DecryptionManager(BruteForceSettings settings, EnigmaMachine machine) {
        this.taskSize = settings.getTaskSize();
        this.difficulty = settings.getDifficulty();
        this.enigmaMachine = machine;
        this.toDecrypt = settings.getToDecrypt();
        totalMissions = new AtomicLong(0);
        missionsTakenFromQueue = new AtomicLong(0);
        missionsCreated = new AtomicLong(0);
    }

    public void startBruteForce() throws Exception {

        updateTotalMissions(difficulty);
        queue = new ArrayBlockingQueue<>(1000);

        Runnable startBF = () -> {
            try {
                switch (difficulty) {
                    case EASY:      // find only rotors positions
                        divideTasksEasy();
                        break;
                    case MEDIUM:    // find rotors positions & reflector
                        divideTasksMedium();
                        break;
                    case HARD:      // rotors order & find rotors positions & reflector
                        divideTasksHard();
                        break;
                    case IMPOSSIBLE:    // we don't know anything
                        divideTasksImpossible();
                        break;
                }
            } catch (Exception e) { }
        };

        missionDividerThread = new Thread(startBF, "Mission dvider");
        missionDividerThread.start();
    }

    private void updateTotalMissions(BruteForceDifficulty difficulty) {

        long easyEncryptions = (long) Math.pow(enigmaMachine.getVocabulary().size(), enigmaMachine.getRotorsCount());
        long easyTasks = (long) Math.ceil(easyEncryptions * 1.0 / taskSize);
        int reflectorsCount = enigmaMachine.getReflectorsCount();
        int rotorsCount = enigmaMachine.getRotorsCount();
        int totalNumOfRotors = enigmaMachine.getAmountOfDefinedRotors();

        switch (difficulty) {
            case EASY:
                totalMissions.set(easyTasks);
                break;
            case MEDIUM:
                totalMissions.set(easyTasks * reflectorsCount);
                break;
            case HARD:
                totalMissions.set(easyTasks * reflectorsCount * factorial(rotorsCount));
                break;
            case IMPOSSIBLE:
                totalMissions.set(easyTasks * reflectorsCount
                        * (factorial(totalNumOfRotors) / factorial(totalNumOfRotors - rotorsCount)));
                break;
        }
    }

    public void divideTasksEasy() throws Exception {

        long numOfEncryptions = (long) Math.pow(enigmaMachine.getVocabulary().size(), enigmaMachine.getRotorsCount());
        long numOfTasks = (long) Math.ceil(numOfEncryptions * 1.0 / taskSize);
        int lastTaskSize = (int) numOfEncryptions % taskSize;
        if (lastTaskSize == 0)
            lastTaskSize = taskSize;
        int i;
        for (i = 0; i < numOfTasks - 1; i++) {
            queue.put(new DTOBruteForceQueueItem(taskSize, i * taskSize, enigmaMachine.getMachineConfig(NotchesState.CURRENT_NOTCHES_SETUP), toDecrypt));
            missionsCreated.incrementAndGet();
        }
        // put in queue last task (it has different size, therefore it's not in the loop)
        if (lastTaskSize > 0) {
            queue.put(new DTOBruteForceQueueItem(lastTaskSize, i * taskSize, enigmaMachine.getMachineConfig(NotchesState.CURRENT_NOTCHES_SETUP), toDecrypt));
            missionsCreated.incrementAndGet();
        }
    }

    public void divideTasksMedium() throws Exception {

        int reflectorsCount = enigmaMachine.getReflectorsCount();

        for (int i = 1; i <= reflectorsCount; i++) {
            enigmaMachine.setReflector(i);
            divideTasksEasy();
        }
    }

    private void divideTasksHard() throws Exception {
        int rotorsCount = enigmaMachine.getRotorsCount();

        List<Integer> usedRotors = enigmaMachine.getUsedRotors();
        List<List<Integer>> rotorsPermute = new ArrayList<>();
        permute(usedRotors, 0, rotorsCount - 1, rotorsPermute);

        for (List<Integer> currUsedRotors : rotorsPermute) {
            enigmaMachine.setUsedRotors(currUsedRotors);
            divideTasksMedium();
        }
    }

    private void divideTasksImpossible() throws Exception {
        int rotorsCount = enigmaMachine.getRotorsCount();
        int totalNumOfRotors = enigmaMachine.getAmountOfDefinedRotors();

        List<List<Integer>> resultList = new ArrayList<>();
        findSubGroups(totalNumOfRotors, rotorsCount, resultList);

        for (List<Integer> rotorsList : resultList) {
            enigmaMachine.setUsedRotors(rotorsList);
            divideTasksHard();
        }
    }

    public void findSubGroups(int N, int K, List<List<Integer>> res) {
        List<Integer> combinationList = new ArrayList<>();
        findSubGroupsUtil(N, K, 1, combinationList, res);
    }

    public void findSubGroupsUtil(int N, int K, int startNumber, List<Integer> combinationList, List<List<Integer>> res) {

        if (K == 0) {
            res.add(new ArrayList<>(combinationList));
            return;
        }

        for (int i = startNumber; i <= N; i++) {
            combinationList.add(i);
            findSubGroupsUtil(N, K - 1, i + 1, combinationList, res);
            combinationList.remove(combinationList.size() - 1);
        }
    }

    private int factorial(int max) {
        int res = 1;
        for (int i = max; i > 0; i--) {
            res = res * i;
        }
        return res;
    }

    private void permute(List<Integer> list, int l, int r, List<List<Integer>> res) {
        if (l == r)
            res.add(list);
        else {
            for (int i = l; i <= r; i++) {
                list = swap(list, l, i);
                permute(list, l + 1, r, res);
                list = swap(list, l, i);
            }
        }
    }

    public List<Integer> swap(List<Integer> list, int i, int j) {
        int temp;
        List<Integer> res = new ArrayList<>(list);
        temp = res.get(i);
        res.set(i, list.get(j));
        res.set(j, temp);
        return res;
    }

    public DTOBruteForceTasksBatch getMissionsBatch(int batchSize) throws InterruptedException {

        List<DTOBruteForceQueueItem> allMisions = new ArrayList<>();
        if (isMissionsOver())
            return new DTOBruteForceTasksBatch(allMisions, true, "");

        for (int i = 0; i < batchSize; i++) {
            if (isMissionsOver())
                break;
            allMisions.add(queue.take());
            missionsTakenFromQueue.incrementAndGet();
        }
        return new DTOBruteForceTasksBatch(allMisions, false, toDecrypt);
    }

    private boolean isMissionsOver() {
        if(totalMissions.get() == 0)
            return false;
        else
            return missionsTakenFromQueue.get() == totalMissions.get();
    }

    public long getTotalMissions() {
        return totalMissions.get();
    }

    public long getCreatedMissions(){
        return missionsCreated.get();
    }

    public void interruptMissionDivider(){
        missionDividerThread.interrupt();
    }

}
