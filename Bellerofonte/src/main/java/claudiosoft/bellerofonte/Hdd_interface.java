package claudiosoft.bellerofonte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.Properties;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class manage the operations between other classes and the hard disk. It
 * implements various load/save from/to file of the data.
 */
class Hdd_interface {

    /**
     * Fields
     *
     * These variables holds the path of the directory where are the work files.
     * They should remain the same in all version of Bellerofonte
     */
    private String _fileDirRoot = ".." + File.separatorChar + "webTestFiles" + File.separatorChar;
    private String _dirProgram = "program" + File.separatorChar;
    private String _dirOptions = "options" + File.separatorChar;
    private String _dirTestTXT = "testTXT" + File.separatorChar;
    private String _dirTestXML = "testXML" + File.separatorChar;
    private String _dirReports = "reports" + File.separatorChar;

    /**
     ***********************************************
     * Constructor
     */
    public Hdd_interface() {
        // CODE FOR MANAGE EXECUTION FROM IDE PROGRAM (JCREATOR):
        // NOT ESSENTIAL FOR COMMAND LINE EXECUTIONS
        // get current dir
        File tmp = new File("./");
        String[] cont = tmp.list();
        for (int i = 0; i < cont.length; i++) {
            if (cont[i].equalsIgnoreCase("webtestfiles")) {
                _fileDirRoot = "webtestfiles" + File.separatorChar;
            }
        }
    }

    /**
     ***********************************************
     * This method search in _fileDirRoot if there is an option file properties
     * with name "nameF" and after read its content it returns its info.
     *
     * IN: name of file options OUT: values readed
     */
    public Properties readFileOption(String nameF) {
        Properties props = new Properties();
        try {
            File tmp = new File(_fileDirRoot + _dirOptions + nameF);
            if (tmp.exists()) {
                FileInputStream fis = new FileInputStream(tmp);
                DataInputStream dfis = new DataInputStream(fis);
                props.load(dfis);
                fis.close();
                dfis.close();
            } else {
                System.out.println("The file options " + nameF + " not exist in the directory");
                System.exit(2);
            }
        } catch (IOException e) {
            System.out.println("An error occurred on open file option \"" + nameF + "\"");
            System.exit(2);
        }
        return props;
    }

    /**
     ***********************************************
     * In this method on provide to charge a vector with the lines read from
     * file of test TXT.
     *
     * IN: name of file .txt OUT: vector with the values readed
     */
    public Vector readFileTestTXT(String nameF) {
        Vector textRead = new Vector();
        File tmp = new File(_fileDirRoot + _dirTestTXT + nameF);
        if (!tmp.canRead() || !tmp.exists()) {
            System.out.println("Error on open file \"" + nameF + "\"");
            System.exit(2);
        } else {
            try {
                FileInputStream testList = new FileInputStream(tmp);
                DataInputStream testListIn = new DataInputStream(testList);
                String testName = testListIn.readLine();
                while (testName != null) {
                    textRead.addElement(testName);
                    testName = testListIn.readLine();
                }
                testListIn.close();
                testList.close();
            } catch (IOException e) {
                System.out.println("I/O error on reading \"" + nameF + "\"");
                Web_test.addErrorState();
            }
        }
        return (textRead);
    }

    /**
     ***********************************************
     * In this method on provide to charge a vector with the lines read from
     * file of tests available.
     *
     * IN: name of file testList.txt OUT: vector with the values readed
     */
    public Vector readFileListTest(String nameF) {
        Vector textRead = new Vector();
        File tmp = new File(_fileDirRoot + _dirProgram + nameF);
        if (!tmp.canRead() || !tmp.exists()) {
            System.out.println("Error on open file \"" + nameF + "\"");
            System.exit(2);
        } else {
            try {
                FileInputStream testList = new FileInputStream(tmp);
                DataInputStream testListIn = new DataInputStream(testList);
                String testName = testListIn.readLine();
                while (testName != null) {
                    textRead.addElement(testName);
                    testName = testListIn.readLine();
                }
                testListIn.close();
                testList.close();
            } catch (IOException e) {
                System.out.println("I/O error on reading \"" + nameF + "\"");
                Web_test.addErrorState();
            }
        }
        return (textRead);
    }

    /**
     ***********************************************
     * This method save to a TXT file the actual report
     *
     * IN: name of file .txt to save OUT: void
     */
    public void saveFileReportTXT(Report rep, String nameF) {
        File tmp = new File(_fileDirRoot + _dirReports + nameF);
        try {
            tmp.createNewFile();
            if (!tmp.canWrite()) {
                System.out.println("Error on create new file \"" + nameF + "\"");
                Web_test.addErrorState();
            } else {
                FileWriter repFile = new FileWriter(tmp);
                PrintWriter pw = new PrintWriter(repFile);
                int count = 0;
                String line = rep.getSingleLine(count);
                while (line != null) {
                    pw.println(line);
                    count++;
                    line = rep.getSingleLine(count);
                }
                repFile.close();
                pw.close();
            }
        } catch (IOException e) {
            System.out.println("I/O error on saving \"" + nameF + "\"");
            Web_test.addErrorState();
        }
    }

