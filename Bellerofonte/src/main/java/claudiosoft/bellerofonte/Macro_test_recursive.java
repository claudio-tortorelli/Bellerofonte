package claudiosoft.bellerofonte;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.util.Vector;
import org.xml.sax.*; //needed by httpunit
import com.meterware.httpunit.*;

/**
 * This class manage and execute various single_test as
 * Macro_test.
 * It signs its results on Report but not hold anything.
 * It can be called in recursive manner
 */ 
class Macro_test_recursive 
{	
/** 
 * Fields
 */ 
	
/**
 ***********************************************
 * Constructor
 */
	public Macro_test_recursive() 
	{		
	}

/**
 ***********************************************
 * This method extract sequentially all single_test
 * in _tests and click their execute method
 *
 * IN: void
 * OUT: void
 */
	public void execute(Vector _tests, int numberToStart, Report _actualRep, Web_page page, String subNum) 
	{	
	// start the timer for check total time of execution
		Timer tim = new Timer();
		tim.start();
	// execute all single _test in _tests 						
		Single_test toExec;				
		int i = numberToStart;
		while (i < _tests.size())
		{			
		// get a test
			toExec = (Single_test)_tests.elementAt(i);
		// check if the test is a followLinks
			if ((toExec.getName()).equals("FollowLinks"))
			{
			// returnParam hold strings with all pages's url to test
				int j = 0;			
				Vector linksToFollow = toExec.executeFollow(_actualRep, page, subNum);				
				Macro_test_recursive mTestRec = new Macro_test_recursive();
				while (j < linksToFollow.size())
				{
				// get url and number of a target link
					_actualRep.addTargetSeparation();
					String target = (String)linksToFollow.elementAt(j);					
					page = loadCurrent(target, _actualRep);
					String subNum2 = subNum + "." + (j+1);
					mTestRec.execute(_tests, i+1, _actualRep, page, subNum2);					
					j++; 
				}
			// exit from execution
				i = _tests.size();
			}	
		// the test isn't a follow links so it can be runned normally
			else
			{ 				
				toExec.execute(_actualRep, page, subNum);
				i++;
			}			
		}	
	// stop the timer of execution
		tim.stop();
	}

/**
 ***********************************************
 * This method load the actual page and store its
 * state's elements to permit all Single test to 
 * share informations. It use httpUnit elements.
 *
 * IN: String target-url's page and actual report (the options aren't read here)
 * OUT: a Web_page that hold WebConversation, WebResponse and ClientProperties 
 */
 	public Web_page loadCurrent(String target, Report rep)
 	{
 	// create new web page
 		Web_page pageToCheck = new Web_page(); 		
 	// set his name
 		pageToCheck.setTarget(target);
 		try
 		{
 		// get elements to store in page
 			WebConversation conv = new WebConversation();
 			WebResponse resp = conv.getResponse(target);
 			ClientProperties prop = conv.getClientProperties();
 		// store the elements found
 			Vector webElements = new Vector();
 			webElements.addElement(conv);
 			webElements.addElement(resp);
 			webElements.addElement(prop);
 			pageToCheck.setCurrent(webElements); 			
 		}
 	// manage the common errors
		catch (SAXException e)
		{ 			
			rep.addError("It is not possible to get links after parsing HTML page.");			
			Web_test.addErrorState();
		}
		catch (MalformedURLException e)
		{		
			rep.addError("The URL that is considered is malformed:" +target);						
			Web_test.addErrorState();
		}
		catch (NoRouteToHostException e)
		{		
			rep.addError("The host isn't attainable");						
			Web_test.addErrorState();
		}
		catch (HttpNotFoundException e)
		{
			rep.addError("error 404: document not found at " +target);
			Web_test.addErrorState();
		}
		catch (UnknownHostException e)
		{
			rep.addError("The host address isn't correct or not findable at "+target);
			Web_test.addErrorState();
		}
		catch (IOException e)
		{
			rep.addError("It's verified an I/O error");
			Web_test.addErrorState();
		}
		catch (NullPointerException e)
		{
			rep.addError("Some parameters received are empty");
			Web_test.addErrorState();
		}    
		catch (RuntimeException e)
		{
			rep.addError("Probably must be specified an host name in FollowLink test for "+ target);
			Web_test.addErrorState();
		}    
	// return the page load
		return pageToCheck;
 	}
 	 
}// end of class
