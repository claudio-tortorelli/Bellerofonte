package claudiosoft.bellerofonte;

import java.util.Vector;
import java.io.Serializable; 
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 	
import java.net.*;
import java.io.IOException;

// Libraries used
import com.meterware.httpunit.*;
import java.util.regex.*; // elaborate regular expression
import org.htmlparser.util.*;
import org.htmlparser.parserapplications.*;
/////////////////////////// END OF COMMON CHANGE AREA 	
/**
 * @author Claudio Tortorelli - 2003
 * 
 */
//-----------------------------------------

class CheckTextInPage extends Single_test implements Serializable
{
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
  private String _expectedText = "";
/////////////////////////// END OF COMMON CHANGE AREA 	
 	
/**
 ***********************************************
 * Constructor
 */
	public CheckTextInPage()
	{
		super();
	}

/**
 ***********************************************
 * This setUp method read an ordered list of 
 * parameters embedded in a Parameters object, some
 * test's options and single test's number. With
 * these information it intialize its object.
 *
 * IN: vector with parameters specified in file macro test, the number of this test * 
 * OUT: void
 */
 	public void setUp(Vector parameters, int numSingleTest, String host, String path)
 	{ 			
 		super.setUp(parameters, numSingleTest, host, path); 		
 		
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----	
		_expectedText = (String)parameters.elementAt(1); //read words from file test txt or xml					
/////////////////////////// END OF COMMON CHANGE AREA ----	
 	}

/**
 ***********************************************
 * This method implement the particular execution of
 * this single test. It take as arguments the actual 
 * report that will be modified, the actual web page's
 * elements and options, the number of test. It return
 * nothing and adjourn the report with the result.
 *
 * IN: report for add results, file name target, vector with results of previous test
 * OUT: void
 */ 	
	public void execute(Report rep, Web_page page, String subNum) 
	{			
	// strings of header of Single_test	
		String num = _myNumber + subNum;
		String target = page.getTarget();
	    String head1 = "TEST: \""+_myName+"\""+" | Url: "+target; 
	    rep.addHeader(head1, num);	       
	// start timer
		Timer tim = new Timer();
		tim.start();		    

/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
		rep.addResultExpected("Text: " + _expectedText);		
/////////////////////////// END OF COMMON CHANGE AREA ----			
	// get the page's state elements, if equal to 0 then the page was not loaded correctly
		Vector pageData = page.getCurrent();			
		if (pageData.size() != 0)
		{			
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
			try
			{
			// get the target
				String url = page.getTarget();			
			// crate a new string extractor object
				StringExtractor se = new StringExtractor(url);
			// extract in "text" all text in the page
				String text = se.extractStrings(false); 								
			// create a pattern with the string to match
				Pattern p = Pattern.compile(_expectedText);
			// search the string in the text
				Matcher m = p.matcher(text);
				if (m.find())
				{
					rep.addMyResultGood("The text is in the page"); //test passed to Report								
					_finalResult = "PASSED"; // don't change: it is used for comparison
				}
				else
				{
					rep.addMyResultBad("The text pattern isn't in the page"); //test failed to Report								
					_finalResult = "FAILED"; // don't change: it is used for comparison
				}				
			}
			catch (ParserException e)
			{
				rep.addError("Error during parse");			
				_finalResult = "ERROR";
			}
		
//////////////////////////// END OF COMMON CHANGE AREA ----			
		}
	// the web page isn't load correctly
		else
		{			
			rep.addError("Impossible to perform this test");			
			_finalResult = "ERROR";
		}
	// stop timer and set elapsed time		
		tim.stop();		
		long oneSec = 1000;
		_timeOfExecution = tim.elapsed()/oneSec + "." + tim.elapsed()%oneSec;
		rep.addSecOfExecution(_timeOfExecution);
	// add to Report the significative datas 		
		rep.addDataToCompare(_myName, _finalResult, _timeOfExecution);
	// end of execution 
		rep.addSeparation();	
	}		
} // end of class