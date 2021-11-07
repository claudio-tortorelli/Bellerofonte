package claudiosoft.bellerofonte;

import java.util.Vector;
import java.io.Serializable; 
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 	
import java.net.*;
import java.io.IOException;

// Libraries used
import com.meterware.httpunit.*;
import org.xml.sax.*; //needed by httpunit
/////////////////////////// END OF COMMON CHANGE AREA 	
/**
 * @author Claudio Tortorelli - 2003
 * 
 */

//-----------------------------------------

class CheckLink extends Single_test implements Serializable
{
	
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
  private String _expectedLink = "";
  private String _expectedUrl = "";
/////////////////////////// END OF COMMON CHANGE AREA ----	
 	
/**
 ***********************************************
 * Constructor
 */
	public CheckLink()
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
		_expectedLink = (String)parameters.elementAt(1); 	
		_expectedUrl = (String)parameters.elementAt(2);
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
		rep.addResultExpected("Link: " + _expectedLink);
		rep.addResultExpected("Url: " + _expectedUrl);
/////////////////////////// END OF COMMON CHANGE AREA ----			
	// get the page's state elements, if equal to 0 then the page was not loaded correctly
		Vector pageData = page.getCurrent();			
		if (pageData.size() != 0)
		{			
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----	
			try
			{
			// get the Response
				WebResponse resp = (WebResponse)pageData.elementAt(1);					
			// try to accomplish the test		 
				WebLink linkFound = resp.getLinkWith(_expectedLink);
				if (linkFound == null)
				{
					linkFound = resp.getLinkWithID(_expectedLink);
					if (linkFound == null)
					{
						linkFound = resp.getLinkWithName(_expectedLink);
						if (linkFound == null)
						{
							linkFound = resp.getLinkWithImageText(_expectedLink);
						}
					}					
				}
			// check: the title read is equal to title expected?		
				if (linkFound != null)
				{					
					if (!_expectedUrl.equals(""))
					{
						if ( (linkFound.getURLString()).equals(_expectedUrl) )
						{
							rep.addMyResultGood("The link searched is present");								
							_finalResult = "PASSED"; // don't change: it is used for comparison							
						}
						else
						{
							rep.addMyResultBad("The link searched is present but don't has the expected url");								
							_finalResult = "FAILED"; // don't change: it is used for comparison
						}
					}
					else
					{
						rep.addMyResultGood("The link searched is present");								
						_finalResult = "PASSED"; // don't change: it is used for comparison
					}					
				}						
				else 
				{
					rep.addMyResultBad("The link isn't in the page");								
					_finalResult = "FAILED"; // don't change: it is used for comparison
				}
			}	
	 	//manage common errors during elaboration
			catch (SAXException e)
			{ 			
				rep.addError("It is not possible to parsing the page");			
				_finalResult = "ERROR";
			}				
			catch (Exception e)
			{ 			
				rep.addError("Some errors in the execution");						
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
