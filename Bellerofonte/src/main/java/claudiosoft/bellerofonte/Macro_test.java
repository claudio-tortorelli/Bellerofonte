package claudiosoft.bellerofonte;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.util.Vector;
import java.util.Properties;
import org.xml.sax.*; //needed by httpunit
import com.meterware.httpunit.*;

/**
 * This class manage and execute various single_test.
 * It keep also the last two report generated and
 * saved after its execute operation,
 */
 
class Macro_test implements Serializable
{	
/** 
 * Fields
 */ 
  	private String _name = ""; //the name (identifier) of Macro_test
	private Vector _tests = null; //Vector of Single_test	
	private Vector _reports = null; //vector of Report;	
	private Report _actualRep = null; //the report that is created at next execute;
	private TestOption _options = null; //options of Macro_test
	
/**
 ***********************************************
 * Constructor
 */
	public Macro_test(String name) 
	{
		_name = name;
		_options = new TestOption();
		_reports = new Vector();		
		_tests = new Vector();
	}

/**
 ***********************************************
 * It return the actual options of the Macro_test.
 *
 * IN: void
 * OUT: options
 */
 	public TestOption getOptions() 
 	{
 		return _options;
 	}

/**
 ***********************************************
 * IN: void
 * OUT: It return the name of the Macro_test
 */
 	public String getName() 
 	{
 		return _name;
 	}

/**
 ***********************************************
 * IN: void
 * OUT: It return the number of single test in macro test
 */
 	public int getNumSingleTests() 
 	{
 		return _tests.size();
 	}

/**
 ***********************************************
 * It add one single_test in the _tests vector that
 * next will be execute.
 *
 * IN: a Single_test to add
 * OUT: void
 */
	public void addSingleTest(Single_test sTest) 
	{		
		_tests.addElement(sTest);		
	}

/**
 ***********************************************
 * It read from a vector of String the names of 
 * Single_test actually available, and from another vector
 * the name and the parameters of the single_test
 * that will be created.
 *
 * IN: the vector of Single_test that are known, the vector of string with the new test info readed from file, the number of this test
 * OUT: an object of class Single_test that is the new particular single test created
 */
	public Single_test newSingleTest(Vector testAvailable, Vector newTestInfo, int numSingleTest) 
	{	
	// create a generic Single test and read his particular name
		Single_test testObj = null;
		String nameTestStr = (String)newTestInfo.elementAt(0);
		try
		{			
		// check if its name is in the list of tests available
			int count = 0;		
			boolean found = false;
			for (int i = 0; i < testAvailable.size(); i++)
			{
				if (nameTestStr.equals((String)testAvailable.elementAt(i)))
				{
					found = true;
				}
			}		
			if (found)
			{		
			//call constructor and cast to Single_test
				String full = "orbilio.webTest.bellerofonte." + nameTestStr ;								
				Class typeName = Class.forName(full);
				testObj = (Single_test)typeName.newInstance();				
			//pass to new Single_test the parameters vector and context options
				testObj.setUp(newTestInfo, numSingleTest, _options.getHost(), _options.getPath());
			}			
		}
		catch(ClassNotFoundException e)
		{			
			System.out.println ("Error: class not found for test " + nameTestStr);
			Web_test.addErrorState();
		}		
		catch(InstantiationException e)
		{	
			System.out.println ("Error on istantiation for test " + nameTestStr);	
			Web_test.addErrorState();
		}	
		catch(IllegalAccessException e)		
		{		
			System.out.println ("Error: illegal access in test " + nameTestStr);
			Web_test.addErrorState();
		}					
		catch (ArrayIndexOutOfBoundsException e)
		{		
			System.out.println ("Error: array index out of bound in test " + nameTestStr);
			System.out.println ("Check the number of test's arguments");
			Web_test.addErrorState();
		}
		return (testObj);				
	}

/**
 ***********************************************
 * This return the _actualRep report
 */
 	public Report getActualReport() 
 	{
		return _actualRep; 		
 	}
 
/**
 *********************************************
 * This method display the last report's and compare
 * it with two previous report saved on tst object
 *
 * IN: void
 * OUT: void
 */
	public void viewOldAndCompare()
	{
		String optOld; // holds the targets of old and new test
		String optNew;				
	// verify if the actual rep and olds have the same options	and there are some other rep
		if (_reports.size() != 0)
		{
			optOld = ((Report)_reports.elementAt(0)).getTarget();
			optNew = _actualRep.getTarget();
			if (!optOld.equals(optNew))  
			{
				System.out.println("The comparison with old tests isn't possible: options aren't compatible or there aren't any other reports stored"); 
				Web_test.addErrorState();
			}
			else
			{
			// display the comparison			
				Web_test.scroll(1);
				System.out.println("COMPARISON BETWEEN LAST TESTS (Actual Test , Old Test , Very Old Test)");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				int i = 0;			
			// line by line
				int numDataToDisplay = _actualRep.getNumDataToCompare();			
				for (i = 0; i < numDataToDisplay; i++)
				{
					System.out.print(_actualRep.getDataToCompare(i));
					System.out.print(" , ");                							
					for (int j = 0; j < _reports.size(); j++)
					{
						if (j == _reports.size()-1)
						{						
							System.out.println(((Report)_reports.elementAt(j)).getDataToCompare(i));
						}
						else 
						{ 
							System.out.print (((Report)_reports.elementAt(j)).getDataToCompare(i)); 
							System.out.print(" , ");
						}						
					}				
					if (i%3 != 2)
						{System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - ");}
					else 
						{System.out.println("----------------------------------------------------");}
				}			
			
			}
		}			
	}

/**
 ***********************************************
 * This method extract sequentially all single_test
 * in _tests and click their execute method
 *
 * IN: void
 * OUT: void
 */
	public int[] execute() 
	{	
	// delete last _actualRep eventually saved
		_actualRep = null;
	// set initial report and load web page properties
		String target = _options.getProtocol() + _options.getHost() + _options.getPath() + _options.getFileName();
		_actualRep = new Report(_name, Web_test.giveTheDate(), target);				
		Web_page page = loadCurrent(target, _options, _actualRep);				
	// start the timer for check total time of execution
		Timer tim = new Timer();
		tim.start();	
	// initialize return array with results
		int[] resArr = new int[3];				
	// execute all single_test in _tests 						
		Single_test toExec;				
		int i = 0;		
		String subNum = "";
		while (i < _tests.size())
		{			
			subNum = ".0";			
		// get a test			
			toExec = (Single_test)_tests.elementAt(i);
		// check if the test is a followLinks
			if ((toExec.getName()).equals("FollowLinks"))
			{					
			// returnParam hold strings with all pages's url to test
				Vector linksToFollow = toExec.executeFollow(_actualRep, page, subNum);			
				int j = 0;								
				Macro_test_recursive mTestRec = new Macro_test_recursive();
				while (j < linksToFollow.size())
				{								
				// get url of a link and pass it to Macro_test_recursive as new target
					_actualRep.addTargetSeparation();
					target = (String)linksToFollow.elementAt(j);					
					page = loadCurrent(target, _options,  _actualRep);
					subNum = "."+(j+1);					
					mTestRec.execute(_tests, i+1, _actualRep, page, subNum);					
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
	// print on screen every single result write also on report
		Vector results = _actualRep.getResults();
		for (i = 0; i < results.size(); i++)
		{
			System.out.println((String)results.elementAt(i));			
		} 
	// store to array the report's results
		String resComp = "";
		for (i = 1; i < _actualRep.getNumDataToCompare(); i = i+3)
		{
			resComp = (String)_actualRep.getDataToCompare(i);
			if (resComp.equals("PASSED"))
			{
				resArr[0]++;
			}
			else if (resComp.equals("FAILED"))
			{
				resArr[1]++;
			}
			else if (resComp.equals("ERROR"))
			{
				resArr[2]++;
			}
		}
	// print on screen the total time of execution
		long oneSec = 1000;
		System.out.println("Total seconds of execution: " + tim.elapsed()/oneSec + "." + tim.elapsed()%oneSec);			
	// return res
		return (resArr);
	}

/**
 ***********************************************
 * This method load the actual page and store its
 * state's elements to permit all Single test to 
 * share informations. It use httpUnit elements for default.
 *
 * IN: String target-url's page, actual options and report
 * OUT: a Web_page that hold WebConversation, WebResponse and ClientProperties 
 */
 	public Web_page loadCurrent(String target, TestOption opt, Report rep)
 	{
 	// create new web page
 		Web_page pageToCheck = new Web_page(); 		
 	// set his name
 		pageToCheck.setTarget(target);
 		try
 		{	
 		// set other options of httpUnit
 		 	HttpUnitOptions.setCheckContentLength(opt.getMsgLength());
 		 	HttpUnitOptions.setDefaultCharacterSet(opt.getDefCharset());
 		 	HttpUnitOptions.setDefaultContentType(opt.getDefType());
 		 	HttpUnitOptions.setExceptionsThrownOnErrorStatus(opt.getExceptionOnError());
 		 	HttpUnitOptions.setExceptionsThrownOnScriptError(opt.getExceptionOnScript()); 		 	
 		 	HttpUnitOptions.setImagesTreatedAsAltText(opt.getImageAsTxt());
 		 	HttpUnitOptions.setLoggingHttpHeaders(opt.getHttpHeaderLogging());
 		 	HttpUnitOptions.setMatchesIgnoreCase(opt.getMatchingSensitive());
 		 	HttpUnitOptions.setParameterValuesValidated(opt.getValidateParameters());
 		 	HttpUnitOptions.setParserWarningsEnabled(opt.getParsingWarning());
 		 	HttpUnitOptions.setPostIncludesCharset(opt.getPostCharset());
 		 	HttpUnitOptions.setRedirectDelay(Integer.parseInt(opt.getRedirectDelay()));
 		 	HttpUnitOptions.setScriptingEnabled(opt.getScriptingEnabled());
 		
 		// put all values to default if selected
  		 	if (opt.getDefOptions())
 		 	{
 		 		HttpUnitOptions.reset();
 		 	}
 		 		
 		// get elements to store in page
 			WebConversation conv = new WebConversation(); 			
 			WebResponse resp = conv.getResponse(target);
 			ClientProperties prop = conv.getClientProperties();
 	 		 	 			
 		// set other options of the client
 			prop.setAcceptCookies(opt.getCookie());
 			prop.setAutoRedirect(opt.getAutoRedirect());
 			prop.setAutoRefresh(opt.getAutoRefresh());
 			prop.setIframeSupported(opt.getFloatFrame());
			prop.setUserAgent(opt.getUserAgent());
		 		 	
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
			rep.addError("It is not possible to parse correctly the HTML page.");			
			Web_test.addErrorState();
		}
		catch (MalformedURLException e)
		{		
			rep.addError("The URL that is considered is malformed:" + target);						
			Web_test.addErrorState();
		}
		catch (NoRouteToHostException e)
		{		
			rep.addError("The host isn't attainable");						
			Web_test.addErrorState();
		}
		catch (HttpNotFoundException e)
		{
			rep.addError("error 404: document not found at "+target);
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
			rep.addError("Probably must be specified an host name in FollowLink test for " + target);
			Web_test.addErrorState();
		}    
	// return the page load
		return pageToCheck;
 	}

/**
 ***********************************************
 * This method add the actual rep to vector _reports.
 * It verify if size of _reports is between 0 and 2. 
 *
 * IN: void
 * OUT: void
 */
	public void saveActualReport() 
	{
		if (_reports.size() < 2)
		{
			_reports.addElement(_actualRep);
		}
		else
		{
			_reports.setElementAt((Report)_reports.elementAt(0), 1);
			_reports.setElementAt(_actualRep, 0);
		}
	}

/**
 ***********************************************
 * This method receive the option's Properties from
 * Web_test and send this options to TestOption object.
 *
 * IN: Properties readed from file by hdd_interface
 * OUT: void
 */
 	public void setOptions(Properties opt)
	{
		try {_options.setUp(opt);}
		catch (Exception e)
		{
			System.out.println ("Error on set up macro test's option. Try to check option file content");
			System.exit(2);
		}
	}

/**
 ***********************************************
 * Copy constructor
 */

	public Macro_test(Macro_test other) 
	{
		if(this != other) 
		{
			this._name = other._name;
			this._tests = other._tests;
			this._reports = other._reports;
			this._actualRep = other._actualRep;
			this._options = other._options;
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
		buffer.append("_name = ");
		buffer.append(_name);
		buffer.append(sep);
		buffer.append("_tests = ");
		buffer.append(_tests);
		buffer.append(sep);
		buffer.append("_reports = ");
		buffer.append(_reports);
		buffer.append(sep);
		buffer.append("_actualRep = ");
		buffer.append(_actualRep);
		buffer.append(sep);
		buffer.append("_options = ");
		buffer.append(_options);
		buffer.append(sep);
		
		return buffer.toString();
	}	
}// end of class
