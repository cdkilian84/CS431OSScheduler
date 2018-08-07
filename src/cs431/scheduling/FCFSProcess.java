//Christopher Kilian
//CS 431 Programming Project
//Winter 2018

package cs431.scheduling;

import java.util.List;
import java.util.Map;

//The FCFS process is a child class of MyProcess and implements its run method using First-Come-First-Serve logic.
public class FCFSProcess extends MyProcess {

    //constructor
    public FCFSProcess(Map<String, Integer> contents, List<String> jobs){
        super(contents, jobs);//just pass values to super class
    }
    
    //FCFS version of the runProcess method. In this case, the process simply moves through the list of job names in the order
    //they were read in and processes each job in turn until each job is completed. In this case, "processing" is represented by
    //examining the time each process needs, adding that time to the total runtime, and marking the timeLine and finishTime objects
    //with the time at which each job would finish.
    @Override
    public void runProcess() {
        int totalTime = 0;
        for(String name : super.getJobNames()){
            int timeNeeded = super.getJobTimes().get(name);
            totalTime += timeNeeded;
            super.getTimeLine()[totalTime] = name;
            super.getFinishTimes().put(name, totalTime);
        }
    }
    
}
