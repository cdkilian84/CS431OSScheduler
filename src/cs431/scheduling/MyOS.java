//Christopher Kilian
//CS 431 Programming Project
//Winter 2018

package cs431.scheduling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class acts as the top level interaction for the program. The Operating System is responsible for reading in a batch of jobs from the
//provided job files, and then passing that job data on to a specified process as required. When a process is started, the job data is passed to that
//process for handling, and the process is tracked and monitored by the OS through the use of a list of instantiated processes.
//If another job file is read-in, the job data stored by the OS object is overwritten with new data, however all data passed to individual processes
//remains intact. Job data stored by the OS is meant only to be passed on to processes hence its transient nature.

//File Input Note: This class reads in job data files that use the format:
//jobname
//jobtime
//jobname
//jobtime
//etc...
//Where "jobname" is a string and "jobtime" is an integer value
public class MyOS {

    private Map<String, Integer> fileContents;
    private List<String> jobNames;
    private List<MyProcess> myProcesses;

    //constructor
    public MyOS() {
        myProcesses = new ArrayList<>();
    }
    
    //Read in a new batch of job files.
    //Note that using this method will overwrite the currently stored system file, but previously instantiated
    //processes should not be effected.
    //To use this method, pass in the string representing the location of the file to be read-in
    public void readFile(String fileName) {
        fileContents = new HashMap<>();
        jobNames = new ArrayList<>();
        int count = 0;//when count % 2 == 0, the line is a job name line, and when it's 1, the line is a time remaining line
        String jobName = "";
        int jobTime = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if ((count % 2) == 0) { //the name of the job
                    jobName = line;
                    jobNames.add(jobName);
                } else { //the time for the job
                    jobTime = Integer.parseInt(line);
                    fileContents.put(jobName, jobTime);
                }
                count++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
    

    //Method to start a new process based on the identifying string passed to the method.
    //If the identifying string isn't recognized or a jobfile hasn't been read in yet, an exception is thrown.
    //If the identifying string matches "FCFS", "SJF", "RR-2", or "RR-5" an appropriate process is
    //created and added to the process list.
    public void startNewProcess(String processName) {
        MyProcess theProcess = null;
        //only try to start a process if a file has been read already
        if(fileContents != null && jobNames != null){
            switch (processName) {
                case "FCFS":
                    theProcess = new FCFSProcess(fileContents, jobNames);
                    break;
                case "SJF":
                    theProcess = new SJFProcess(fileContents, jobNames);
                    break;
                case "RR-2":
                    theProcess = new RRProcess(fileContents, jobNames, 2); //round robin process passed timeslice 2
                    break;
                case "RR-5":
                    theProcess = new RRProcess(fileContents, jobNames, 5); //round robin process passed timeslice 5
                    break;
                default:
                    throw new RuntimeException("No such process as " + processName +" exists! Cannot start the process!");//throw an error if an incorrect type of process is specified
            }
            myProcesses.add(theProcess);
        }else{
            throw new RuntimeException("No job file has been read yet! Read a job file first!");//throw an error if the job hasn't been read yet
        }
    }

    //public accessor to run processes - note that if an invalid process number is passed, nothing happens
    //This method returns a boolean to indicate if the process ran or not - true if it did, false otherwise
    public boolean runProcess(int procNum){
        boolean didItRun = false;
        if((myProcesses.size() > procNum) && (procNum >= 0)){ //procNum is valid if it's 0 or greater, and less than the size of the MyProcesses list
            myProcesses.get(procNum).runProcess();
            didItRun = true;
        }
        return didItRun;
    }
    
    //public accessor to show the results of a process which has run
    public String showProcessResults(int procNum){
        String output = ""; //return value
        if((myProcesses.size() > procNum) && (procNum >= 0)){ //procNum is valid if it's 0 or greater, and less than the size of the MyProcesses list
            output = myProcesses.get(procNum).outputResults();
        }
        else if(myProcesses.isEmpty()){ //warn of empty process list
            output = "No processes exist yet in the system! Create and run a process first!";
        }
        else if(procNum < 0){ //warn of negative process number values
            output = "Process numbers cannot be negative - provide a value from 0 to " + (myProcesses.size() - 1);
        }
        else if(procNum >= myProcesses.size()){ //warn of invalid process number and provide list of appropriate numbers
            output = "No process " + procNum + " exists. Choose a process from 0 to " + (myProcesses.size() - 1);
        }
        
        return output;
    }
    
}
