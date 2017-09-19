package epicVotingSystem;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
* This class manages user Interface of the voting system by utilizing object of staff, candidates, admin and VotingController classes.
* @author  Tony Assadi, John R. McLaren
* @version 2.0
* @since   01/09/2016
*/

public final class VotingInterface
{
    private BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    
    private VotingController vc;
    private Staff theStaff;
    private Candidate theCandidate;
    private Admin theAdmin;
    
    private int numberOfCandidates = 0;
    private Date startDate, endDate;
    private boolean isDateRange = false; //is there a voting range?
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/y");
    private Calendar cal = Calendar.getInstance();
    
	/**
	 * Application static void main. Starting point for voting system.
	 * <br> This method creates an object of VotingInterface class and calls the start method.
	 * */
    public static void main(String[] args)
    {
        VotingInterface vi = new VotingInterface();
        vi.start();
    }

    /**
	 * Setter method for private filed number Of Candidates
	 * @return int - number Of Candidates
	 */
	private int getNumberOfCandidates() 
	{
		return numberOfCandidates;
	}

	/**
	 * Geter method for private filed number Of Candidates
	 * @param numberOfCandidates - number Of Candidates stored in private field
	 */
	private void setNumberOfCandidates(int numberOfCandidates) 
	{
		this.numberOfCandidates = numberOfCandidates;
	}

	private void start()
	{
	    vc = new VotingController();
	    commenceVoting();
	}

	private void commenceVoting()
	{
	    boolean systemQuit = false;
	    while (!systemQuit)
	    {
	        String input = null;
	        System.out.println("\t\t===================== eVoting System =====================\n\n");
	        System.out.print("Enter \"v\" to Vote as staff \nOR \"a\" to login in as system administrator \nOR \"h\" for further help: ");
	        input = getInput();
	
	        if (input.equalsIgnoreCase("V"))
	        {
	            checkVotingRange();
	        }
	        else if (input.equalsIgnoreCase("A"))
	        {
	            systemQuit = loginAdmin();
	        }
	        else if (input.equalsIgnoreCase("H"))
	        {
	        	showHelp();
	        }
	        else
	        {
	            System.out.println("Your input was not recognised");
	        }
	    }
	}

	//manages staff vote
	private void getStaffVote()
	{
	   int candidateCode;
	   boolean retry = true;
	   
	   while (retry)
	   {
		   displayVotingScreen();
		   
	       System.out.print("\n\nEnter your candidate's code OR enter Q to quit voting : ");
	       try{
	           String input = getInput();
	
	           if (input.equalsIgnoreCase("Q"))
	           {
	               retry = false;
	           }
	           else
	           {
	               candidateCode = Integer.parseInt(input);
	               theCandidate = vc.getCandidate(candidateCode);
	               System.out.print("\nYou have selected " + theCandidate.getName()+ ". \n\nEnter Y to confirm or any other key to Cancel, then press ENTER : ");
	
	               if (getInput().equalsIgnoreCase("y"))
	               {
	                   vc.recordVote();
	                   System.out.println("\n\nThanks for voting " + theStaff.getName() + ". Bye!!!");
	                   retry = false;
	               }
	           }
	       }
	       catch(NumberFormatException e){
	           System.out.println("That was not a number you entered\nPlease try again");
	       }
	       catch(NullPointerException e){
	           System.out.println("This candidate code does not exist\nPlease try again");
	       }
	       catch(Exception e){
	           System.out.println("We have a problem, please contact your administrator");
	       }
	   }
	}

	//screen input reader
	private String getInput()
	{
		String theInput = "";
		try
		{
		 theInput = in.readLine().trim();
		}
		catch(IOException e)
		{
		 System.out.println(e);
		}
		return theInput;
	}

