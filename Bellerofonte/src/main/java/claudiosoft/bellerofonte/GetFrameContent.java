package claudiosoft.bellerofonte;

import java.util.Vector;
import java.io.Serializable; 
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 	
import java.net.*;
import java.io.IOException;

// Libraries used
import com.meterware.httpunit.*;
/////////////////////////// END OF COMMON CHANGE AREA 	
/**
 * @author Claudio Tortorelli - 2003
 * 
 */
//-----------------------------------------

class GetFrameContent extends Single_test implements Serializable
{
	
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
  private String _expectedFrame = "";	  
/////////////////////////// END OF COMMON CHANGE AREA 	
 	
/**
 ***********************************************
 * Constructor
 */
	public GetFrameContent()
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
		_expectedFrame = (String)parameters.elementAt(1); //read words from file test txt or xml			
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
		rep.addResultExpected("Frame to switch: " + _expectedFrame);
/////////////////////////// END OF COMMON CHANGE AREA ----			
	// get the page's state elements, if equal to 0 then the page was not loaded correctly
		Vector pageData = page.getCurrent();			
		if (pageData.size() != 0)
		{			 
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
			try
			{
			// get actual web conversation
				WebConversation conv = (WebConversation)pageData.elementAt(0);
			// get the Response	
				WebResponse resp = (WebResponse)pageData.elementAt(1);				
			// check if there is a frame with name selected
				String[] frameNames = resp.getFrameNames();
				if (frameNames.length != 0)
				{
				// there are frames
					int i = 0;
					boolean found = false;
					while (i < frameNames.length && !found)
					{
						if (frameNames[i].equals(_expectedFrame))
						{
						// frame found
							found = true;
						}						
						i++;
					}	
					if (found)
					{
					// get his content
						resp = conv.getFrameContents(_expectedFrame);
						pageData.setElementAt(resp,1);
						page.setCurrent(pageData);		
						rep.addMyResultGood("Frame found and ready to read"); //test passed to Report								
						_finalResult = "PASSED"; // don't change: it is used for comparison															
					}
					else
					{
					// frame not found
						rep.addMyResultBad("Frame name not found"); //test passed to Report								
						_finalResult = "FAILED"; // don't change: it is used for comparison															
					}									
				}
				else
				{
				//there aren't any frames
					rep.addMyResultBad("There aren't frames in the page"); //test passed to Report								
					_finalResult = "FAILED"; // don't change: it is used for comparison															
				}
			}				
		// manage the common errors					
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