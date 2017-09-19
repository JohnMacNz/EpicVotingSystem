package epicVotingSystem;
/**
* This class manages file read/writes, voting calculations and data updates by the voting system
* @author  Tony Assadi, John R. McLaren
* @version 2.0
* @since   01/09/2016
*/

public class Staff
{
    private int id, voted;
    private String name, password, timeVoted;

    public Staff(int id, String name, String password, int voted, String voteTime)
    {
        this.id = id;
        this.name = name;
        this.password = password;
        this.voted = voted;
        this.timeVoted = voteTime;
    }

    public void setID(int id)
    {
    	this.id = id;
    }

    public void setName(String name)
    {
    	this.name = name;
    }
    
    public void setPassword(String password)
    {
    	this.password = password;
    }

    public void setVoted()
    {
    	this.voted = 1;
    }
    public void setTimeVoted(String time)
    {
    	this.timeVoted = time;
    }

    public int getID()
    {
       return id;
    }

    public String getName()
    {
            return name;
    }

    public String getPassword()
    {
            return password;
    }
    public int hasVoted()
    {
            return voted;
    }
    public String getTimeVoted()
    {
    		return timeVoted;
    }
}

