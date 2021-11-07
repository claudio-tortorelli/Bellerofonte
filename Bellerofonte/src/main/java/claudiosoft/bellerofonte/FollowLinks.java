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

class FollowLinks extends Single_test implements Serializable
{
	
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
	private String _typeSearch = "";
	private String _stringToMatch = "";	
/////////////////////////// END OF COMMON CHANGE AREA 	
 	
/**
 ***********************************************
 * Constructor
 */
	public FollowLinks()
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
		_typeSearch = (String)parameters.elementAt(1);
		_stringToMatch = (String)parameters.elementAt(2);
/////////////////////////// END OF COMMON CHANGE AREA ----	
 	}

/**
 ***********************************************
 * This method implement the particular execution of
 * this single test. It take as arguments the actual 
 * report that will be modified, the actual web page's
 * elements and options, the number of test. It return
 * the vector with the strings of links to follow. Then It 
 * adjourn the report with the result.
 *
 * IN: report for add results, file name target, vector with results of previous test
 * OUT: vector with the results of this test
 */ 	
 	
	public Vector executeFollow(Report rep, Web_page page, String subNum) 
	{				
	// strings of header of Single_test	
		String num = _myNumber + subNum;
		String target = page.getTarget();				
	    String head1 = "TEST: \""+_myName+"\""+" | Url: "+ target; 
	    rep.addHeader(head1, num);	       
	// start timer
		Timer tim = new Timer();
		tim.start();	
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
		rep.addResultExpected(_typeSearch + "=" + _stringToMatch);
	// create a vector with links that match
		Vector linkMatching = new Vector();
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
			// check which link found matching with str
				for (int i = 0; i < linkFound.length; i++)
				{	
					String nameToMatch = "";
				// extract the text name or url from weblink	
					if (_typeSearch.equals("string"))
					{					
						nameToMatch = linkFound[i].asText();
					}
					else
					{
						nameToMatch = linkFound[i].getURLString();	
					}						
				// create a pattern with the string to match
					Pattern p = Pattern.compile(_stringToMatch);
				// search the string in the weblink's name
					Matcher m = p.matcher(nameToMatch);
				// return at the URL format
					nameToMatch= linkFound[i].getURLString();											
				// if found and isn't a mailto link then add the web link to return vector but not mail or video link							
					if (m.find() && !nameToMatch.toLowerCase().startsWith("mailto") && nameToMatch.toLowerCase().indexOf("callto") == -1)
					{
					// check if there are a protocol and host it's an absolute url
						if(nameToMatch.toUpperCase().startsWith("HTTP"))
						{
						// add to vector result								
							linkMatching.addElement(nameToMatch);
						}
					// it's a relative url: find the target's prefix and add to url
						else							
						{								
						// verify if the name link have some strange character in the beginning and remove them
						// it refers to an upper dir in the path		
							String prefix = "http://" + _host + _path;											
							if (nameToMatch.startsWith("../"))
							{			
								int count = 1;			
								while (nameToMatch.startsWith("../"))
								{											
									count++;
								// del first two dot and slash									
									nameToMatch = nameToMatch.substring(3,nameToMatch.length());												
								}
							// get the correct dir of path
							// avoid the last '/'
								int t = target.length()-1;
								while (count > 0 && t > 0)
								{
									if (target.charAt(t) == '/')
									{
										count --;
									}	
									t--;			
								}									
								prefix = target.substring(0,t+2);									
							}	
						// it refers to actual dir of path
							else if (nameToMatch.startsWith("/"))								
							{									
								nameToMatch = nameToMatch.substring(1, nameToMatch.length());
							}								
							else if (nameToMatch.startsWith("./"))
							{
								nameToMatch = nameToMatch.substring(2, nameToMatch.length());
							}
							nameToMatch = prefix + nameToMatch;																							
							linkMatching.addElement(nameToMatch);							
						}								
					}		
				}			
			// delete equals links
				for (int i = 0; i < linkMatching.size()-1; i++)
				{
					String thisLink = ((String)linkMatching.elementAt(i));					
					for (int j=i+1; j < linkMatching.size(); j++) 
					{
						String nextLink = ((String)linkMatching.elementAt(j));
						if (thisLink.equals(nextLink))
						{
							linkMatching.removeElementAt(j);
						}
					}
				}
				// report the results
				if (linkMatching.size() != 0)
				{
					rep.addMyResultGood(linkMatching.size() + " links are ready to be followed"); //test passed to Report								
					_finalResult = "PASSED"; // don't change: it is used for comparison															
				}
				// there aren't any links
				else
				{ 
					rep.addMyResultBad("There aren't any links to follow");								
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
	// return parameters (if next test must read it)
		return (linkMatching);
	}		
} // end of class
