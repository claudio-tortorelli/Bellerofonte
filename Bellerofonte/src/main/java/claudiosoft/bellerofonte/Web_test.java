/**
 **************************************************
 * BELLEROFONTE - Program for web testing for thesis
 * Started on 19/05/03
 *
 * BELLEROFONTE - Programma di web testing per tesi
 * Iniziato il 19/05/03
 *
 * DIARIO DI SVILUPPO:
 * 19/05 - ver. 0.1 - prime prove con i singoli test, costruzione delle classi principali
 * 25/05 - ver. 0.2 - introduzione della lettura di file esterni, funzionamento con test unico
 * 01/06 - ver. 0.3 - salvataggio e caricamento di Macro_test in XML dalla riga di comando
 * 03/06 - ver. 0.4 - completamento della definizione di Single_test ed esecuzione di pi� single_test contemporaneamente
 * 10/06 - ver. 0.5 - meccanismo di attraversamento pagine, modifica run-time dei target dei Single test da parte dei test, parsing di espressioni regolari
 * 11/06 - ver. 0.6 - raffinamento dell'attraversamento, migliore gestione comparazioni, migliore gestione eccezioni
 * 20/06 - ver. 0.7 - aggiunta degli altri Single test base, aggiunta opzioni selezionabili
 * 22/06 - ver. 0.72 - creazione pacchetto definitivo e jar eseguibile
 * 24/06 - ver. 0.8 - migliore gestione degli errori, aggiunta semplice interattivit� nel caso di parametri mancanti dalla riga di comando
 * 30/06 - ver. 0.81 - aggiunta del primo test non interamente httpUnit, aggiunti metodo toString e costruttore per copia alle classi maggiori
 * 02/07 - ver. 0.82 - revisione dei commenti, aggiunta di documentazione html, variazione nome pacchetto
 * 03/07 - ver. 0.83 - refactoring del metodo main e di tutto il sistema di gestione dei valori di ritorno di Bellerofonte: 0 ok, 1 test falliti, 2 errori
 * 14/07 - ver. 0.84 - aggiunta del test ReachAll che fa uso della libreria BtreeString. Uso per installation test.
 * 22/07 - ver. 0.85 - corretto ReachAll, aggiunto timeout, passaggio al pacchetto orbilio
 * 27/07 - ver. 0.86 - corretta e ottimizata la gestione di link relativi in FollowLinks e ReachAll; variato il setUp dei test
 * ver. 0.9 - modifiche, aggiunte e correzioni in seguito alle applicazioni pratiche, finita l'opera di commento e ripulitura del codice
 * ver. 1.0 - rilascio versione testata per Orbilio
 **********************************************
 */
package claudiosoft.bellerofonte;

import com.meterware.httpunit.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

/**
 * This class holds the main method and manage the start and the end of
 * execution, with related command line arguments
 */
class Web_test {

    /**
     * static elements used in the program
     */
    public static DataInputStream stdin = new DataInputStream(System.in);
    public static DataOutputStream stdout = new DataOutputStream(System.out);
// numbers of: all tests, passed tests, failed tests, general errors
    public static int[] testingResult = new int[3];

    /**
     * Fields
     */
    private Hdd_interface _hddInt = new Hdd_interface();
    private int _exitVal; // value to return to caller
    private String _version = "0.86"; // version of Bellerofonte

    /**
     *********************************************
     * Constructor
     */
    public Web_test() {
    }

