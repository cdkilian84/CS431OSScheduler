//Christopher Kilian
//CS 431 Programming Project
//Winter 2018

package cs431.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The SJF process is a child class of MyProcess and implements its run method using Shortest-Job-First logic.
//In order to do this, the jobs are first sorted based on their total runtimes, and then are "processed" in the order
//of their sorting. This subclass keeps its own list of sorted job names in order to process the jobs in the correct order.
public class SJFProcess extends MyProcess {

    private List<String> orderedJobNames;

    //constructor
    public SJFProcess(Map<String, Integer> contents, List<String> jobs) {
        super(contents, jobs); //simply pass values to superclass
    }

    //SJF version of the runProcess method. In this case, the job names are first sorted based on the times each of them require.
    //This sorting is handled by a separate method which is called first before any processing occurs.
    //After the job names have been sorted, the processing is handled similarly to FCFS, in that each sorted job name is examined,
    //the time it needs added to the total, and the finish time marked in both the timeLine and finishTimes objects. Each job
    //runs to completion before the next job begins.
    @Override
    public void runProcess() {
        sortJobs();
        int totalTime = 0;
        for (String name : orderedJobNames) {
            int timeNeeded = super.getJobTimes().get(name);
            totalTime += timeNeeded;
            super.getTimeLine()[totalTime] = name;
            super.getFinishTimes().put(name, totalTime);
        }
    }

    
    //Helper method for runProcess, which handles the sorting of job names. Names are sorted because they are stored in a list which is then
    //iterated over, with the times each job requires stored in a separate map. By sorting job names based on their required times, the shortest
    //jobs are at the beginning of the new job name list and the longest are at the end. The list can then be iterated over in order for processing.
    private void sortJobs() {
        orderedJobNames = new ArrayList<>();
        Map<String, Integer> tempToSort = new HashMap<>(super.getJobTimes()); //local clone of job times created to protect original job times object from alterations
        while (!tempToSort.isEmpty()) {
            List<String> keys = new ArrayList<String>(tempToSort.keySet()); //"keys" here are the names of the jobs
            int smallest = tempToSort.get(keys.get(0));//initially set "smallest" to the value of the first mapped job time
            String smallestName = keys.get(0); //value being sorted for (based on mapped times)
            for (String name : keys) {//check remaining values in tempToSort to see which is smallest
                if (tempToSort.get(name) < smallest) {
                    smallest = tempToSort.get(name);
                    smallestName = name;
                }
            }
            orderedJobNames.add(smallestName); //smallestName is the next shortest job, add to next position in list
            tempToSort.remove(smallestName); //remove value from tempToSort once it has been sorted - when tempToSort is empty, all jobs have been sorted
        }
    }
}
