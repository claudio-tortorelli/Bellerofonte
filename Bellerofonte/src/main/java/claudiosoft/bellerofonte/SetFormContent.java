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

class SetFormContent extends Single_test implements Serializable
{
	
/**
 * Fields
 */  
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ---- 
  private String _expectedForm = "";	
  private String _expectedFieldName = "";
  private String _valueToSet = "";  
/////////////////////////// END OF COMMON CHANGE AREA 	
 	
/**
 ***********************************************
 * Constructor
 */
	public SetFormContent()
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
		_expectedForm = (String)parameters.elementAt(1); //read words from file test txt or xml	
		_expectedFieldName = (String)parameters.elementAt(2); //read words from file test txt or xml	
  		_valueToSet = (String)parameters.elementAt(3); //read words from file test txt or xml	
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
		rep.addResultExpected("Form: " + _expectedForm);
		rep.addResultExpected("Field: " + _expectedFieldName);
		rep.addResultExpected("Value to set: " + _valueToSet);
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
			// try to open a web form with string parameter passed				
				WebForm form = resp.getFormWithName(_expectedForm);
			// the parameter isn't a name form
				if (form == null)
				{									
					form = resp.getFormWithID(_expectedForm);
				// the parameter isn't an identifier
					if (form == null)
					{						
					// try to open it with his ordinal number
						int expNumInt = Integer.parseInt(_expectedForm);
						WebForm[] allForm = resp.getForms();						
						if (expNumInt < allForm.length && expNumInt > -1)
						{							
							form = allForm[expNumInt];							
						}								
					}
				}			
			//one of tried open  operation was successfully
				if (form != null)
				{				
				// perform the check		
					String[] allField = form.getParameterNames();			
					boolean found = false;
					for (int u = 0; u < allField.length; u++)
					{ 
						if (_expectedFieldName.equals(allField[u]))
						{  found = true; }
					}
					if (found == true)
					{
						form.setParameter(_expectedFieldName, _valueToSet);
						String fValue = form.getParameterValue(_expectedFieldName);
						if (fValue != null)
						{
							if (fValue.equals(_valueToSet))				
							{							
								rep.addMyResultGood("The field has been setted at the expected value"); //test passed to Report								
								_finalResult = "PASSED"; // don't change: it is used for comparison
							}						
							else 
							{
								rep.addMyResultBad("The field have this value: " + fValue); //test failed to Report
								_finalResult = "FAILED"; // don't change: it is used for comparison
							}
						}						
						else
						{
							rep.addError("Impossible to set this field");
							_finalResult = "ERROR";
						}		
					}
					else
					{
						rep.addError("Field not found");
						_finalResult = "ERROR";
					}
				}
				else
				{
					rep.addError("The parameters aren't correct to open form");
					_finalResult = "ERROR";
				}		
			}
		// manage the common errors					
			catch (SAXException e)
			{ 			
				rep.addError("It is not possible to parse the page");						
				_finalResult = "ERROR";
			}			 	
			catch (NumberFormatException e)
			{ 			
				rep.addError("It is not possible open specified form");			
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
