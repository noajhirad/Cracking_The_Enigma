package AgentBruteForce;

import DTOs.DTOBruteForceQueueItem;
import DTOs.DTOBruteForceResult;
import DTOs.DTOBruteForceTasksBatch;
import DTOs.DTOProgressInfo;
import EnigmaMachineAgent.EnigmaMachine;
import MainControllerAgents.MainController;
import UtilsAgent.Constants;
import UtilsAgent.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import MainAgents.*;

public class ThreadPoolManager {

    private ArrayBlockingQueue<Runnable> queue;
    private ThreadPoolExecutor threadPoolExecutor;
    private EnigmaMachine enigmaMachine;
    private MainController mainController;
    private DTOBruteForceTasksBatch allMissions;
    private AtomicBoolean isMissionFinished;
    private AtomicBoolean isResponseReceived;
    private AgentProgressInfo progressInfo;
    private final Object queueLock = new Object();
    private String agent;
    private String allie;
    private int threadNumber;
    private String toDecrypt = "";
    private SimpleBooleanProperty isBinded = new SimpleBooleanProperty(false);
//    private List<DTOBruteForceResult> allCandidates;
//    private List<DecryptionTask> missionsList;


    public ThreadPoolManager(MainController mainController, int threadNumber, String allie) {
        this.mainController = mainController;
        isMissionFinished = new AtomicBoolean(false);
        isResponseReceived = new AtomicBoolean(true);
        progressInfo = new AgentProgressInfo();
        agent = mainController.getAgentName();
        this.threadNumber = threadNumber;
        this.allie = allie;
//        this.allCandidates = new ArrayList<>();
//        this.missionsList = new ArrayList<>();
        this.allMissions = new DTOBruteForceTasksBatch();
        // go to the server and get enigma machine
        getBruteForceInitializingDetails();
    }