    /**
     **********************************************
     * This method is invoked when on must make a new Macro_test from command
     * line directives. It create a new Macro_test. Then initialize its _options
     * reading from options file and add some Single_test to Macro_test reading
     * from another file. All Single_test added must be in a list of Single_test
     * available. If any errors aren't occured it start automatically the
     * execute of Macro_test and write the report on file or on _reports vector
     * of Macro_test, how specified by command line.
     *
     * IN: arguments of command line OUT: void
     */
    public void start(String args[]) {
        // these are the three needed parameters to determine a macroTest
        String options = "";
        String test = "";
        String howRep = "";
        Web_test.scroll(1);
        // first line displayed. Strings displayed in main aren't stored in report
        String date = giveTheDate();
        System.out.println("Bellerofonte: ver. " + _version + " --- " + date);
        Web_test.scroll(1);
        // check the number of arguments
        // the needed parameters aren't passed by command line
        if (args.length < 2) {
            Web_test.scroll(1);
            // options
            String[] fileList = _hddInt.readDirOptions();
            Web_test.scroll(1);
            System.out.println("* Write the name of options file that will be used from list below:");
            Web_test.scroll(1);
            options = Web_test.whatFile(fileList);
            Web_test.scroll(1);
            // test
            System.out.println("* Write the name of test file that will be used from list below:");
            Web_test.scroll(1);
            String[] fileList2 = _hddInt.readDirTest();
            test = Web_test.whatFile(fileList2);
            Web_test.scroll(1);
            // report and save
            System.out.println("* How must the report be treated ?");
            System.out.println("[1] \"saveToMacroTest\"");
            System.out.println("[2] \"compareToOld\"");
            System.out.println("[3] \"compareToOldAndSave\"");
            System.out.println("- nameReportInTXT");
            System.out.println("- null");
            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);
            try {
                howRep = in.readLine();
                Web_test.scroll(1);
                Web_test.scroll(1);
                // if the user have specified one choice
                if (!howRep.equals("")) {
                    // first choice
                    if (howRep.equals("1") || howRep.equalsIgnoreCase("savetomacrotest")) {
                        howRep = "saveToMacroTest";
                    } // second choice
                    else if (howRep.equals("2") || howRep.equalsIgnoreCase("comparetoold")) {
                        howRep = "compareToOld";
                    } // third choice
                    else if (howRep.equals("3") || howRep.equalsIgnoreCase("comparetooldandsave")) {
                        howRep = "compareToOldAndSave";
                    } // other: save on report txt
                    else {
                        if (howRep.length() > 4) {
                            String extension = howRep.substring(howRep.length() - 3, howRep.length());
                            extension = extension.toUpperCase();
                            if (!extension.equals("TXT")) {
                                howRep = howRep + ".txt";
                            }
                        } else {
                            howRep = howRep + ".txt";
                        }
                    }

                }
            } catch (IOException e) {
                System.out.println("There are some problems to read from stdin - program ended");
                System.exit(2);
            }
        } // there are command line paramters and assign those to variables
        else {
            options = args[0];
            test = args[1];
            if (args.length > 2) {
                howRep = args[2];
            }
        }
        //read from the file of tests's name
        Vector namesFromList = _hddInt.readFileListTest("TestList.txt");
        //create the Macro_test and pass it the name of Macro test file
        Macro_test Mtest = new Macro_test(test);
        //ask Hdd_interface to read options file which name is passed as first parameter
        Properties optionsFromFile = _hddInt.readFileOption(options);
        //ask Hdd_interface to open the Macro_tests file which name is passed as second parameter
        // extract extension
        String extension = test.substring(test.length() - 3, test.length());
        extension = extension.toUpperCase();
        // load from xml serialized object
        if (extension.equals("TST")) //check extension
        {
            // read from file XML the state of macro_test object
            Mtest = _hddInt.readFileTestXML(test);
            // set actual target url and file names (and other options)
            Mtest.setOptions(optionsFromFile);
        } // load from txt file
        else {
            //set target url and file names (and other options)
            Mtest.setOptions(optionsFromFile);
            //read from file txt strings that contain single_test + parameters
            Vector listST = _hddInt.readFileTestTXT(test);
            //for all single test to execute extract him with his parameters and create Single_test
            int count = 0;
            for (count = 0; count < listST.size(); count++) {
                // parse line by line
                Vector testInfo = parseTestStr((String) listST.elementAt(count));
                // create new Single test
                Single_test newTest = Mtest.newSingleTest(namesFromList, testInfo, count + 1);
                // if newTest <> null then a new Single_test object has been created and initialized
                // we can add him to our Macro_test and then go to next Single_test
                if (newTest != null) {
                    Mtest.addSingleTest(newTest);
                } else {
                    Web_test.scroll(1);
                    System.out.println("Some errors are occurred creating the Macro test");
                    Web_test.scroll(1);
                    System.out.println("Single_test named \"" + (String) testInfo.elementAt(0) + "\" not present");
                    System.out.println("or hasn't a correct number of parameters.");
                    // return the error in the array location
                    Web_test.addErrorState();
                }
                testInfo = null;
            }
        }
        // finally execute the Macro_test and get results
        int[] tmpRes = new int[3];
        tmpRes = Mtest.execute();
        testingResult[0] = tmpRes[0];
        testingResult[1] = tmpRes[1];
        testingResult[2] = testingResult[2] + tmpRes[2];
        // check if there are switch to manage _actualRep (else the report isn't save)
        if (!howRep.equals("")) {
            // the report must be saved on _report vector
            if (howRep.equals("saveToMacroTest")) {
                Mtest.saveActualReport();
                _hddInt.saveFileTestXML(Mtest);
            } // if test must be compared to old....
            else if (howRep.equals("compareToOld")) {
                Mtest.viewOldAndCompare();
            } // if test must be compared to old and saved
            else if (howRep.equals("compareToOldAndSave")) {
                Mtest.viewOldAndCompare();
                Mtest.saveActualReport();
                _hddInt.saveFileTestXML(Mtest);
            } // the report must be saved on external txt file
            else {
                _hddInt.saveFileReportTXT(Mtest.getActualReport(), howRep);
            }
        }
        // end of program...
        Web_test.scroll(2);
        date = giveTheDate();
        // write on screen the testing result and set exit value
        System.out.println("Result of tests in " + Mtest.getName() + ":");
        System.out.println("  * " + testingResult[0] + " tests PASSED;");
        System.out.println("  * " + testingResult[1] + " tests FAILED;");
        System.out.println("  * " + testingResult[2] + " GENERAL ERRORS;");
        Web_test.scroll(1);
        // only if there is an interactive console
        if (args.length < 2) {
            // wait a enter's pression when the program is terminated
            System.out.println("(press Enter) --- " + date);
            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);
            try {
                String choice = in.readLine();
            } catch (IOException e) {
            }
        }
        // determine the exit value
        _exitVal = 0;
        if (testingResult[1] != 0) {
            _exitVal = 1;
        }
        if (testingResult[2] != 0) {
            _exitVal = 2;
        }
    }

    /**
     *********************************************
     * This internal method return the value that the program must return of the
     * caller
     *
     * IN: void; OUT: int;
     */
    public int getExitVal() {
        return _exitVal;
    }

    /**
     *********************************************
     * This static method is referred by some other object that can generate an
     * error that is relevant for final exit value
     *
     * IN: void; OUT: void;
     */
    public static void addErrorState() {
        testingResult[2]++;
    }

    /**
     *********************************************
     * This internal method return the current date and hour.
     *
     * IN: void; OUT: string with the current date
     */
    protected static String giveTheDate() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.ITALY);
        String date = df.format(new Date());
        return date;
    }

    /**
     *********************************************
     * Giving a String that hold one line readed from a file test, this method
     * read every word and separe the name of single_test and their parameters.
     *
     * IN: string with the name of Single test and his parameters separed by ':'
     * OUT: vector with the same word each one separed.
     */
    public Vector parseTestStr(String line) {
        Vector testInfo = new Vector(); //hold name and parameters
        String word = "";
        int x = 0;
        int y = 0;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j) == '|') {
                y = j;
                if (x != 0) {
                    word = line.substring(x + 1, y);
                } else {
                    word = line.substring(x, y);
                }
                testInfo.addElement(word);
                x = y;
            }
        }
        return (testInfo);
    }

    /**
     **********************************************
     * This method draw textual (typeInterface = 0) or graphic (typeInterface =
     * 1) user interface
     *
     * -> NOT Used for now
     */
    public void drawInterface(int typeInterface) {
        switch (typeInterface) {
            case 0:
                scroll(4); // scroll the console screen
                System.out.println("1) Check the title");
                System.out.println("2) Read the page");
                System.out.println("3) jWebTest");
        }
    }

    /**
     ***********************************************
     * In this method is required a confirmation by user about some action. It
     * make the question provided by whatConfirm.
     *
     * -> NOT USED FOR NOW
     */
    public boolean askIf(String whatConfirm) {
        Web_test.scroll(3);
        String choice = "";
        while (!choice.equals("Y") && !choice.equals("N")) {
            System.out.println(whatConfirm + " (Y/N)");
            try {
                choice = Web_test.stdin.readLine();
                choice = choice.toUpperCase();
            } catch (IOException e) {
                System.out.println("There are some problems to read from stdin - program ended");
                System.exit(1);
            }
        }
        if (choice.equals("Y")) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     ***********************************************
     * In this method is asked to user what file options and file test
     * Bellerofonte will use.
     *
     * IN : Array with names of files stored related dir OUT: name choosed
     */
    public static String whatFile(String[] list) {
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);
        String choice = "";
        int min = 0;
        int max = list.length;
        // there are some files
        for (int i = 0; i < max; i++) {
            System.out.println(i + "- " + list[i]);
        }
        try {
            boolean ok = false;
            while (!ok) {
                choice = in.readLine();
                int num = Integer.parseInt(choice.trim());
                if (num >= min && num < max) {
                    choice = list[num];
                    ok = true;
                } else {
                    System.out.println("Choice out of range");
                    ok = false;
                }
            }
        } catch (NumberFormatException e) {
            //if isnt' a number pheraphs it is a name file
        } catch (IOException e) {
            System.out.println("There are some problems to read from stdin - program ended");
            Web_test.addErrorState();
        }
        if (choice.equals("")) {
            System.out.println("Impossible to proceed without a program file");
            System.exit(2);
        }
        return choice;
    }

    /**
     **********************************************
     * This is an auxiliary method for clear the screen from console. It want in
     * input the number of lines to scroll down.
     *
     * IN: number of line to scroll OUT: void
     */
    protected static void scroll(int lines) {
        int x;
        for (x = 0; x < lines; x++) {
            System.out.println(' ');
        }
    }

    /**
     **********************************************
     * Copy constructor
     */
    public Web_test(Web_test other) {
        if (this != other) {
            this._hddInt = other._hddInt;
        }
    }

    /**
     **********************************************
     * toString method
     */
    public String toString() {
        String sep = System.getProperty("line.separator");

        StringBuffer buffer = new StringBuffer();
        buffer.append(sep);
        buffer.append("stdin = ");
        buffer.append(stdin);
        buffer.append(sep);
        buffer.append("stdout = ");
        buffer.append(stdout);
        buffer.append(sep);
        buffer.append("_hddInt = ");
        buffer.append(_hddInt);
        buffer.append(sep);

        return buffer.toString();
    }

    /**
     **********************************************
     * Main of the program
     *
     * exit value 0 = good exit value 1 = some test are failed exit value 2 =
     * errors are occurred
     */
    public static void main(String args[]) {
        Web_test testSuite = new Web_test();
        try {
            testSuite.start(args);
        } catch (HttpNotFoundException e) {
            Web_test.scroll(1);
            System.out.println("Http not found exception");
            System.out.println("End of program");
            Web_test.scroll(2);
            e.printStackTrace();
            // general error: exit val surely equals to 2
            System.exit(2);
        } catch (ArrayIndexOutOfBoundsException e) {
            Web_test.scroll(1);
            System.out.println("Array Index Error, try to delete all empty line in file test.");
            System.out.println("End of program");
            Web_test.scroll(2);
            // general error: exit val surely equals to 2
            System.exit(2);
        } catch (Exception e) {
            Web_test.scroll(1);
            System.out.println("A generic error have occurred (read below) - End of program:");
            System.out.println("____________________________________________________________");
            Web_test.scroll(2);
            e.printStackTrace();
            // general error: exit val surely equals to 2
            System.exit(2);
        }
        System.exit(testSuite.getExitVal());
    }
}// end of class
