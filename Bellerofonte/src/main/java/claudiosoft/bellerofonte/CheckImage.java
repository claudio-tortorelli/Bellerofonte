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

class CheckImage extends Single_test implements Serializable
{
	
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
  private String _expectedImage = "";  
  private String _expectedLink = ""; 	
/////////////////////////// END OF COMMON CHANGE AREA ----	
 	
/**
 ***********************************************
 * Constructor
 */
	public CheckImage()
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
			_expectedImage = (String)parameters.elementAt(1); 				
			_expectedLink = (String)parameters.elementAt(2); 	
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
		rep.addResultExpected("Image: " + _expectedImage);		
		rep.addResultExpected("Link: " + _expectedLink);
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
			// try to open image
				WebImage image = resp.getImageWithAltText(_expectedImage);				
				if (image == null)
				{
					image = resp.getImageWithName(_expectedImage);
					if (image == null)
					{
						image = resp.getImageWithSource(_expectedImage);						
					}
				}	
			// image found			
				if (image != null)
				{
				//check the image's link url
					if (!_expectedLink.equals(""))
					{					
						WebLink linkFound = image.getLink();
					// the image have a link
						if (linkFound != null)
						{							
							String url = linkFound.getURLString();
							if (url.equalsIgnoreCase(_expectedLink))
							{
								rep.addMyResultGood("Link is as expected");																							
								_finalResult = "PASSED";
							}
							else						
							{
								rep.addMyResultBad("The link has another value: " + url);
								_finalResult = "FAILED";
							}
						}						
					// the image isn't a link
						else
						{
							rep.addMyResultBad("The image is not a link");
							_finalResult = "FAILED";						
						}
					}	
				// it is only a verify of image's presence or the image isn't a link			
					else
					{
						rep.addMyResultGood("The image is present");
						_finalResult = "PASSED";
					}
				}
			// image not present
				else 
				{
					rep.addMyResultBad("The expected image isn't in the page");								
					_finalResult = "FAILED";
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
