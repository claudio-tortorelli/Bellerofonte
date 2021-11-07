package claudiosoft.bellerofonte;

import com.meterware.httpunit.Button;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author Claudio Tortorelli - 2003
 *
 */
//-----------------------------------------
class CheckButtonValue extends Single_test implements Serializable {

    /**
     * Fields
     */
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
    private String _expectedForm = "";
    private String _expectedButton = "";
    private String _expectedAction = "";
/////////////////////////// END OF COMMON CHANGE AREA

    /**
     ***********************************************
     * Constructor
     */
    public CheckButtonValue() {
        super();
    }

    /**
     ***********************************************
     * This setUp method read an ordered list of parameters embedded in a
     * Parameters object, some test's options and single test's number. With
     * these information it intialize its object.
     *
     * IN: vector with parameters specified in file macro test, the number of
     * this test * OUT: void
     */
    public void setUp(Vector parameters, int numSingleTest, String host, String path) {
        super.setUp(parameters, numSingleTest, host, path);

/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
        _expectedForm = (String) parameters.elementAt(1); //read words from file test txt or xml
        _expectedButton = (String) parameters.elementAt(2); //read words from file test txt or xml
        _expectedAction = (String) parameters.elementAt(3); //read words from file test txt or xml
/////////////////////////// END OF COMMON CHANGE AREA ----
    }

    /**
     ***********************************************
     * This method implement the particular execution of this single test. It
     * take as arguments the actual report that will be modified, the actual web
     * page's elements and options, the number of test. It return nothing and
     * adjourn the report with the result.
     *
     * IN: report for add results, file name target, vector with results of
     * previous test OUT: void
     */
    public void execute(Report rep, Web_page page, String subNum) {
        // strings of header of Single_test
        String num = _myNumber + subNum;
        String target = page.getTarget();
        String head1 = "TEST: \"" + _myName + "\"" + " | Url: " + target;
        rep.addHeader(head1, num);
        // start timer
        Timer tim = new Timer();
        tim.start();

/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
        rep.addResultExpected("Form: " + _expectedForm);
        rep.addResultExpected("Button value: " + _expectedButton);
        rep.addResultExpected("Action to perform: " + _expectedAction);
/////////////////////////// END OF COMMON CHANGE AREA ----
        // get the page's state elements, if equal to 0 then the page was not loaded correctly
        Vector pageData = page.getCurrent();
        if (pageData.size() != 0) {
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
            try {
                // get the Response
                WebResponse resp = (WebResponse) pageData.elementAt(1);
                // try to open a web form with string parameter passed
                WebForm form = resp.getFormWithName(_expectedForm);
                // the parameter isn't a name form
                if (form == null) {
                    form = resp.getFormWithID(_expectedForm);
                    // the parameter isn't an identifier
                    if (form == null) {
                        // try to open it with his ordinal number
                        int expNumInt = Integer.parseInt(_expectedForm);
                        WebForm[] allForm = resp.getForms();
                        if (expNumInt < allForm.length && expNumInt > -1) {
                            form = allForm[expNumInt];
                        }
                    }
                }
                // one of tried open operation was successfully
                if (form != null) {
                    // get all button of form
                    Button[] bb = form.getButtons();
                    if (bb != null) {
                        // verify if there is one with the expected value
                        int i = 0;
                        while (i < bb.length && !bb[i].getValue().equalsIgnoreCase(_expectedButton)) {
                            i++;
                        }
                        if (i == bb.length) {
                            rep.addMyResultBad("Button with the expected value not found"); //test failed to Report
                            _finalResult = "FAILED"; // don't change: it is used for comparison
                        } else {
                            if (!_expectedAction.equalsIgnoreCase("")) {
                                bb[i].click();
                                rep.addMyResultGood("Button found and clicked"); //test failed to Report
                            } else {
                                rep.addMyResultGood("The form has a button with the expected value"); //test failed to Report
                            }
                            _finalResult = "PASSED"; // don't change: it is used for comparison
                        }
                    } // there aren't any buttons
                    else {
                        rep.addMyResultBad("There aren't any button in the form"); //test failed to Report
                        _finalResult = "FAILED"; // don't change: it is used for comparison
                    }
                } else {
                    rep.addMyResultBad("Form not found"); //test failed to Report
                    _finalResult = "FAILED"; // don't change: it is used for comparison
                }
            } catch (Exception e) {
                rep.addError("Some errors in the execution");
                _finalResult = "ERROR";
            }
//////////////////////////// END OF COMMON CHANGE AREA ----
        } // the web page isn't load correctly
        else {
            rep.addError("Impossible to perform this test");
            _finalResult = "ERROR";
        }
        // stop timer and set elapsed time
        tim.stop();
        long oneSec = 1000;
        _timeOfExecution = tim.elapsed() / oneSec + "." + tim.elapsed() % oneSec;
        rep.addSecOfExecution(_timeOfExecution);
        // add to Report the significative datas
        rep.addDataToCompare(_myName, _finalResult, _timeOfExecution);
        // end of execution
        rep.addSeparation();
    }
} // end of class
