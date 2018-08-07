//Christopher Kilian
//CS 431 Programming Project
//Winter 2018

package cs431.scheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The RR process is a child class of MyProcess and implements its run method using Round-Robin logic.
//Round-Robin operates on the principle of giving equal sized time slices to each job before moving on to the next.
//If a job doesn't finish during a timeslice, then it must wait until its turn comes around again before using more
//processing time. This subclass implements any sized timeslice as long as its an integer value. When the class is
//instantiated, its timeslice must be passed as an argument to the constructor, and the process will always use that
//timeslice in the future. To process jobs with different Round Robin timeslices, a different process must be instantiated with a
//new timeslice value. Because the logic for Round-Robin is exactly the same no matter that the timeslice is, there's
//no need for more than a single Round-Robin class.
public class RRProcess extends MyProcess {

    private int timeSlice;

    //constructor
    public RRProcess(Map<String, Integer> contents, List<String> jobs, int time) {
        super(contents, jobs); //pass names and time to superclass
        timeSlice = time; //save the passed timeslice
    }

    @Override
    public void runProcess() {
        int totalTime = 0;
        int totalFinished = 0; //track number of finished jobs
        Map<String, Integer> localMappedTimes = new HashMap<>(super.getJobTimes()); //local clone of job times to prevent changes to original data
        while (totalFinished < localMappedTimes.size()) { //run as long as there are jobs still uncompleted
            for (String name : super.getJobNames()) {
                int timeNeeded = localMappedTimes.get(name);
                if (timeNeeded != 0) { //if the job time needed is 0, it's finished and can be skipped
                    if (timeNeeded > timeSlice) { //job won't finish during this timeslice
                        int remaining = timeNeeded - timeSlice; //new time remaining to map to job name
                        localMappedTimes.put(name, remaining);
                        totalTime += timeSlice; //total time is incremented by the timeslice since job will use max allotted time
                    } else {
                        //in this case, timeNeeded is the time the job needs to finish (which is less than or equal to the timeslice)
                        totalTime += timeNeeded;
                        localMappedTimes.put(name, 0); //set time remaining to 0 for this job since it will finish in this timeslice
                        super.getFinishTimes().put(name, totalTime); //job has finished, so mark finishing time
                        totalFinished++;//since this job will finish, increment total finished jobs
                    }
                    super.getTimeLine()[totalTime] = name; //mark job stopping time in timeline whether it finished or not
                }
            }
        }
    }

}