	private void checkVotingRange()
	{
		if(isDateRange)
		{ // if voting range is set
			try {
				if(isWithinRange(dateFormat.parse(dateFormat.format(cal.getTime())))){ // check current date is within range
					manageVote();
				}
				else
				{
					System.out.printf("Unable to vote. "
							+ "Voting times are available between %s and %s",startDate.toString(),endDate.toString());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else
		{
			manageVote();
		}
	}

	private void showHelp()
    {
    	System.out.println("Help");
    }

    private void manageVote()
    {
    	boolean moveOn = false;
        String password = "";
        int tryCount = 0;

        while (moveOn == false)
        {
            System.out.print("Please enter your staff ID :");
            try
            {
                theStaff = vc.getStaff(Integer.parseInt(getInput()));

                if(theStaff.hasVoted() == 1)
                {
                    System.out.println("\nYou have voted and cannot vote again\nGood bye...!");
                    moveOn = true;
                }
                else if (theStaff.hasVoted() == 0)
                {
                	System.out.print("Please enter your password:");
                	try{
                		password = getInput().trim();
                		
                		if(theStaff.getPassword().equals(password)){
                			getStaffVote();
                            moveOn = true;
                		}
                		else{
                			System.out.print("Incorrect password.\nPress ENTER to try again or \"q\" to QUIT :  ");
                            if ("q".equalsIgnoreCase(getInput()))
                            {
                                System.out.println("Good bye!");
                                moveOn = true;
                            }
                		}
                	}
                	catch(Exception e){
                		System.out.print("Unknown error occurred. Contact your administrator.");
                	}                   
                }
                else
                {
                    System.out.println("There seems to be a problem. Contact your administrator");
                }
            }
            catch(NumberFormatException e)
            {
            	tryCount++;
				if(tryCount == 3){
					System.out.println("Maximum amount of login attempts reached.");
					moveOn = true;
				}
				else
                 System.out.println("Invalid entry - you must enter a number\nPlease try again");
            }
			catch(NullPointerException e)
			{
				tryCount++;
				if(tryCount == 3){
					System.out.println("Maximum amount of login attempts reached.");
					moveOn = true;
				}
				else
				{
					System.out.println("Error! Staff ID not found.\nPress ENTER to try again or \"q\" to QUIT :  ");
					if ("q".equalsIgnoreCase(getInput()))
					{
						System.out.println("Good bye!");
						moveOn = true;
					}
				}
			}
         }
         System.out.println("going back to voting screen...");
     }

     private void displayVotingScreen()
     {
        System.out.println("\nWelcome "+ theStaff.getName()+"!\n");
        setNumberOfCandidates(0);

        ArrayList<?> candidates = vc.getCandidateList();

        Iterator<?> it = candidates.iterator();
        System.out.println("\tCode\tCandidate Name");
        System.out.println("\t====\t==============\n");
        
        while(it.hasNext())
        {   theCandidate = (Candidate)it.next();
            System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName());
            setNumberOfCandidates(getNumberOfCandidates() + 1);
        }
     }

     private void displayStaff()
	 {
		ArrayList<?> staff = vc.getStaffList();
		Iterator<?> it = staff.iterator();
	
		while(it.hasNext())
		{
			theStaff = (Staff) it.next();
		    System.out.printf("ID: %d, Name: %s\n",theStaff.getID(),theStaff.getName());
		}
	 }

	//loop through admin array list and display the current admins to console	
	 private void displayAdmins()
	 {
		ArrayList<?> admins = vc.getAdminList();
		Iterator<?> it = admins.iterator();
		int counter = 0;
		while(it.hasNext())
		{
			counter++;
			theAdmin = (Admin) it.next();
		    System.out.printf("%d: ID: %d, Username: %s\n",counter,theAdmin.getID(),theAdmin.getUsername());
		}
	 }

	//prints out the voting results
	 private void displayVoteResults()
	 {
	    ArrayList<?> candidates = vc.getCandidateList();
	    int totalVoters = vc.getTotalVoters();
	    double totalVoted = 0;
	    int candidateVotes = 0;
		
		//formatting display
		DecimalFormat df = new DecimalFormat("###.##");
		
		Iterator<?> it = candidates.iterator();
		System.out.println("\n\t   VOTING STATISTICS");
		System.out.println("\t=========================\n");
		System.out.println("Code\tName\t\tVotes\t(Vote%)");
		System.out.println("____\t____\t\t_____\t______\n");
		
		
		//calculate total voted
		while(it.hasNext())
		{
		     theCandidate = (Candidate) it.next();
		     totalVoted += theCandidate.getVotes();// count total votes for this candidate
		}
		
		it = candidates.iterator();
		while(it.hasNext()) 
		{
		     theCandidate = (Candidate) it.next();
		     candidateVotes = theCandidate.getVotes();
		     System.out.println(theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t"
		     + candidateVotes +"\t(" + df.format((candidateVotes/totalVoted)*100) +"%)");
		}
		System.out.println("\nNumbers on voting list: " + totalVoters);
		System.out.println("Numbers voted: " + totalVoted + "(" + df.format((totalVoted/totalVoters)*100)  + "%)");
		System.out.println();
	}
	
	private void displayVotingActivity(){
		// gets list of staff and displays their voting status
		ArrayList<?> staff = vc.getStaffList();
		Iterator<?> it = staff.iterator();
		
		System.out.println("\n\t   STATUS REPORT");
		System.out.println("\t=========================\n");
	
		while(it.hasNext())
		{
			theStaff = (Staff) it.next();
			String vote = "";
			if(theStaff.hasVoted() == 1){
				vote = "Yes";
				System.out.printf("%s, Voted: %s, Time: %s\n",theStaff.getName(), vote, theStaff.getTimeVoted());
			}
			else{
				vote = "No";
				System.out.printf("%s, Voted: %s\n",theStaff.getName(), vote);
			}
		}
		System.out.printf("\nTotal No. of Voters: %d\n\n", vc.getStaffList().size());
	}

	private void displayDateRange()
	{
		if(isDateRange){
			System.out.println("Start Date: " + startDate.toString());
			System.out.println("EndDate: " + endDate.toString());
		}
		else{
			System.out.println("No Date Range Set");
		}

	}
	
	private boolean loginAdmin()
	{
	    boolean adminQuit = false;
	    System.out.println("\n=====Admin Login=====\n");
	    while (!adminQuit){
	    	System.out.print("Enter username or \"Q\" to quit : " );
	        try{
	        	String input = getInput();
	            if (input.equalsIgnoreCase("q")){ 
	                adminQuit = true;
	            }
	            else
	            {
	                String username, password = null;
	                username = input.trim();
	                System.out.print("\nEnter password : ");
	                password = getInput().trim();
	
	                if(validateAdmin(username, password)){
	                	System.out.println("Login Successful.");
	                	adminQuit = manageAdmin();
	                	return adminQuit;
	                }
	                else
	                {
	                    System.out.println("Incorrect username/password.");
	                }
	            }
	        }
	        catch(NullPointerException e){
	        	System.out.println(e);
	        }
	    }
	    return false;
	 }

	// handles admin operations
	 private boolean manageAdmin()
	 {
		boolean back = false;
	    boolean adminQuit = false;
	    boolean systemQuit = false;
	    try{
	    	System.out.println("\n==============Admin Interface==============\n");
			System.out.println("Welcome " + theAdmin.getName() +".\n");
	    	while (!back)
	    	{
	    		System.out.printf("Choose an option: \n1) Display Current Voting Results\n2) View Voting Report\n"
	    				+ "3) Modify Staff\n4) Modify Voting Times\n5) Modify Admins\n6) Exit\n");
	    		int option = Integer.parseInt(getInput());
	    		switch(option){
	            	case 1: displayVoteResults();
	        			break;
	        		case 2: displayVotingActivity();
	        			break;
	        		case 3: modifyStaffAccounts();
	        			break;
	        		case 4: modifyVotingRange();
	        			break;
	        		case 5: modifyAdminAccounts();
	        			break;
	        		case 6: back = true;
	        			break;
	    		}
	    		if(!back)
	    		{
	            	System.out.println("Press any key to continue");
	            	getInput();
	    		}
	    	} // end of loop
	    	while(!adminQuit){
	    		System.out.println("\nTo return to the main menu enter \"B\" \nTo end voting enter \"Stop\" : ");
		        String input = getInput();
		        if (input.equalsIgnoreCase("B"))
		        {
		            adminQuit = true;
		        }
		        else if(input.equalsIgnoreCase("Stop"))
		        {
		            //stop system
		            adminQuit = true;
		            systemQuit = true;
		            System.out.println("Voting System Closed. Goodbye.");
		        }
		        else
		        {
		            System.out.println("Cannot understand your input, please re-enter : \n\n");
		        }
	    	}
	        
	    }
	    catch(NumberFormatException e){
	        System.out.println("That was not a number you entered\nPlease try again");
	    }
	    return systemQuit;
	 }

	private void createDateRange()
	{
		 if(!isDateRange){
			try{
				System.out.println("Enter start date (DD/MM/YY): ");
				startDate = dateFormat.parse(getInput());
				System.out.println("Enter end date (DD/MM/YY): ");
				endDate = dateFormat.parse(getInput());
				isDateRange = true;
			}
			catch(NullPointerException e){
			    System.out.println("Invalid Input. Please try again");
			}
			catch(Exception e){
			    System.out.println(e);
			}
		 }
		 else{
			 System.out.println("Date range already in place. Select update date range instead.");
		 }
	 }
	 
	 private void createAdmin()
     {
    	 try
    	 {
        	 System.out.print("Enter new name: ");
        	 String name = getInput();
        	 System.out.print("Enter new username: ");
        	 String username = getInput();
        	 System.out.print("Enter new password: ");
        	 String password = getInput();
        	 vc.addNewAdmin(name, username, password);
    	 }
    	 catch(NullPointerException e){
    		 System.out.println("Cannot have a null value entered");
    	 }
     }
     
     private void createStaff()
     {
    	 try
    	 {
        	System.out.print("Enter new staff name: ");
        	String name = getInput();
        	System.out.print("Enter new staff password: ");
        	String password = getInput();
        	vc.addNewStaff(name, password);
    	 }
    	 catch(NullPointerException e){
    		System.out.println("Cannot have a null value entered");
    	 }
     }
     private void deleteAdmin()
	 {
		 try
		 {
			 System.out.print("Enter ID: ");
	    	 int id = Integer.parseInt(getInput());
	    	 String name = vc.getAdmin(id).getName();
	    	 
	    	 System.out.printf("Warning! You are about to delete %s's account. Continue? Y/N: ", name);
	         if (getInput().equalsIgnoreCase("y")){
	        	 vc.removeAdmin(id);
	         }
	         else if (getInput().equalsIgnoreCase("n")){
	        	 System.out.println("Cancelled.");
	         }
		 }
	     catch(NullPointerException e){
	         System.out.println("The admin account does not exist.");
	     }
	     catch(Exception e){
	         System.out.println(e);
	     }
	 }

	private void deleteStaff()
    {
    	 try
    	 {
    		System.out.print("Enter Staff ID: ");
        	int id = Integer.parseInt(getInput());
        	System.out.printf("Warning! You are about to delete %d. Continue? Y/N: ", id);
            if (getInput().equalsIgnoreCase("y")){
             vc.removeStaff(id);
            }
            else if (getInput().equalsIgnoreCase("n")){
             System.out.println("Cancelled.");
            }
    	 }
         catch(NullPointerException e){
            System.out.println("Staff does not exist.");
         }
         catch(Exception e){
            System.out.println(e);
         }
     }
     
    private void modifyVotingRange()
    {
    	boolean back = false;
     	while(!back){
     		System.out.printf("\nChoose an option: \n1) Add a Voting Date Range\n2) View Voting Date Range\n3) Update Date Range\n4) Back\n");
         	try
         	{
             	int option = Integer.parseInt(getInput());

             	switch(option){
                 	case 1: createDateRange();
                 		break;
                 	case 2: displayDateRange();
                 		break;
                 	case 3: updateDateRange();
                 		break;
                 	case 4: back = true;
                 		break;
             	}
         	}
            catch(NumberFormatException e){
                System.out.println("That was not a number you entered\nPlease try again");
            }
            catch(NullPointerException e){
                System.out.println("Invalid Input. Please try again");
            }
            catch(Exception e){
                System.out.println(e);
            }
         	// end of try/catch
         	
         	if(back == true){
         		break;
         	}
         	else{
             	System.out.println("Press any key to continue");
             	getInput();
         	}
     	} // end of loop
     }
     
    private void updateDateRange(){
    	try{
    		System.out.println("Enter start date (DD/MM/YY): ");
    		startDate = dateFormat.parse(getInput());
    		System.out.println("Enter end date (DD/MM/YY): ");
    		endDate = dateFormat.parse(getInput());
	    }
		catch(NullPointerException e){
		    System.out.println("Invalid Input. Please try again");
		}
		catch(Exception e){
		    System.out.println(e);
		}
    }
    
     private void modifyAdminAccounts()
     {
    	boolean back = false;
    	while(!back){
    		System.out.printf("\nChoose an option: \n1) Display a list of Admin Accounts\n2) Add Admin Account\n3) Delete Admin Account\n4) Back\n");
        	try
        	{
            	int option = Integer.parseInt(getInput());

            	switch(option){
                	case 1: displayAdmins();
                		break;
                	case 2: createAdmin();
                		break;
                	case 3: deleteAdmin();
                		break;
                	case 4: back = true;
                		break;
            	}
        	}
            catch(NumberFormatException e){
                System.out.println("That was not a number you entered\nPlease try again");
            }
            catch(NullPointerException e){
                System.out.println("Invalid Input. Please try again");
            }
            catch(Exception e){
                System.out.println(e);
            }
        	// end of try/catch
        	
        	if(back == true){
        		break;
        	}
        	else{
            	System.out.println("Press any key to continue");
            	getInput();
        	}
    	} // end of loop
     }
     
     private void modifyStaffAccounts()
     {
    	 boolean back = false;
    	 while(!back){
    		 System.out.printf("\nChoose an option: \n1) Display a list of Staff Accounts\n2) Add Staff Account\n3) Delete Staff Account\n4) Back\n");
    		 try{
    			 int option = Integer.parseInt(getInput());

    			 switch(option){
	    			 case 1: displayStaff();
	    			 	break;
	    			 case 2: createStaff();
	    				 break;
	    			 case 3: deleteStaff();
	    				 break;
	    			 case 4: back = true;
	    			 break;
    			}
		 	}
			catch(NumberFormatException e){
			System.out.println("That was not a number you entered\nPlease try again");
			}
			catch(NullPointerException e){
			System.out.println("Invalid Input. Please try again");
			}
			catch(Exception e){
			System.out.println(e);
			}
         	
         	// end of try/catch
         	
         	if(back == true){
         		break;
         	}
         	else{
             	System.out.println("Press any key to continue");
             	getInput();
         	}

     	} // end of loop
     }
     
 	//validates administrator Username & Password
	 private boolean validateAdmin(String username, String password)
	 {
	 	ArrayList<?> admins = vc.getAdminList();
	 	Iterator<?> it = admins.iterator();
	
	 	while(it.hasNext())
	 	{
	 		theAdmin = (Admin)it.next();
	
			if(theAdmin.getUsername().equals(username)&&theAdmin.getPassword().equals(password))
			{
			    return true;
			}
			else
			{
				return false;
			}
	 	}
	 	return false;
	 }

	private boolean isWithinRange(Date testDate){
		return !(testDate.before(startDate) || testDate.after(endDate));
	}
}