    public void startBruteForce(){

        Consumer<List<DTOBruteForceResult>> onFinish = (data) -> {
            synchronized (progressInfo){
                Platform.runLater(()-> {
                    progressInfo.addToCandidateFound(data.size()); // update number of candidate founds
                    progressInfo.decrementMissionsInProgress();    // update number of missions in progress
                    progressInfo.incrementCompletedMissions();     // update number of finished missions
                    mainController.setCompletedMissionsProp(progressInfo.getCompletedMissions().get());
                });
            }
            for (DTOBruteForceResult res : data){
                Platform.runLater(()->
                        mainController.addToResultsTable(res));
//                synchronized (allCandidates){
//                    if(allCandidates.contains(res)) {
//                        System.out.println("candidate already exists!:");
//                        System.out.println(DTOParser.parseDTOMachineConfigToString(res.getConfig()) + " " + res.getOutput());
//                    }
//                    allCandidates.add(res);
//                }
            }
            if (data.size() > 0)
                sendCandidatesToAllie(data);

            synchronized(queueLock) {
                if (queue.isEmpty()) {
                    queueLock.notify();
                }
            }
        };

        Consumer<Integer> updateProgress = (val) -> {
            synchronized (progressInfo) {
                Platform.runLater(()->{
                progressInfo.setMissionsInQueue(queue.size());
                progressInfo.incrementMissionsInProgress();
                });
            }
        };

        Runnable startBF = () -> {
            queue = new ArrayBlockingQueue<>(1000);
            threadPoolExecutor = new ThreadPoolExecutor(threadNumber, threadNumber, 1, TimeUnit.SECONDS, queue);
            threadPoolExecutor.prestartAllCoreThreads();

            do {
                // get missions from server
                getMissions();

                synchronized (isResponseReceived) {
                    while(!isResponseReceived.get()){
                        try {
                            isResponseReceived.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                try {
                    // convert mission to decryption task
                    synchronized (allMissions) {
                        List<DTOBruteForceQueueItem> allItems = allMissions.getAllTasks();
                        for (DTOBruteForceQueueItem item : allItems) {
                            DecryptionTask newTask = new DecryptionTask(item, enigmaMachine.getDeepCopy(), item.getToDecrypt(), onFinish, updateProgress, agent, allie);

//                            if (missionsList.contains(newTask)) {
//                                int ind = missionsList.indexOf(newTask);
//                                System.out.println("mission already in list! at index : " + + ind + newTask.getStartingPoint());
//                                System.out.println(missionsList);
//                            }
//                            missionsList.add(newTask);

                            queue.put(newTask);
                            synchronized (progressInfo) {
                                Platform.runLater(() ->
                                        progressInfo.setMissionsInQueue(queue.size()));
                            }
                            allMissions = new DTOBruteForceTasksBatch();
                        }
                    }
                } catch (Exception e) {
                    Platform.runLater( ()->
                            mainController.setErrorMessage("Oops... something went wrong"));
                    e.printStackTrace();
                }

                synchronized(queueLock) {
                    while (!queue.isEmpty()) {
                        try {
                            queueLock.wait(); //wait for the queue to become empty
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            while (!isMissionFinished.get());

            // Shutdown thread pool
            System.out.println("Mission is done -> shutting down threadpool");
            threadPoolExecutor.shutdown();
            try {
                threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
                return;
            } catch (InterruptedException e) {
                Platform.runLater( ()->
                        mainController.setErrorMessage("Oops... something went wrong"));
                e.printStackTrace();
            }
        };

        Thread threadPoolManager = new Thread(startBF, "Thread Pool Manager");
        threadPoolManager.start();
    }

    private void sendCandidatesToAllie(List<DTOBruteForceResult> data) {
        String json = Constants.GSON_INSTANCE.toJson(data);

        RequestBody body = RequestBody.create(json, Constants.JSON);

        Request request = new Request.Builder()
                .url(Constants.SEND_CANDIDATES)
                .post(body)
                .build();


        HttpClientUtil.runPostAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        mainController.setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            mainController.setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                }
            }
        });
    }

    private void getMissions() {

        isResponseReceived.set(false);

        String finalUrl = HttpUrl
                .parse(Constants.GET_DECRYPTION_TASKS)
                .newBuilder()
                .addQueryParameter("taskSize", mainController.getTaskSize())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        mainController.setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            mainController.setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    synchronized (allMissions) {
                        allMissions = Constants.GSON_INSTANCE.fromJson(responseBody, new TypeToken<DTOBruteForceTasksBatch>() {
                        }.getType());
                    }
                    synchronized (isResponseReceived){
                        isResponseReceived.set(true);
                        isResponseReceived.notifyAll();
                    }
                    if(allMissions.isMissionOver()){
//                        System.out.println("Server says no more missions...");
                        isMissionFinished.set(true);
                    }
                    else {
                        if (toDecrypt.equals("")) {
                            toDecrypt = allMissions.getToDecrypt();
                        }
                        progressInfo.addToTotalMissions(allMissions.getAllTasks().size());
                        Platform.runLater(() ->
                                mainController.setTotalMissionsProp(progressInfo.getTotalMissions().get()));
                    }
                    if(!isBinded.get()) {
                        Platform.runLater(()-> {
                            mainController.bindLabelsToProgressProperty(progressInfo);});
                        isBinded.set(true);
                    }
                }
            }
        });
    }

    private void getBruteForceInitializingDetails() {

        String finalUrl = HttpUrl
                .parse(Constants.BRUTE_FORCE_INIT_INFO)
                .newBuilder()
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        mainController.setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            mainController.setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    enigmaMachine = Constants.GSON_INSTANCE.fromJson(responseBody, EnigmaMachine.class);
                    startBruteForce();
                }
            }
        });
    }

    public DTOProgressInfo getProgress(){
        return new DTOProgressInfo(agent,Long.toString(progressInfo.getTotalMissions().get())
                ,Integer.toString(progressInfo.getMissionsInQueue().get()), Integer.toString(progressInfo.getCandidateFound().get()),
                toDecrypt, Long.toString(progressInfo.getCompletedMissions().get()));
    }

    public void stopBruteForce() {
        isMissionFinished.set(true);
        threadPoolExecutor.shutdownNow();
    }
}