//******************************************************************************
// Project: CaetanoSoft.BricksCollectorHelper
// URL:     https://github.com/caetanator/JavaBricksCollectorHelper/
// File:    BricksCollectorHelper.java
//
// Description:
//          This class implements a Java Swing GUI application that can help 
//          collectors of Self-Locking Building/Automatic Binding plastic Bricks 
//          to control their spending on the hobby and complete the sets they 
//          collect.
//
// Copyright:
//          © 2026 José Caetano Silva / CaetanoSoft. All rights reserved.
//
// License:
//          This file is part of CaetanoSoft.BricksCollectorHelper.
//
//          CaetanoSoft.BricksCollectorHelper is free software: you can 
//          redistribute it and/or modify it under the terms of the GNU General 
//          Public License as published by the Free Software Foundation, either 
//          version 3 of the License, or (at your option) any later version.
//
//          CaetanoSoft.BricksCollectorHelper is distributed in the hope that it 
//          will be useful, but WITHOUT ANY WARRANTY; without even the implied  
//          warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See 
//          the GNU General Public License for more details.
//
//          You should have received a copy of the GNU General Public License
//          along with CaetanoSoft.BricksCollectorHelper. If not, see 
//          <https://www.gnu.org/licenses/gpl-3.0.html>.
//******************************************************************************


package CaetanoSoft.BricksCollectorHelper;

import java.util.Properties;

import CaetanoSoft.Utilities.String.StringUtils;
import CaetanoSoft.Utilities.Path.PathUtils;
import javax.swing.UIManager;

/**
 * This class implements a Java Swing GUI application that can help collectors
 * of Self-Locking Building/Automatic Binding plastic Bricks to control their
 * spending on the hobby and complete the sets they collect.
 *
 * @author José Caetano Silva
 * @version 1.00.0000, 2026-03-01
 * @since 1.00
 */
public class BricksCollectorHelper {
    // Application data
    /**
     * A string with the application's name.
     */
    private static final String APP_NAME = "Bricks Collector Helper";
    /**
     * A string with the application's version.
     */
    private static final String APP_VERSION = "1.00.0000";
    /**
     * Complete path to configuration file.
     */
    private static String m_strConfigFile = "BricksCollectorHelper.properties";

    /**
     * Exit error codes
     */
    public enum ExitErrorCodes {
        /**
         * The application terminated normaly, without errors.
         */
        EXIT_OK(0),
        /**
         * The application terminated abnormaly, with an unhandled Java
         * exception.
         */
        EXIT_ERROR_EXCEPTION(-1),
        /**
         * The application terminated abnormaly, because the number of
         * parameters is wrong.
         */
        EXIT_ERROR_BAD_NUMBER_PARAMETERS(1),
        /**
         * The application terminated abnormaly, because one parameter is wrong.
         */
        EXIT_ERROR_BAD_PARAMETER(2),
        /**
         * The application terminated abnormaly, because one parameter is
         * duplicated.
         */
        EXIT_ERROR_PARAMETER_DUPLICATED(3),
        /**
         * The application terminated abnormaly, because the configuration file
         * was not found or is invalid.
         */
        EXIT_ERROR_BAD_CONFIG_FILE(4),
        /**
         * The application terminated abnormaly, because of invalid or
         * non-existent input file.
         */
        EXIT_ERROR_BAD_INPUT_FILE(5),
        /**
         * The application terminated abnormaly, because of invalid or
         * non-existent output file.
         */
        EXIT_ERROR_BAD_OUTPUT_FILE(6),
        /**
         * The application terminated abnormaly, because of invalid database
         * connection or operation.
         */
        EXIT_ERROR_BAD_DB_OPERATION(7),
        /**
         * The application terminated abnormaly, because of invalid server
         * IP/Name.
         */
        EXIT_ERROR_BAD_SERVER_IP(8),
        /**
         * The application terminated abnormaly, because of invalid TCP/UDP
         * service port.
         */
        EXIT_ERROR_BAD_PORT_NUMBER(9);

        private int errorCode;

        /**
         * Constructer.
         * 
         * @param errorCode The integer value o the exit enumeration.
         */
        ExitErrorCodes(int errorCode) {
            this.errorCode = errorCode;
        }

        /**
         * 
         * @return The integer value o the exit enumeration.
         */
        public int getErrorCode() {
            return this.errorCode;
        }
    }

    /**
     * Prints the use of the command, with the respective parameters.
     *
     * @since 1.00
     */
    public static void printUsage() {
        System.out.println("");
        System.out.println("" + APP_NAME + " v" + APP_VERSION);
        System.out.println("(c) 2026 José Caetano Silva / CaetanoSoft");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("\tjava -jar BricksCollectorHelper.jar -h");
        System.out.println("\t\t(Prints this help screen and exit)");
        System.out.println("\tjava -jar BricksCollectorHelper.jar -di");
        System.out.println("\t\t(Prints Java VM and configuration information and exit)");
        System.out.println("\tjava -jar BricksCollectorHelper.jar");
        System.out.println("\t\t(Runs the application normally)");
        System.out.println("");
        System.out.println("Returned Error Codes:");
        System.out.println("\t  0: OK");
        System.out.println("\t -1: Error: Java Exception");
        System.out.println("\t  1: Error: Invalid number of parameters");
        System.out.println("\t  2: Error: Invalid parameter");
        System.out.println("\t  3: Error: Duplicated parameter");
        System.out.println("\t  4: Error: Configuration file not found or invalid");
        System.out.println("\t  5: Error: Invalid or non-existent input file");
        System.out.println("\t  6: Error: Invalid or non-existent output file");
        System.out.println("\t  7: Error: Invalid database connection or operation");
        System.out.println("\t  8: Error: Invalid server IP/Name");
        System.out.println("\t  9: Error: Invalid TCP/UDP service port");
        System.out.println("");
    }

