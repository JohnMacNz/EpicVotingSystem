package epicVotingSystem;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
* This class manages file read/writes, voting calculations and data updates by the voting system
* @author  Tony Assadi, John R. McLaren
* @version 2.0
* @since   01/09/2016
*/

public class VotingController
{
    private ArrayList<Staff> staffList = new ArrayList<Staff>();
    private ArrayList<Candidate> candidateList = new ArrayList<Candidate>();
    private ArrayList<Admin> adminList = new ArrayList<Admin>();

    private Staff theStaff;
    private Candidate theCandidate;
    private Admin theAdmin;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/y HH:mm:ss");
    private Calendar cal = Calendar.getInstance();
    
    /**
    * Class constructor calls two functions, <b>loadStaffData()</b> and <b>loadCandidateData()</b> 
    * to load staff and candidate data from file.
    */
    public VotingController()
    {
        loadStaffData();
        loadCandidateData();
        loadAdminData();
    }
    
    /**
     * This method loads candidates data from a comma-delimited text file <b>("candidates.txt")</b>, located in the root directory of this project.
     * <br>Each candidate from file is loaded as an object "theCandidate" and then added to "candidates" Arraylist.     
     */
    public void loadCandidateData()
    {
        try
        {
             String fileName = "candidates.txt";
             File theFile = new File(fileName);
             BufferedReader reader = new BufferedReader(new FileReader(theFile));

             String candidateData;

             while((candidateData = reader.readLine())!= null)
             {
                 String[] candidateDetails = candidateData.split(",");
                 int code = Integer.parseInt(candidateDetails[0]);
                 int votes = Integer.parseInt(candidateDetails[2]);
                 theCandidate = new Candidate(code, candidateDetails[1], votes);
                 candidateList.add(theCandidate);
             }
             reader.close();
         }
         catch(Exception e)
         {
             System.out.println("Error! There was a problem with loading CANDIDATE names from file");
         }
    }

    /**
     * This method loads staff data from a comma-delimited text file <b>("staff.txt")</b>, located in the root directory of this project.
     * <br>Each staff from file is loaded as an object "theStaff" and then added to "staffs" Arraylist. 
     * * <br> Since this a comma-delimited txt file, Changes to structure of how data is loaded from file must reflect "Staff class" and
     * <br> saveStaffData() function that save staff data back to file.    
     */
    public void loadStaffData()
    {
         try
         {
             String fileName = "staff.txt";
             File theFile = new File(fileName);

             BufferedReader reader = new BufferedReader(new FileReader(theFile));

             String staffData;
             String[] staffDetails;

             while((staffData = reader.readLine()) != null)
             {
                 staffDetails = staffData.split(",");
                 int id = Integer.parseInt(staffDetails[0]);
                 String name = staffDetails[1];
                 String password = staffDetails[2];
                 int voted = Integer.parseInt(staffDetails[3]);
                 String timeVoted = staffDetails[4].toString();
                 theStaff = new Staff(id, name, password, voted, timeVoted);
                 staffList.add(theStaff);
             }
             reader.close();
         }
         catch(Exception e)
         {
             System.out.println("Error! There was a problem with loading STAFF names from file");
         }
    }
    
    public void loadAdminData()
    {
        try
        {
             String fileName = "admin.txt";
             File theFile = new File(fileName);
             BufferedReader reader = new BufferedReader(new FileReader(theFile));

             String adminData;

             while((adminData = reader.readLine())!= null)
             {
                 String[] adminDetails = adminData.split(",");
                 int id = Integer.parseInt(adminDetails[0]);
                 String name = adminDetails[1];
                 String username = adminDetails[2];
                 String password = adminDetails[3];
                 theAdmin = new Admin(id, name, username, password);                 
                 adminList.add(theAdmin);
             }
             reader.close();
         }
         catch(Exception e)
         {
             System.out.println("Error! There was a problem with loading ADMIN data from file");
         }
    }
    
    public void addNewAdmin(String name, String username, String password)
    {
    	for(int i = 0; i < adminList.size() + 1; i++){ // use a for loop to get next unused ID number
            if(i == adminList.size()) // when we have reached the end of the list
            {
	           	theAdmin = new Admin(100 + i, name, username, password); // create new admin
	 	    	adminList.add(theAdmin); // add to arraylist
	 	    	saveAdminData(); // save to file
	 	    	System.out.println("Admin Successfully Added.");
	 	    	break;
            }
         }
    }
    
    public void addNewStaff(String name, String password)
    {
    	for(int i = 0; i < staffList.size() + 1; i++){ // use a for loop to get next unused ID number
            if(i == staffList.size()) // when we have reached the end of the list
            {
	           	theStaff = new Staff(100 + i, name, password, 0, "0:0:0"); // create new staff
	 	    	staffList.add(theStaff); // add to arraylist
	 	    	saveStaffData(); // save to file
	 	    	System.out.println("Staff Successfully Added.");
	 	    	break;
            }
         }
    }

    /**
     * This method returns a staff object if found in the staffs ArrayList
     * @param id staff id to search
     * @return theStaff - staff object that matched search id
     */
    public Staff getStaff(int id)
    {
        Iterator<Staff> it = staffList.iterator();
        while(it.hasNext())
        {
            theStaff = (Staff) it.next();
            if(theStaff.getID() == id)
            {
                return theStaff;
            }
        }
        return null;
    }

