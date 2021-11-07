package claudiosoft.bellerofonte;

import java.util.Vector;
import java.io.Serializable; 
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 	
import java.net.*;
import java.io.IOException;

// Libraries used
import com.meterware.httpunit.*;
import org.xml.sax.*; //needed by httpunit
import java.util.regex.*; // elaborate regular expression
/////////////////////////// END OF COMMON CHANGE AREA 	

/**
 * @author Claudio Tortorelli - 2003
 * 
 */
//-----------------------------------------

class CheckMailLinks extends Single_test implements Serializable
{
	
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
	private String _stringToMatch = "";
	private String _expectedAddress = "";
/////////////////////////// END OF COMMON CHANGE AREA 	
 	
/**
 ***********************************************
 * Constructor
 */
	public CheckMailLinks()
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
		_stringToMatch = (String)parameters.elementAt(1);
		_expectedAddress = (String)parameters.elementAt(2);
		_expectedAddress = "mailto:" + _expectedAddress.toLowerCase();
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
		rep.addResultExpected("Name to match: " + _stringToMatch);
		rep.addResultExpected("Mailto: " + _expectedAddress);
/////////////////////////// END OF COMMON CHANGE AREA ----	

	// get the page's state elements, if equal to 0 then the page was not loaded correctly		
		Vector pageData = page.getCurrent();			
		if (pageData.size() != 0)
		{			
			try
			{				
			// get the Response
				WebResponse resp = (WebResponse)pageData.elementAt(1);													
			// prepare to accomplish the test
				WebLink[] linkFound; //vector that hold page's link			
			// charge the array
				linkFound = resp.getLinks();			
			// vector of mail links
				Vector mailLinks = new Vector();				
			// check which link found matching with str						
				for (int i = 0; i < linkFound.length; i++)
				{						
					String nameToMatch = "";
					nameToMatch = linkFound[i].asText();					
				// create a pattern with the string to match
					Pattern p = Pattern.compile(_stringToMatch);
				// search the string in the weblink's name
					Matcher m = p.matcher(nameToMatch);
				// if found and it is really a mailto link		
					//System.out.println(linkFound[i].getURLString());		
					if (m.find() && linkFound[i].getURLString().toLowerCase().startsWith("mailto"))
					{
						mailLinks.addElement(linkFound[i]);
					}
				}	
				if (mailLinks.size() != 0)
				{
					boolean done = true;					
					for (int i = 0; i < mailLinks.size(); i++)
					{
						String urlMail = ((WebLink)mailLinks.elementAt(i)).getURLString().toLowerCase();											
					// if the mail link hasn't the correct address report bad
						if (!_expectedAddress.equals(urlMail))
						{
							done = false;
						}						
					}	
					if (done)				
					{
						rep.addMyResultGood(mailLinks.size() + " mail links are how expected"); //test passed to Report								
						_finalResult = "PASSED"; // don't change: it is used for comparison															
					}
					// there are one or over mail links not correct
					else
					{ 
						rep.addMyResultBad("There are one or over mail links not as expected");								
						_finalResult = "FAILED"; // don't change: it is used for comparison
					}
				}
				else
				{
					rep.addMyResultBad("There aren't mail links");								
					_finalResult = "FAILED"; // don't change: it is used for comparison
				}			
			}			
			catch (SAXException e)
			{ 			
				rep.addError("It is not possible to parsing the page");			
				_finalResult = "ERROR";
			}				
			catch (ClassCastException e)
			{
				rep.addError("The type of parameters isn't as expected");
				_finalResult = "ERROR";
			}    	
			catch (NullPointerException e)
			{
				rep.addError("The parameters received are empty");
				_finalResult = "ERROR";
			}    		
			catch (Exception e)
			{ 			
				rep.addError("Some errors in the execution");						
				_finalResult = "ERROR";
			}			 			
//////////////////////////// END OF COMMON CHANGE AREA ----			
		}
	// stop timer and set elapsed time		
		tim.stop();
		long oneSec = 1000;
		_timeOfExecution = tim.elapsed()/oneSec + "." + tim.elapsed()%oneSec;
		rep.addSecOfExecution(_timeOfExecution);				
	// add to Report the significative datas 
		rep.addDataToCompare(_myName, _finalResult, _timeOfExecution);
	// end of single test area in Report
		rep.addSeparation(); 	
	}		
} // end of class