    /**
     ***********************************************
     * This method provide to save the actual report to an XML file
     *
     * IN: macro test to save in a .tst file OUT: void
     */
    public void saveFileTestXML(Macro_test mtest) {
        String nameFile = mtest.getName();
        nameFile = nameFile.substring(0, nameFile.length() - 3) + "tst";

        File tmp = new File(_fileDirRoot + _dirTestXML + nameFile);
        try {
            tmp.createNewFile();
            if (!tmp.canWrite()) {
                System.out.println("Error on create new file \"" + nameFile + "\"");
                Web_test.addErrorState();
            } else {
                FileOutputStream fos = new FileOutputStream(tmp);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(mtest);
                fos.close();
                oos.close();
            }
        } catch (IOException e) {
            System.out.println("Error on saving new tst object");
            Web_test.addErrorState();
        }
    }

    /**
     ***********************************************
     * This method read a Macro test from an XML file
     *
     * IN: name of file .tst to read OUT: macro test readed
     */
    public Macro_test readFileTestXML(String nameFile) {
        File tmp = new File(_fileDirRoot + _dirTestXML + nameFile);
        Macro_test Mtest = null;
        try {
            FileInputStream fis = new FileInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Mtest = (Macro_test) ois.readObject();
            fis.close();
            ois.close();
        } catch (IOException e) {
            System.out.println("Error on open \"" + nameFile + "\"");
            System.exit(2);
        } catch (ClassNotFoundException e) {
            System.out.println("Error reading from tst object");
            System.exit(2);
        }
        return Mtest;
    }

    /**
     ***********************************************
     * This method read the file in the test txt dir
     *
     * IN: void OUT: vector with the names of files in test dirs
     */
    public String[] readDirTest() {
        // file list from txt dir
        String[] tmpRis = null;
        // file list from xml dir
        String[] tmpRis2 = null;
        // merge from txt and xml
        String[] ris = null;
        File tmp = new File(_fileDirRoot + _dirTestTXT);
        File tmp2 = new File(_fileDirRoot + _dirTestXML);
        if (!tmp.exists() || !tmp2.exists()) {
            System.out.println("Tests directory not found: verify the installation");
            System.exit(2);
        }
        try {
            tmpRis = tmp.list();
            tmpRis2 = tmp2.list();
            int len = tmpRis.length + tmpRis2.length;
            ris = new String[len];
            int i = 0;
            // add txt file name array
            for (i = 0; i < tmpRis.length; i++) {
                ris[i] = tmpRis[i];
            }
            // add tst file name array
            int j = 0;
            for (j = 0; j < tmpRis2.length; j++) {
                ris[i + j] = tmpRis2[j];
            }
        } catch (Exception e) {
            System.out.println("Impossible to read test directory: verify the installation");
            System.exit(2);
        }
        if (ris.length != 0) {
            return ris;
        } else {
            return ris = new String[0];
        }
    }

    /**
     ***********************************************
     * This method read the file in the options dir
     *
     * IN: void OUT: vector with the names of files that are in option dir
     */
    public String[] readDirOptions() {
        String[] ris = null;
        File tmp = new File(_fileDirRoot + _dirOptions);
        if (tmp.exists()) {
            try {
                ris = tmp.list();
            } catch (Exception e) {
                System.out.println("Impossible to read options directory: verify the installation");
                System.exit(2);
            }
        } else {
            System.out.println("Impossible to read options directory: verify the installation");
            System.exit(2);
        }
        if (ris.length != 0) {
            return ris;
        } else {
            return ris = new String[0];
        }
    }

    /**
     ***********************************************
     * Copy constructor
     */
    public Hdd_interface(Hdd_interface other) {
        if (this != other) {
            this._fileDirRoot = other._fileDirRoot;
            this._dirProgram = other._dirProgram;
            this._dirOptions = other._dirOptions;
            this._dirTestTXT = other._dirTestTXT;
            this._dirTestXML = other._dirTestXML;
            this._dirReports = other._dirReports;
        }
    }

    /**
     ***********************************************
     * toString method
     */
    public String toString() {
        String sep = System.getProperty("line.separator");

        StringBuffer buffer = new StringBuffer();
        buffer.append(sep);
        buffer.append("_fileDirRoot = ");
        buffer.append(_fileDirRoot);
        buffer.append(sep);
        buffer.append("_dirProgram = ");
        buffer.append(_dirProgram);
        buffer.append(sep);
        buffer.append("_dirOptions = ");
        buffer.append(_dirOptions);
        buffer.append(sep);
        buffer.append("_dirTestTXT = ");
        buffer.append(_dirTestTXT);
        buffer.append(sep);
        buffer.append("_dirTestXML = ");
        buffer.append(_dirTestXML);
        buffer.append(sep);
        buffer.append("_dirReports = ");
        buffer.append(_dirReports);
        buffer.append(sep);

        return buffer.toString();
    }
} // end of class

