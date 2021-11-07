package claudiosoft.bellerofonte;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author Claudio Tortorelli - 2003
 *
 */
//-----------------------------------------
class Anonymus extends Single_test implements Serializable {

    /**
     * Fields
     */
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
    private String _expectedInput = "";
/////////////////////////// END OF COMMON CHANGE AREA

    /**
     * **********************************************
     * Constructor
     */
    public Anonymus() {
        super();
    }

    /**
     * **********************************************
     * This setUp method read an ordered list of parameters embedded in a
     * Parameters object, some test's options and single test's number. With
     * these information intialize its object.
     *
     * IN: vector with parameters specified in file macro test, the number of
     * this test parameters have at first position the test's name and in the
     * other the parameters OUT: void
     */
    public void setUp(Vector parameters, int numSingleTest) {
        _myNumber = numSingleTest;
        _myName = (String) parameters.elementAt(0);
        _finalResult = "notCheckedYet";
        _timeOfExecution = "0";

/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
        _expectedInput = (String) parameters.elementAt(1); //read words from file test txt or xml
/////////////////////////// END OF COMMON CHANGE AREA ----
    }

    /**
     * **********************************************
     * This method implement the particular execution of this single test. It
     * take as parameters the actual report and the list of values returned by
     * some eventual previous tests. It return its values.
     *
     * IN: report for add results, file name target, vector with results of
     * previous test OUT: vector with the results of this test
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
        rep.addResultExpected("Input: " + _expectedInput);
/////////////////////////// END OF COMMON CHANGE AREA ----

        // get the page's state elements, if equal to 0 then the page was not loaded correctly
        Vector pageData = page.getCurrent();
        if (pageData.size() != 0) {
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----

            /* IF GOOD
 *	rep.addMyResultGood("The file upload has been setted"); //test passed to Report
 * _finalResult = "PASSED"; // don't change: it is used for comparison
 *
 * IF BAD
 * rep.addMyResultBad("The file upload has been setted"); //test failed to Report
 * _finalResult = "FAILED"; // don't change: it is used for comparison
             */
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
