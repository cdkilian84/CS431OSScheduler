//Christopher Kilian
//CS 431 Programming Project
//Winter 2018

package cs431.scheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Abstract class MyProcess represents all operations common to all processes, and has an abstract method that each subclass must implement
//in order to actually run (since the logic behind running each process is the only difference in these processes).

//Note that the "finishTimes" and "timeLine" member variables are somewhat overlapping in duties - in the case of certain
//process types such as FCFS, the finish times will be denoted by the timeLine since the timeLine tracks the stopping points
//for jobs and jobs only stop in those cases when they are finished. However, for processes like Round-Robin, jobs often stop before
//they are finished. This necessitates the tracking of actual finishing times in addition to stop times.
public abstract class MyProcess {
    private Map<String, Integer> jobTimes;
    private List<String> jobNames;
    private Map<String, Integer> finishTimes; //track the times at which a job actually finishes
    //the timeLine array represents the Gantt chart for a process's execution
    //it is set to the size of the total time required for all jobs, and will hold the names of each job
    //at the time they stopped running (either finished or were swapped out)
    private String[] timeLine;
    private final String NEWLINE = System.lineSeparator(); //system independent line separator - used for output formatting
    
    //constructor
    public MyProcess(Map<String, Integer> contents, List<String> jobs){
        jobNames = jobs;
        jobTimes = new HashMap<>(contents); //ensure that a copy is made of job times so original data isn't changed
        finishTimes = new HashMap<>();
        setTimeLineSize();
    }
    
    
    //this method must be implemented by the subclasses - it's the only method that should differ among process types
    public abstract void runProcess();
    
    
    //public accessor to output a formatted string of the results from running the process.
    //method checks finishTimes map to see if the process has been run yet - if it is empty, the process has not been run
    //so no output can be presented yet.
    public String outputResults(){
        StringBuilder finalResults = new StringBuilder();
        
        if(finishTimes.isEmpty()){ //only run output methods if this process has been run already (finishTimes won't be empty)
            finalResults.append("This process has not been run yet. No output available. Run the process first.");
        }else{
            //Show gantt chart first:
            finalResults.append("Gantt chart:" + NEWLINE + outputGantt() + NEWLINE + NEWLINE + NEWLINE);
            //next show table:
            finalResults.append("Start/Stop/Finish Time Table:" + NEWLINE + outputTable() + NEWLINE);
            //last show averages:
            finalResults.append("Averages:" + NEWLINE + outputAverages());
        }
        
        return finalResults.toString();
    }
    
    
    //private method to generate the gantt chart and return the string containing it
    //uses String.format to produce a formatted gantt chart
    private String outputGantt(){
        StringBuilder outputLine1 = new StringBuilder();
        StringBuilder outputLine2 = new StringBuilder();
        outputLine1.append("|");
        //nothing finishes at time = 0, so skip that time in the loop
        outputLine2.append(String.format("%-7d", 0));
        for(int i = 1; i < timeLine.length; i++){
            String output = "";
            if(timeLine[i] != null){
                output = String.format("%-6s|", timeLine[i]);
            }else{
                output = String.format("%7s", "");
            }
            outputLine1.append(output);
            outputLine2.append(String.format("%-7d", i));
        }
        
        String gantt = outputLine1.toString() + NEWLINE + outputLine2.toString();
        return gantt;
    }
    
    
    //private method to generate the averages of finish and wait times for all jobs and return the formatted values as a string
    private String outputAverages(){
        double totalWaitTime = 0;
        double totalCompletionTime = 0;
        for(String name : jobNames){
            totalWaitTime += (finishTimes.get(name) - jobTimes.get(name)); //wait time for a job is the time it finished - time it needed to run
            totalCompletionTime += finishTimes.get(name); //completion time for the job is just the time it finished
        }
        double avgWaitTime = totalWaitTime/jobNames.size(); //average is total divided by number of jobs
        double avgCompletionTime = totalCompletionTime/jobNames.size();
        
        String waitTime = String.format("Average Wait Time: %.2f", avgWaitTime);
        String completionTime = String.format("Average Completion Time: %.2f", avgCompletionTime);
        
        return waitTime + NEWLINE + completionTime;
    }
    
    
    //private method to generate a table which shows job start/stop times and completion times
    //returns the table as a formatted string
    private String outputTable(){
        StringBuilder theTable = new StringBuilder();
        int lastFinishTime = 0;
        
        theTable.append("Job\t\tStart\t\tEnd\t\tOutput" + NEWLINE);
        
        for(int i = 0; i < timeLine.length; i++){
            if(timeLine[i] != null){ //job stopping time found
                StringBuilder row = new StringBuilder();
                //note that "lastFinishTime" hasn't been updated yet and so contains the last end time for a job which is the same as the start time for the current job
                row.append(timeLine[i] + "\t\t" + lastFinishTime + "\t\t" + i + "\t\t"); //also note that "i" is the current end time
                lastFinishTime = i;
                if(i == finishTimes.get(timeLine[i])){ //if the job is done
                    row.append(timeLine[i] + " finished at time " + i + NEWLINE);
                }else{
                    row.append(NEWLINE);
                }
                theTable.append(row);
            }
        }
        
        return theTable.toString();
    }
    
    //sets the size of the Gantt chart represented by an array named timeLine - number of elements in the array represents the total
    //amount of time required for all jobs
    private void setTimeLineSize(){
        int totalTime = 0;
        for(String name : jobNames){
            totalTime += jobTimes.get(name);
        }
        //always set timeLine size 1 larger than actual job times - last job will always finish at last time
        //and with 0 indexed array this would go out of bounds
        timeLine = new String[totalTime+1];
    }
    
    //public getters
    public Map<String, Integer> getJobTimes(){
        return jobTimes;
    }
    
    public List<String> getJobNames(){
        return jobNames;
    }
    
    public String[] getTimeLine(){
        return timeLine;
    }
    
    public Map<String, Integer> getFinishTimes(){
        return finishTimes;
    }
}