    /**
     * This method returns a Candidate object if found in the candidates ArrayList
     * @param candidateCode Candidate code to find
     * @return theCandidate - Candidate object that matched search code
     */
    public Candidate getCandidate(int candidateCode)
    {
        Iterator<Candidate> it = candidateList.iterator();
        while(it.hasNext())
		{
            theCandidate = (Candidate) it.next();
            if(theCandidate.getCandidateCode() == candidateCode){
                return theCandidate;
            }
        }
        return null;
    }
    
    public Admin getAdmin(int id){
        Iterator<Admin> it = adminList.iterator();
        while(it.hasNext())
        {
            theAdmin = (Admin) it.next();
            if(theAdmin.getID() == id)
            {
                return theAdmin;
            }
        }
        return null;
    }
    
    public void removeAdmin(int id){
    	Iterator<Admin> it = adminList.iterator();
        while(it.hasNext())
        {
            theAdmin = (Admin) it.next();
            if(theAdmin.getID() == id)
            {
                adminList.remove(theAdmin);
                System.out.println("Admin Account Successfully Deleted.");
                break;
            }
        }
    }

    public void removeStaff(int id){
    	Iterator<Staff> it = staffList.iterator();
        while(it.hasNext())
        {
            theStaff = (Staff) it.next();
            if(theStaff.getID() == id)
            {
                staffList.remove(theStaff);
                System.out.println("Staff Account Successfully Deleted.");
                break;
            }
        }
    }
    
    public ArrayList<Staff> getStaffList()
	{
		return staffList;
	}

	/**
     * This method is used to return collection of candidates as an Arraylist
     * @return ArrayList - list of candidates as an Arraylist
     */
    public ArrayList<Candidate> getCandidateList()
    {
        return candidateList;
    }
    
    public ArrayList<Admin> getAdminList()
    {
    	return adminList;
    }
    
    /**
     *  This methods is used to retrieve total number of staffs in the collection
     * @return int - total number of staff to vote
     */
    public int getTotalVoters()
    {
        return staffList.size();
    }
    
    /**
     * This method writes staffs data back to file. 
     * <br><b>Note :</b> This method writes ALL data from staffs Arraylist back to file and data in the file is completely overwritten. 
     * <br> Since this a comma-delimited txt file, changes to structure of data written to file in this method MUST reflect structure of staff class and
     * <br> loadStaffData() function that load staff data from file.      
     */
    public void saveStaffData()
    {
        try
        {
        	BufferedWriter writer = new  BufferedWriter(new FileWriter("staff.txt"));
        	Iterator<Staff> it = staffList.iterator();
            String staffDetails;

            while(it.hasNext())
            {
                theStaff = (Staff) it.next();
                if(theStaff.hasVoted() == 1){
                	staffDetails = theStaff.getID() + "," 
                	+ theStaff.getName() + "," 
                	+ theStaff.getPassword() + "," 
                	+ theStaff.hasVoted() + "," 
                	+ theStaff.getTimeVoted() +"\n";
                	writer.write(staffDetails);
                }
                else{
                	staffDetails = theStaff.getID() + "," 
                	+ theStaff.getName() + "," 
                	+ theStaff.getPassword() + "," 
                	+ theStaff.hasVoted() + "," + "0:0:0" + "\n";
                	writer.write(staffDetails);
                }
                
                
            }
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    /**
     * This method writes candidates data back to file. 
     * <br><b>Note :</b> This method writes ALL data from candidates Arraylist back to file and data in the file is completely overwritten. 
     * <br> Since this a comma-delimited txt file, changes to structure of data written to file in this method MUST reflect structure of Candidate class and
     * <br> loadCandidateData() function that load candidate data from file.      
     */
    public void saveCandidateData()
    {
        try
        {
            BufferedWriter writer = new  BufferedWriter(new FileWriter("candidates.txt"));
            Iterator<Candidate> it = candidateList.iterator();
            String candidateDetails;
            while(it.hasNext())
			{
                theCandidate = (Candidate) it.next();
                candidateDetails = theCandidate.getCandidateCode() + "," 
                + theCandidate.getName() + "," 
                + theCandidate.getVotes() +"\n";
                writer.write(candidateDetails);
            }
            writer.close();
        }
        catch(IOException e)
		{
            System.out.println(e);
        }
    }
    
    public void saveAdminData(){
    	try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("admin.txt"));
            Iterator<Admin> it = adminList.iterator();
            String adminDetails;
            while(it.hasNext())
			{
                theAdmin = it.next();
                adminDetails = theAdmin.getID() + "," 
                + theAdmin.getName() + "," 
                + theAdmin.getUsername() + "," 
                + theAdmin.getPassword() +"\n";
                writer.write(adminDetails);
            }
            writer.close();
        }
        catch(IOException e)
		{
            System.out.println(e);
        }
    }

	/**
	 * This method is used to save every staff vote to file and update number of votes received by each Candidate.
	 * <br> Once staff votes is updated for staff and candidate objects, This methods call two functions 
	 * <br> "saveStaffData()"  and "saveCandidateData()" to write updated data for staff and candidates objects into file.
	 */
	public void recordVote()
	{
	    theStaff.setVoted();
	    theStaff.setTimeVoted(dateFormat.format(cal.getTime()));
	    theCandidate.addVote();
	    saveStaffData();//save to file
	    saveCandidateData();//save to file
	}
}