    /**
     * Prints debug information.
     *
     * @since 1.00
     */
    public static void printDebugInfo() {
        System.out.println("");
        System.out.println("" + APP_NAME + " v" + APP_VERSION);
        System.out.println("(c) 2026 José Caetano Silva / CaetanoSoft");
        System.out.println("");
        System.out.println("Java VM Information:");
        System.out.println("\tVendor: " + System.getProperty("java.vendor"));
        System.out.println("\tVersion: " + System.getProperty("java.version"));
        System.out.println("\tSpecification Version: " + System.getProperty("java.specification.version"));
        System.out.println("\tVendor URL: \"" + System.getProperty("java.vendor.url") + "\"");
        System.out.println("\tBug Report URL: \"" + System.getProperty("java.vendor.url.bug") + "\"");
        System.out.println("\tInstallation Directory: \"" + System.getProperty("java.home") + "\"");
        System.out.println("\tLibrary Directory: \"" + System.getProperty("sun.boot.library.path") + "\"");
        System.out.println("\tClass Path: \"" + System.getProperty("java.class.path") + "\"");
        System.out.println("\tJNU String Encoding: " + System.getProperty("sun.jnu.encoding"));
        System.out.println("Java Application Information:");
        System.out.println("\tCommand Line: \"" + System.getProperty("sun.java.command") + "\"");
        System.out.println("\tWorking Directory: \"" + System.getProperty("user.dir") + "\"");
        System.out.println("\tTemp Directory: \"" + System.getProperty("java.io.tmpdir") + "\"");
        System.out.println("\tConfiguration File: \"" + m_strConfigFile + "\"");
        System.out.println("OS Information:");
        System.out.println("\tName: " + System.getProperty("os.name"));
        System.out.println("\tVersion: " + System.getProperty("os.version"));
        System.out.println("\tArchitecture: " + System.getProperty("os.arch"));
        System.out.println("\tArchitecture Data Model: " + System.getProperty("sun.arch.data.model"));
        System.out.println("\tCPU Endian: " + System.getProperty("sun.cpu.endian"));
        System.out.println("\tLine Separator: '" + StringUtils.escapeString(System.getProperty("line.separator")) + "'");
        System.out.println("\tDirectory Separator: '" + System.getProperty("file.separator") + "'");
        System.out.println("\tPath Separator: '" + System.getProperty("path.separator") + "'");
        System.out.println("\tText File Encoding: " + System.getProperty("file.encoding"));
        System.out.println("\tStdIn Encoding: " + System.getProperty("stdin.encoding"));
        System.out.println("\tStdOut Encoding: " + System.getProperty("stdout.encoding"));
        System.out.println("\tStdErr Encoding: " + System.getProperty("stderr.encoding"));
        System.out.println("User Information:");
        System.out.println("\tName: " + System.getProperty("user.name"));
        System.out.println("\tLanguage: " + System.getProperty("user.language"));
        System.out.println("\tCountry: " + System.getProperty("user.country"));
        System.out.println("\tHome Directory: \"" + System.getProperty("user.home") + "\"");
        System.out.println("\tDesktop Directory: \"" + PathUtils.getUserDesktopPath() + "\"");
        System.out.println("\tDocuments Directory: \"" + PathUtils.getUserDocumentsPath() + "\"");
        System.out.println("\tPictures Directory: \"" + PathUtils.getUserPicturesPath() + "\"");
        System.out.println("\tVideos Directory: \"" + PathUtils.getUserVideosPath() + "\"");
        System.out.println("\tMusic Directory: \"" + PathUtils.getUserMusicPath() + "\"");
        System.out.println("\tDownloads Directory: \"" + PathUtils.getUserDownloadsPath() + "\"");
        System.out.println("");
        
        System.out.println("-----------------------------------------");
        System.out.println("Java VM Properties:");
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ": '" + v + "'"));
        System.out.println("");
        
        System.out.println("-----------------------------------------");
        System.out.println("Supported Java Swing Look&Feel:");
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks) {
            System.out.println("  " + look.getClassName());
        }
        System.out.println("Default Java Swing Look&Feel: " + UIManager.getSystemLookAndFeelClassName());
        System.out.println("");
    }

    /**
     * Processes command-line parameters.
     *
     * @param arrStrArgs - The command line parameters/arguments.
     */
    public static void parseArguments(String[] arrStrArgs) {
        // Check if the number of parameters is valid
        if (arrStrArgs.length == 0) {
            return;
        }

        // Processes the parameters
        int paramCount = 0;
        while (paramCount < arrStrArgs.length) {
            switch (arrStrArgs[paramCount]) {
                case "-h":
                    // Parameter -h : show help
                    // Prints the command usage
                    printUsage();
                    //doExit(ExitErrorCodes.EXIT_OK.getErrorCode(), "");
                    break;
                case "-di":
                    // Parameter -di : show debug information
                    // Prints Java VM and configuration information and exit
                    printDebugInfo();
                    //doExit(ExitErrorCodes.EXIT_OK.getErrorCode(), "");
                    break;
                default:
                    // Error, Unknown parameter
                    //doExit(ExitErrorCodes.EXIT_ERROR_BAD_PARAMETER.getErrorCode(), "Error: Invalid parameter \"" + arrStrArgs[paramCount] + "\"!");
                    break;
            }

            ++paramCount;
        }
    }

    /**
     * Runs the BricksCollectorHelper application.
     *
     * @param arrStrArgs The command line parameters/arguments
     */
    public static void main(String[] arrStrArgs) {
        System.out.println("Hello World from \"" + APP_NAME + "\"!");
        System.out.println("Parsing the command line parameters...");
        parseArguments(arrStrArgs);
    }
}
