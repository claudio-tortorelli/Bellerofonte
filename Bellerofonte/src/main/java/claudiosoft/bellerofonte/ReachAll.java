package claudiosoft.bellerofonte;

import claudiosoft.btreestring.BtreeString;
import com.meterware.httpunit.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.Vector;
import org.xml.sax.*;

/**
 * @author Claudio Tortorelli - 2003
 *
 */
//-----------------------------------------
class ReachAll extends Single_test implements Serializable {

    /**
     * Fields
     */
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
    private String _expectedInputFile = ""; //with absolute path
    private String _expectedDepth = ""; // depth of visiting
    private String _timeout = "3000"; // timeout to wait response (3 sec default)
/////////////////////////// END OF COMMON CHANGE AREA

    /**
     * **********************************************
     * Constructor
     */
    public ReachAll() {
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
    public void setUp(Vector parameters, int numSingleTest, String host, String path) {
        super.setUp(parameters, numSingleTest, host, path);

/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
        _expectedInputFile = (String) parameters.elementAt(1); //read words from file test txt or xml
        _expectedDepth = (String) parameters.elementAt(2); //read words from file test txt or xml
        if (!parameters.elementAt(3).equals("")) {
            _timeout = (String) parameters.elementAt(3);
        }
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
        rep.addResultExpected("Input file name: " + _expectedInputFile);
        rep.addResultExpected("Depth: " + _expectedDepth);
        rep.addResultExpected("Timeout: " + _timeout + " ms");
/////////////////////////// END OF COMMON CHANGE AREA ----

        // get the page's state elements, if equal to 0 then the page was not loaded correctly
        Vector pageData = page.getCurrent();
        if (pageData.size() != 0) {
/////////////////////////// BELOW CAN CHANGE FROM TEST TO TEST ----
            try {
                // open file
                File tmp = new File(_expectedInputFile);
                // if the file is found
                if (tmp.exists()) {
                    FileInputStream fis = new FileInputStream(tmp);
                    DataInputStream dfis = new DataInputStream(fis);

                    // for search and a vector to verify that all files are reached
                    String filePath = dfis.readLine();
                    Vector filesToReach = new Vector();

                    // counter for position in vector
                    int pos = 0;
                    while (filePath != null) {
                        // insert in vector
                        filesToReach.addElement(filePath);
                        // read new line
                        filePath = dfis.readLine();
                        pos++;
                    }
                    // close streams
                    fis.close();
                    dfis.close();

                    // start to visit the remote site
                    // get the Response	and other data for recursive visit
                    WebResponse resp = (WebResponse) pageData.elementAt(1);
                    // create the btree that hold all page that will be find
                    BtreeString bts = new BtreeString();
                    // extract the depth of visit and timeout
                    int depth = Integer.parseInt(_expectedDepth);
                    long timeout = Long.parseLong(_timeout);
                    // call the recursive method
                    Web_test.scroll(1);
                    System.out.println("Pages reached:");
                    visitDep(bts, resp, depth, 0, target, timeout);
                    Web_test.scroll(3);
                    // after visit check if all pages searched are in the btree
                    boolean allReach = true;
                    int i = 0;
                    String name = "";
                    Vector notFound = new Vector();
                    while (i < filesToReach.size()) {
                        name = (String) filesToReach.elementAt(i);
                        if (bts.find(name) == -1) {
                            allReach = false;
                            notFound.addElement(name);
                        }
                        i++;
                    }
                    if (allReach == true) // if good
                    {
                        rep.addMyResultGood("All pages searched are found"); //test passed to Report
                        _finalResult = "PASSED"; // don't change: it is used for comparison
                    } else // if bad
                    {
                        rep.addMyResultBad("Not all pages searched are found"); //test failed to Report
                        rep.addLine("These pages aren't reachable:");
                        i = 0;
                        while (i < notFound.size()) {
                            rep.addLine(" - " + (String) notFound.elementAt(i));
                            i++;
                        }
                        _finalResult = "FAILED"; // don't change: it is used for comparison
                    }
                } else {
                    System.out.println("File not found " + _expectedInputFile);
                    _finalResult = "ERROR";
                }
            } catch (SAXException e) {
                System.out.println("An error occurred on parsing the test \"" + _myName + "\"");
                _finalResult = "ERROR";
            } catch (IOException e) {
                System.out.println("An error occurred on open file at \"" + _expectedInputFile + "\"");
                _finalResult = "ERROR";
            } catch (NumberFormatException e) {
                System.out.println("Error format of depth integer in the test \"" + _myName + "\"");
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

    /**
     ***********************************************
     * This method visit a site from a base page and in recursive manner recall
     * itself implementing a depth first visit. It add the nodes found in a
     * common btree of string (url).
     *
     * IN: a btree string, the base web response, the depth specified for visit,
     * the actual recursive depth, the base target page and the timeout to wait
     * the responde. OUT: void
     */
    private void visitDep(BtreeString bts, WebResponse resp, int depth, int thisDep, String target, long timeout) throws SAXException, IOException, UnknownHostException, UnknownServiceException {
        if (thisDep <= depth) {
            // common variables
            WebConversation conv = new WebConversation();
            WebLink[] links = resp.getLinks();
            WebResponse resp2 = null;
            String nameLink = "";
            Timer timeResp = new Timer();

            // for all links found
            for (int i = 0; i < links.length; i++) {
                // get string name
                nameLink = links[i].getURLString();
                // except mail links
                if (nameLink.toUpperCase().indexOf("MAILTO") == -1 && nameLink.toUpperCase().indexOf("CALLTO") == -1) {
                    // check if there are a protocol and host it's an absolute url
                    if (!nameLink.toUpperCase().startsWith("HTTP")) {
                        // it's a relative url: find the target's prefix and add to url

                        // it refers to an upper dir in the path
                        String prefix = "http://" + _host + _path;
                        if (nameLink.startsWith("../")) {
                            int count = 1;
                            while (nameLink.startsWith("../")) {
                                count++;
                                // del first two dot and slash
                                nameLink = nameLink.substring(3, nameLink.length());
                            }
                            // get the correct dir of path
                            // avoid the last '/'
                            int t = target.length() - 1;
                            while (count > 0 && t > 0) {
                                if (target.charAt(t) == '/') {
                                    count--;
                                }
                                t--;
                            }
                            prefix = target.substring(0, t + 2);
                        } // it refers to actual dir of path
                        else if (nameLink.startsWith("/")) {
                            nameLink = nameLink.substring(1, nameLink.length());
                        } else if (nameLink.startsWith("./")) {
                            nameLink = nameLink.substring(2, nameLink.length());
                        }
                        nameLink = prefix + nameLink;
                    }
                    // get the response from the link
                    try {
                        // check also if the response not arrive within the timeout
                        timeResp.start();
                        while (timeResp.elapsed() < timeout && resp2 == null) {
                            resp2 = conv.getResponse(nameLink);
                        }
                        timeResp.stop();
                        timeResp.reset();
                    } // manage the common exception
                    catch (UnknownHostException e) {
                    } catch (UnknownServiceException e) {
                    } catch (HttpNotFoundException e) {
                    }
                    // if any error isn't occurred and the reponse was getted
                    if (resp2 != null) {
                        // display the link
                        for (int y = 0; y < thisDep; y++) {
                            System.out.print("  ");
                        }
                        System.out.println("Level " + thisDep + ": " + nameLink);
                        // apply in recursive manner the depth visit
                        visitDep(bts, resp2, depth, thisDep + 1, target, timeout);
                        // insert the name of link visited in the btree
                        // del eventual postfix (of post method)
                        int post = nameLink.indexOf("?");
                        if (post != -1) {
                            nameLink = nameLink.substring(0, post);
                        }
                        bts.insert(nameLink, 0);
                    }
                    // del resp2 of previous link follow
                    resp2 = null;
                }
            }
        }
    }

} // end of class
