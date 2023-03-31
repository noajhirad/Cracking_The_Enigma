package AgentBruteForce;

import DTOs.DTOBruteForceQueueItem;
import DTOs.DTOBruteForceResult;
import EnigmaMachineAgent.EnigmaMachine;
import EnigmaMachineAgent.NotchesState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class DecryptionTask implements Runnable {

    private int taskSize;
    private EnigmaMachine enigmaMachine;
    private List<Character> rotorsPositions;
    private String toDecrypt;
    private ABCCounter counter;
    private int startingPoint;
    private Consumer<List<DTOBruteForceResult>> onFinish;
    private Consumer<Integer> updateProgress;
    private List<DTOBruteForceResult> allCandidates = new ArrayList<>();
    private String agent;
    private String allie;

    public DecryptionTask(DTOBruteForceQueueItem dto, EnigmaMachine enigmaMachine, String toDecrypt,
                          Consumer<List<DTOBruteForceResult>> onFinish, Consumer<Integer> updateProgress, String agent, String allie) {
        this.enigmaMachine = enigmaMachine;
        this.taskSize = dto.getTaskSize();
        this.toDecrypt = toDecrypt;
        this.startingPoint = dto.getStartingPoint();
        counter = new ABCCounter(enigmaMachine.getVocabulary(), enigmaMachine.getRotorsCount());
        rotorsPositions = counter.getStartingPositionByIndex(startingPoint);
        this.onFinish = onFinish;
        this.updateProgress = updateProgress;
        this.agent = agent;
        this.allie = allie;
    }

    @Override
    public void run() {
        try {
            updateProgress.accept(1);

            for (int i = 0; i < taskSize; i++) {
//                System.out.println("rotors position:" + rotorsPositions);

                enigmaMachine.setRotorsStartingPointBruteForce(rotorsPositions);
                String output = enigmaMachine.encrypt(toDecrypt);
                String[] outputWords = output.split(" ");

                if (isCandidate(outputWords)) {
                    DTOBruteForceResult res = new DTOBruteForceResult(enigmaMachine.getMachineConfig(NotchesState.ORIGINAL_NOTCHES_SETUP), output, agent, allie);
                    allCandidates.add(res);
                }

                rotorsPositions = counter.increment(rotorsPositions);
            }

            onFinish.accept(allCandidates);
        }
        catch (Exception e) {}
    }

    private boolean isCandidate(String[] words) {
        for(String word : words) {
            if(!enigmaMachine.isInDictionary(word))
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecryptionTask that = (DecryptionTask) o;
        return startingPoint == that.startingPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingPoint);
    }

    public int getStartingPoint() {
        return startingPoint;
    }
}
