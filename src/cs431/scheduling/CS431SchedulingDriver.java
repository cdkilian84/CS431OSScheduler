//Christopher Kilian
//CS 431 Programming Project
//Winter 2018

package cs431.scheduling;

//This class serves as the driver to demonstrate the OS simulation. One instance of "MyOS" is instantiated, and this object is used
//to read in various job files, create processes to handle them, and display the results.
//Note that while the OS object can read in more than a single data file for processing, for simplicity's sake in terms of output, only one
//job is read in at a time in this driver. To test other job data with this driver, simply replace the filename/pathname at the indicated line below.
//Also note that the process number for each new process is determined by the order it's created. First created is process 0, next is process 1, etc.
public class CS431SchedulingDriver {

    //available types of processes - use these to start processes, and NOTHING ELSE
    private static final String FCFS = "FCFS";  //First-Come-First-Served
    private static final String SJF = "SJF";  //Shortest-Job-First
    private static final String RR2 = "RR-2";  //Round-Robin with timeslice 2
    private static final String RR5 = "RR-5"; //Round-Robin with timeslice 5
    
    public static void main(String[] args) {
        MyOS operatingSystem = new MyOS();
        
        System.out.println("The system will read in each jobs file and then output the results to the command line.");
        System.out.println("The output of each process run will be a gantt chart and table of job times, and the average wait and completion times for each batch of jobs.");
        System.out.println("Note that the gantt charts are two rows, with the top row using | to represent start and stop times.");
        System.out.println("Note that the start/stop time is the number found below the | symbol.");
        System.out.println("Job names are right justified within their time block.");
        System.out.println("");
        System.out.println(""); //spacing
        
        //read in data here:
        operatingSystem.readFile("testdata1.txt"); //this line reads in the jobs file - to read in other job files replace the filepath here
        
        //start processes here:
        try{
            operatingSystem.startNewProcess(FCFS); //process 0
            operatingSystem.startNewProcess(SJF); //process 1
            operatingSystem.startNewProcess(RR2); //process 2
            operatingSystem.startNewProcess(RR5); //process 3
        }catch(Exception x){
            System.out.println("An error occurred starting a process!");
            System.out.println("Error reads: " + x.getMessage());
        }
        
        
        //commence running processes and display output
        System.out.println("RUNNING TESTDATA:");
        System.out.println(""); //spacing
        System.out.println("First-Come-First-Serve Results:");
        operatingSystem.runProcess(0); //run process
        System.out.println(operatingSystem.showProcessResults(0)); //display process results
        System.out.println(""); //spacing
        System.out.println("Shortest-Job-First Results:");
        operatingSystem.runProcess(1); //run process
        System.out.println(operatingSystem.showProcessResults(1)); //display process results
        System.out.println(""); //spacing
        System.out.println("Round-Robin with Timeslice 2 Results:"); 
        operatingSystem.runProcess(2); //run process
        System.out.println(operatingSystem.showProcessResults(2)); //display process results
        System.out.println(""); //spacing
        System.out.println("Round-Robin with Timeslice 5 Results:");
        operatingSystem.runProcess(3); //run process
        System.out.println(operatingSystem.showProcessResults(3)); //display process results
        
    }
    
}
