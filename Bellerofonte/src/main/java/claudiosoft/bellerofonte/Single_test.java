package claudiosoft.bellerofonte;

import java.util.Vector;
import java.io.Serializable;
import com.meterware.httpunit.*;
import org.xml.sax.*;

/**
 * This class is the model for all single_tests 
 * really implemented
 */
abstract class Single_test implements Serializable
{

/**
 * Fields
 */
 // this field hold the option class common for all
 // Single_test of Macro_test
 	String _host = "";
 	String _path = "";
 	int _myNumber;
 	String _myName;
 	String _finalResult;
 	String _timeOfExecution; 	

/**
 ***********************************************
 * Constructor
 */ 
	public Single_test() 
	{		
	}

/**
 ***********************************************
 * This setUp method read an ordered list of 
 * parameters embedded in a Vector (the first location
 * is the test's name).
 */
 	public void setUp(Vector parameters, int numSingleTest, String host, String path)  	
 	{
 		_myNumber =  numSingleTest;
 		_myName = (String)parameters.elementAt(0); 
 		_finalResult = "notCheckedYet";
 		_timeOfExecution = "0"; 	
 		_host = host;
 		_path = path;	
 	}

/**
 ***********************************************
 * This method is specialized in every derived
 * single_test
 */
	public void execute(Report rep, Web_page page, String subNum) 
	{			
	}	

/**
 ***********************************************
 * This method is specialized in FollowLinks
 */
	public Vector executeFollow(Report rep, Web_page page, String subNum) 		
	{			
		return (new Vector());
	}	

/**
 ***********************************************
 * This method get the name of Single test. It is
 * specifically used to get this name before
 * execution of macro test.
 */
	public String getName() 
	{	
		return _myName;		
	}

/**
 ***********************************************
 * copy constructor
 */
	public Single_test(Single_test other) {
		if(this != other) {
			this._host = other._host;
			this._path = other._path;
			this._myNumber = other._myNumber;
			this._myName = other._myName;
			this._finalResult = other._finalResult;
			this._timeOfExecution = other._timeOfExecution;
		}
	}	
	
/**
 ***********************************************
 * toString method
 */
 
	public String toString() {
	
		String sep = System.getProperty("line.separator");
	
		StringBuffer buffer = new StringBuffer();
		buffer.append(sep);
		buffer.append("_host = ");
		buffer.append(_host);
		buffer.append(sep);
		buffer.append("_path = ");
		buffer.append(_path);
		buffer.append(sep);
		buffer.append("_myNumber = ");
		buffer.append(_myNumber);
		buffer.append(sep);
		buffer.append("_myName = ");
		buffer.append(_myName);
		buffer.append(sep);
		buffer.append("_finalResult = ");
		buffer.append(_finalResult);
		buffer.append(sep);
		buffer.append("_timeOfExecution = ");
		buffer.append(_timeOfExecution);
		buffer.append(sep);
		
		return buffer.toString();
	}


}// end of class
