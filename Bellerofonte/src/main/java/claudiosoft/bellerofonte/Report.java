package claudiosoft.bellerofonte;

import java.io.Serializable;
import java.util.Vector;

/**
 * This class keep all results that the Single_test executed product and add here.
 * There are good and bad messages, errors signals,
 * headers, and so on. A Report is hold in a Macro_test
 * or is saved in a txt file.
 */
class Report implements Serializable
{

/** 
 * Fields
 */
	private String _id;
	private String _date;	
	private String _target;
	private Vector _resultsStr = new Vector();
	private Vector _resultsToCompare = new Vector();

/**
 ***********************************************
 * Constructor
 */	
	public Report(String identifier, String date, String targetOfTest) 
	{
		_id = identifier; // equal to Macro_test name
		_date = date;	
		_target = targetOfTest; // is checked when on compare two report		 
		_resultsStr.addElement("-----------------------------------------------------");
		_resultsStr.addElement("REPORT OF TEST: " + _id + " - " + _date);
		_resultsStr.addElement("-----------------------------------------------------");
		_resultsStr.addElement(" ");
	}

/**
 ***********************************************
 * It return the initial target of single tests that
 * have write their results
 */
	public String getTarget() 
	{
		return _target;	
	}

/**
 ***********************************************
 * Add a new string as result of some Single_test
 */
	public void addLine(String res) 
	{
		_resultsStr.addElement("   " + res);
	}

/**
 ***********************************************
 * Add a new string as result of some Single_test
 */
	public void addMyResultGood(String res) 
	{
		_resultsStr.addElement("   + TEST PASSED: " + res);
		_resultsStr.addElement(" ");	
	}


/**
 ***********************************************
 * Add a new string as result of some Single_test
 */
	public void addMyResultBad(String res) 
	{
		_resultsStr.addElement("   - TEST FAILED: " + res);
		_resultsStr.addElement(" ");	
	}

/**
 ***********************************************
 * Add new data about one test for comparison
 */
	public void addDataToCompare(String nameTest, String itsResult, String timeExecution)
	{
		_resultsToCompare.addElement(nameTest);		
		_resultsToCompare.addElement(itsResult);	
		_resultsToCompare.addElement(timeExecution);	
	}

/**
 ***********************************************
 * This method return the string with a data stored
 * that must be compared with other
 *
 * IN : number of element to compare
 * OUT : string with result or empty string if i is out of bounds
 */
	public String getDataToCompare(int i)
	{
		if (i < _resultsToCompare.size())
		{
			return (String) _resultsToCompare.elementAt(i);				
		}
		else {return (" ");}
	}

/**
 ***********************************************
 * IN : void
 * OUT : int that is the size of _resultsToCompare
 */
	public int getNumDataToCompare()
	{
		return _resultsToCompare.size();
	}


/**
 ***********************************************
 * IN : void
 * OUT : string with date of report
 */
	public String getDate()
	{
		return _date;
	}

	
/**
 ***********************************************
 * Add a new string as header of Single_test
 */
	public void addHeader(String head, String num) 
	{
		_resultsStr.addElement(num+"] "+ head);			
	}

/**
 ***********************************************
 * Add a new string as error found on execute Single_test
 */
	public void addError(String err) 
	{
		_resultsStr.addElement("   ! ERROR: " +err);	
		_resultsStr.addElement(" ");	
	}

/**
 ***********************************************
 * Add a new string as error found on execute Single_test
 * It take a long that hold the time.
 */
	public void addSecOfExecution(long sec)
	{
		_resultsStr.addElement("   # Seconds elapsed to accomplish the test: " +sec);	
		_resultsStr.addElement(" ");	
	}

/**
 ***********************************************
 * Add a new string as seconds of execution 
 * It take a string.
 */
	public void addSecOfExecution(String sec)
	{
		_resultsStr.addElement("   # Seconds elapsed to accomplish the test: " +sec);	
		_resultsStr.addElement(" ");	
	}

/**
 ***********************************************
 * Add a new string as separator from a target to other
 */
	public void addTargetSeparation()
	{
		_resultsStr.addElement("########### FOLLOW LINK #############################");
		_resultsStr.addElement(" ");	
	}


/**
 ***********************************************
 * Add a new string as separator from test and test
 */
	public void addSeparation()
	{
		_resultsStr.addElement("/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\");	
		_resultsStr.addElement(" ");	
	}

/**
 ***********************************************
 * Add a new string as result that Single_test must return 
 * to consider it as passed
 */
	public void addResultExpected(String exp) 
	{
		_resultsStr.addElement("   Result expected: \"" +exp+"\"");	
	}

/**
 ***********************************************
 * It return the Vector of strings's result
 */	
	public Vector getResults() 
	{
		Vector res =(Vector)_resultsStr.clone();
		return (res);
	}

/**
 ***********************************************
 * It return the identifier of report
 */	
	public String getMyName() 
	{
		return (_id);
	}

/**
 ***********************************************
 * It return the line number i of report
 */		
 	public String getSingleLine(int i) 
 	{
 		if (i < _resultsStr.size())
 		{
 			return (String)_resultsStr.elementAt(i);
 		}
 		else 
 		{
 			return null;
 		}
 	}

/**
 ***********************************************
 * copy constructor
 */

	public Report(Report other) 
	{
		if(this != other)
		{
			this._id = other._id;
			this._date = other._date;
			this._target = other._target;
			this._resultsStr = other._resultsStr;
			this._resultsToCompare = other._resultsToCompare;
		}
	}

/**
 ***********************************************
 * toString method
 */	
	public String toString() 
	{

		String sep = System.getProperty("line.separator");

		StringBuffer buffer = new StringBuffer();
		buffer.append(sep);
		buffer.append("_id = ");
		buffer.append(_id);
		buffer.append(sep);
		buffer.append("_date = ");
		buffer.append(_date);
		buffer.append(sep);
		buffer.append("_target = ");
		buffer.append(_target);
		buffer.append(sep);
		buffer.append("_resultsStr = ");
		buffer.append(_resultsStr);
		buffer.append(sep);
		buffer.append("_resultsToCompare = ");
		buffer.append(_resultsToCompare);
		buffer.append(sep);
		
		return buffer.toString();
	}
}// end of class
