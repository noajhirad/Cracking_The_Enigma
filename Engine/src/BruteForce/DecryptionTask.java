package BruteForce;

import EnigmaMachine.*;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import java.util.function.Consumer;

public class DecryptionTask implements Runnable {

    private int taskSize;
    private EnigmaMachine enigmaMachine;
    private List<Character> rotorsPositions;
    private String toDecrypt;
    private ABCCounter counter;
    private int startingPoint;
    private Consumer<BruteForceResult> onCandidateFound;
    private Consumer<FinishedTaskData> onFinish;
    private SimpleBooleanProperty paused;
    private Consumer<DecryptionTask> addToSleepingList;

    public DecryptionTask(int taskSize, EnigmaMachine enigmaMachine, String toDecrypt, int startingPoint,
                          Consumer<BruteForceResult> onCandidateFound,  Consumer<FinishedTaskData> onFinish, SimpleBooleanProperty paused, Consumer<DecryptionTask> addToSleepingList) {
        this.enigmaMachine = enigmaMachine;
        this.taskSize = taskSize;
        this.toDecrypt = toDecrypt;
        this.startingPoint = startingPoint;
        counter = new ABCCounter(enigmaMachine.getVocabulary(), enigmaMachine.getRotorsCount());
        rotorsPositions = counter.getStartingPositionByIndex(startingPoint);
        this.onCandidateFound = onCandidateFound;
        this.onFinish = onFinish;
        this.paused = paused;
        this.addToSleepingList = addToSleepingList;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < taskSize; i++) {
                if(paused.get()) {
                    while (paused.get()) {
                        synchronized (this) {
                            addToSleepingList.accept(this);
//                            System.out.println(Thread.currentThread() + " is going to sleep");
                            this.wait();
                        }
                    }
//                    System.out.println(Thread.currentThread() + " is up!");
                }

                enigmaMachine.setRotorsStartingPointBruteForce(rotorsPositions);
                String output = enigmaMachine.encrypt(toDecrypt);
                String[] outputWords = output.split(" ");

                if (isCandidate(outputWords)) {
                    BruteForceResult res = new BruteForceResult(enigmaMachine.getMachineConfig(NotchesState.ORIGINAL_NOTCHES_SETUP), output, Thread.currentThread().getName());
                    onCandidateFound.accept(res);
                }

                rotorsPositions = counter.increment(rotorsPositions);
            }
            long totalTime = System.currentTimeMillis() - start;

            onFinish.accept(new FinishedTaskData(taskSize, totalTime));
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
}
