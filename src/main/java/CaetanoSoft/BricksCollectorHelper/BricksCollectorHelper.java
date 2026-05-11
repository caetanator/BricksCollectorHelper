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

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Properties;

import javax.swing.UIManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import CaetanoSoft.Utilities.Internationalization.InternationalizationUtils;
import CaetanoSoft.Utilities.String.StringUtils;
import CaetanoSoft.Utilities.Path.PathUtils;
import CaetanoSoft.Utilities.UI.SplashScreenManager.SplashScreenManager;


/**
 * This class implements a Java Swing GUI application that can help collectors
 * of Self-Locking Building/Automatic Binding plastic Bricks to control their
 * spending on the hobby and complete the sets they collect.
 *
 * <p>
 * </p>
 * Usage:
 * <ul style="list-style-type:none;">
 *   <li>java -jar BricksCollectorHelper.jar [-h]</li>
 *   <li>&nbsp</li>
 *   <li>-h: Prints this help screen and exit;</li>
 *   <li>-di: Prints the debug information and exit.</li>
 * </ul>
 * <p>
 * </p>
 * Returned Error Codes:
 * <ul>
 *   <li> 0: OK;</li>
 *   <li> 1: Error: Invalid number of parameters<;/li>
 *   <li> 2: Error: Invalid parameter;</li>
 *   <li> 3: Error: Duplicated parameter;</li>
 *   <li> 4: Error: Configuration file not found or invalid;</li>
 *   <li> 5: Error: Invalid or non-existent input file;</li>
 *   <li> 6: Error: Invalid or non-existent output file;</li>
 *   <li> 7: Error: Invalid database connection or operation;</li>
 *   <li> 8: Error: Invalid server IP/Name;</li>
 *   <li> 9: Error: Invalid TCP/UDP service port;</li>
 *   <li>-1: Error: Unhandled Java exception.</li>
 * </ul>
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
   * Exit error codes.
   */
  public enum ExitErrorCodes {
    /**
     * The application terminated normaly, without errors.
     */
    EXIT_OK(0),
    /**
     * The application terminated abnormaly, with an unhandled Java exception.
     */
    EXIT_ERROR_EXCEPTION(-1),
    /**
     * The application terminated abnormaly, because the number of parameters is
     * wrong.
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
     * The application terminated abnormaly, because the configuration file was
     * not found or is invalid.
     */
    EXIT_ERROR_BAD_CONFIG_FILE(4),
    /**
     * The application terminated abnormaly, because of invalid or non-existent
     * input file.
     */
    EXIT_ERROR_BAD_INPUT_FILE(5),
    /**
     * The application terminated abnormaly, because of invalid or non-existent
     * output file.
     */
    EXIT_ERROR_BAD_OUTPUT_FILE(6),
    /**
     * The application terminated abnormaly, because of invalid database
     * connection or operation.
     */
    EXIT_ERROR_BAD_DB_OPERATION(7),
    /**
     * The application terminated abnormaly, because of invalid server IP/Name.
     */
    EXIT_ERROR_BAD_SERVER_IP(8),
    /**
     * The application terminated abnormaly, because of invalid TCP/UDP service
     * port.
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
   * Debug logging levels flags: No debugging.
   */
  private static final int DEBUG_LEVEL_NONE = 0x00000000;
  /**
   * Debug logging levels flags: Only errors.
   */
  private static final int DEBUG_LEVEL_ERROR = 0x00000001;
  /**
   * Debug logging levels flags: Only warnings.
   */
  private static final int DEBUG_LEVEL_WARNING = 0x00000002;
  /**
   * Debug logging levels flags: Only debug information.
   */
  private static final int DEBUG_LEVEL_INFO = 0x00000004;
  /**
   * Debug logging levels flags: All levels.
   */
  private static final int DEBUG_LEVEL_ALL = 0xFFFFFFFF;
  
  /**
   * Application level of debugging (DEBUG_LEVEL_* values ORed):
   * <ul>
   *   <li> 0: Off</li>
   *   <li> 1: Errors (default value)</li>
   *   <li> 2: Warnings</li>
   *   <li> 3: Errors & Warnings</li>
   *   <li> 4: Information</li>
   *   <li> 5: Errors & Information</li>
   *   <li> 6: Warnings & Information</li>
   *   <li> 7: Errors & Warnings & Information</li>
   *   <li>-1: All</li>
   * </ul>
   * 
   * @see DEBUG_LEVEL_ALL, DEBUG_LEVEL_NONE, DEBUG_LEVEL_ERROR, DEBUG_LEVEL_WARNING, DEBUG_LEVEL_INFO
   */
  private static int m_fLogLevel = DEBUG_LEVEL_ERROR;

  /**
   * Logger object to output errors, warnings, debugging information.
   */
  private static Logger m_objLogger = null;
  
  /**
   * Splash Screen manager object.
   */
  private static final SplashScreenManager ssManager = SplashScreenManager.getInstance();
    
  // Database connection information
  /**
   * JDBC Driver class name.
   */
  private static String m_strJdbcDriver = "";
  /**
   * JDBC connection URL.
   */
  private static String m_strJdbcURL = "";
  /**
   * JDBC connection user name.
   */
  private static String m_strJdbcUsername = "";
  /**
   * JDBC connection user password.
   */
  private static String m_strJdbcPassword = "";
  /**
   * JDBC connection timeout (in milliseconds).
   */
  private static int m_nConnectionTimeout = 5000;
  /**
   * JDBC connection object to be used.
   */
  private static Connection m_dbConnection = null;
  
  /**
   * HTTP/HTTPS connection timeout (in milliseconds).
   */
  private static int m_iHttpTimeout = 2000;
    
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
    System.out.println("\t  1: Error: Invalid number of parameters");
    System.out.println("\t  2: Error: Invalid parameter");
    System.out.println("\t  3: Error: Duplicated parameter");
    System.out.println("\t  4: Error: Configuration file not found or invalid");
    System.out.println("\t  5: Error: Invalid or non-existent input file");
    System.out.println("\t  6: Error: Invalid or non-existent output file");
    System.out.println("\t  7: Error: Invalid database connection or operation");
    System.out.println("\t  8: Error: Invalid server IP/Name");
    System.out.println("\t  9: Error: Invalid TCP/UDP service port");
    System.out.println("\t -1: Error: Java Exception");
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
    System.out.println("\tJNU String Encoding: " + System.getProperty("sun.jnu.encoding"));
    System.out.println("Java Application Information:");
    System.out.println("\tCommand Line: \"" + System.getProperty("sun.java.command") + "\"");
    System.out.println("\tClass Path: \"" + System.getProperty("java.class.path") + "\"");
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
    properties.forEach((k, v) -> System.out.println(k + ": '" + 
                                      ((k=="line.separator") ? StringUtils.escapeString(v.toString()) : v) + 
                                      "'"));
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
   * Exit the Java application with an error code and message sended to
   * <i>stderr</i> or the logging file.
   *
   * @since 1.1
   * @param iExitCode Error code
   * @param strMessage Error mensage
   */
  private static void doExit(int iExitCode, String strMessage) {
    // Close the Splash Screen
    if (ssManager != null) {
      ssManager.close();
    }

    if ((m_fLogLevel & (DEBUG_LEVEL_INFO | DEBUG_LEVEL_WARNING | DEBUG_LEVEL_ERROR)) != 0) {
      if (m_objLogger != null) {
        if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
          m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        m_objLogger.severe("" + strMessage);
        m_objLogger.severe("Ended with error " + iExitCode + "!");
      } else {
        System.err.println("BricksCollectorHelper: Ended with error " + iExitCode + ", " + strMessage + "!");
      }
    }

    System.exit(iExitCode);
  }
  
  /**
   * Processes command-line parameters.
   *
   * @param arrStrArgs - The command line parameters/arguments.
   */
  public static void parseArguments(String[] arrStrArgs) {
    // Log info entering method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
    
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
          // Close the Splash Screen
          if (ssManager != null) {
            ssManager.close();
          }
          // Prints the command usage
          printUsage();
          doExit(ExitErrorCodes.EXIT_OK.getErrorCode(), "");
          break;
        case "-di":
          // Parameter -di : show debug information
          // Close the Splash Screen
          if (ssManager != null) {
            ssManager.close();
          }
          // Prints Java VM and configuration information and exit
          printDebugInfo();
          doExit(ExitErrorCodes.EXIT_OK.getErrorCode(), "");
          break;
        default:
          // Error, Unknown parameter
          //doExit(ExitErrorCodes.EXIT_ERROR_BAD_PARAMETER.getErrorCode(), "Error: Invalid parameter \"" + arrStrArgs[paramCount] + "\"!");
          break;
      }

      ++paramCount;
    }
    
    // Log info exiting method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.exiting(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
  }

  /**
   * Reads the program's configuration from the default file.
   *
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception
   * @since 1.0
   * @see readConfig(String)
   */
  private static void readConfig() throws FileNotFoundException, IOException, Exception {
    // Log info entering method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }

    String strAppPath = PathUtils.getApplicationPath(new BricksCollectorHelper());
    m_strConfigFile = strAppPath + File.separator + m_strConfigFile;
    try {
      File fileConf = new File(m_strConfigFile);
      if (fileConf.exists()) {
        fileConf = null;
        readConfig(m_strConfigFile);
      } else {
        doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), "Error: Default configuration file \"" + m_strConfigFile + "\" not found!");
      }
    } catch (FileNotFoundException ex) {
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), "Error: Default configuration file \"" + m_strConfigFile + "\" not found!");
      throw ex;
    } catch (IOException ex) {
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), "Error: Invalid default configuration file \"" + m_strConfigFile + "\"!");
      throw ex;
    } catch (Exception ex) {
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), "Error: Invalid default configuration file \"" + m_strConfigFile + "\"!");
      throw ex;
    }

    // Log info exiting method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.exiting(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
  }

  /**
   * Reads the program's configuration file.
   *
   * @param strConfigFile Configuration file to be read
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception
   * @since 1.0
   */
  private static void readConfig(String strConfigFile) throws FileNotFoundException, IOException, Exception {
    // Log info entering method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
    
    // Load properties from file
    Properties properties = new Properties();
    try {
      File fileProperties = new File(strConfigFile);
      synchronized (fileProperties) {
        FileInputStream inStream = new FileInputStream(fileProperties);
        properties.load(new InputStreamReader(inStream, "UTF-8"));
        inStream.close();
      }

      String debugLevel = properties.getProperty("DEBUG_LEVEL", String.valueOf(m_fLogLevel));
      if ((debugLevel != null) && !debugLevel.trim().isEmpty()) {
        try {
          m_fLogLevel = Integer.parseInt(debugLevel);
        }
        catch (NumberFormatException ex) {
          m_fLogLevel = DEBUG_LEVEL_ERROR;
          // Log warning
          if (m_objLogger != null) {
            if ((m_fLogLevel & DEBUG_LEVEL_WARNING) != 0) {
              m_objLogger.log(Level.WARNING, "Invalid Property \"DEBUG_LEVEL\" = " + debugLevel);
            }
          }
        }
      }

      String jdbcDriver = properties.getProperty("JDBC_DRIVER", m_strJdbcDriver);
      if (jdbcDriver != null) {
        m_strJdbcDriver = jdbcDriver.trim();
      }

      String jdbcURL = properties.getProperty("JDBC_URL", m_strJdbcURL);
      if (jdbcURL != null) {
        m_strJdbcURL = jdbcURL.trim();
      }

      String jdbcUsername = properties.getProperty("JDBC_USERNAME", m_strJdbcUsername);
      if (jdbcUsername != null) {
        m_strJdbcUsername = jdbcUsername.trim();
      }

      String jdbcPassword = properties.getProperty("JDBC_PASSWORD", m_strJdbcPassword);
      if (jdbcPassword != null) {
        m_strJdbcPassword = jdbcPassword.trim();
      }

      String connectionTimeoutInMilliseconds = properties.getProperty("JDBC_CONNECT_TIMEOUT", String.valueOf(m_nConnectionTimeout));
      if ((connectionTimeoutInMilliseconds != null) && !(connectionTimeoutInMilliseconds.trim().isEmpty())) {
        connectionTimeoutInMilliseconds = connectionTimeoutInMilliseconds.trim();
        try {
          m_nConnectionTimeout = Integer.parseInt(connectionTimeoutInMilliseconds);
          if ((m_nConnectionTimeout < 1000) || (m_nConnectionTimeout > 65535)) {
            m_nConnectionTimeout = 5000;
            // Log warning
            if (m_objLogger != null) {
              if ((m_fLogLevel & DEBUG_LEVEL_WARNING) != 0) {
                m_objLogger.log(Level.WARNING, 
                  "Invalid Property \"JDBC_CONNECT_TIMEOUT\" = " + connectionTimeoutInMilliseconds +
                  System.getProperty("line.separator", "\n") + "Interval: [1000; 65535]");
              }
            }
          }
        }
        catch (NumberFormatException ex) {
          m_nConnectionTimeout = 5000;
          // Log warning
          if (m_objLogger != null) {
            if ((m_fLogLevel & DEBUG_LEVEL_WARNING) != 0) {
              m_objLogger.log(Level.WARNING, 
                  "Invalid Property \"JDBC_CONNECT_TIMEOUT\" = " + connectionTimeoutInMilliseconds +
                  System.getProperty("line.separator", "\n") + "Interval: [1000; 65535]");
            }
          }
        }
      }
      
      connectionTimeoutInMilliseconds = properties.getProperty("HTTP_CONNECT_TIMEOUT", String.valueOf(m_iHttpTimeout));
      if ((connectionTimeoutInMilliseconds != null) && !(connectionTimeoutInMilliseconds.trim().isEmpty())) {
        connectionTimeoutInMilliseconds = connectionTimeoutInMilliseconds.trim();
        try {
          m_iHttpTimeout = Integer.parseInt(connectionTimeoutInMilliseconds);
          if ((m_iHttpTimeout < 1000) || (m_iHttpTimeout > 65535)) {
            m_iHttpTimeout = 2000;
            // Log warning
            if (m_objLogger != null) {
              if ((m_fLogLevel & DEBUG_LEVEL_WARNING) != 0) {
                m_objLogger.log(Level.WARNING, 
                  "Invalid Property \"HTTP_CONNECT_TIMEOUT\" = " + connectionTimeoutInMilliseconds +
                  System.getProperty("line.separator", "\n") + "Interval: [1000; 65535]");
              }
            }
          }
        }
        catch (NumberFormatException ex) {
          m_iHttpTimeout = 2000;
          // Log warning
          if (m_objLogger != null) {
            if ((m_fLogLevel & DEBUG_LEVEL_WARNING) != 0) {
              m_objLogger.log(Level.WARNING, 
                  "Invalid Property \"HTTP_CONNECT_TIMEOUT\" = " + connectionTimeoutInMilliseconds +
                  System.getProperty("line.separator", "\n") + "Interval: [1000; 65535]");
            }
          }
        }
      }
    }
    catch (FileNotFoundException ex) {
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), 
            "Error: Configuration file \"" + strConfigFile + "\" not found!" + 
            System.getProperty("line.separator", "\n") + "Exception: " + ex.getMessage());
    }
    catch (IOException ex) {
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), 
            "Error: Invalid configuration file \"" + strConfigFile + "\"!" + 
            System.getProperty("line.separator", "\n") + "Exception: " + ex.getMessage());
    }
    catch (Exception ex) {
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_CONFIG_FILE.getErrorCode(), "Error: Invalid configuration file \"" + strConfigFile + "\"!");
    }
    
    // Log info exiting method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.exiting(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
  }

  /**
   * Connects to the database.
   */
  private static void conectToDatabase() {
    // Log info entering method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
    
    // Loads the JDBC driver, if exists
    try {
      Class.forName(m_strJdbcDriver).getDeclaredConstructor().newInstance();
    }
    catch (ClassNotFoundException ex) {
      // An exception was detected; the program terminates with an error
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_DB_OPERATION.getErrorCode(), 
            "Error: Invalid JDBC driver \"" + m_strJdbcDriver + "\"!" + 
            System.getProperty("line.separator", "\n") + "Exception: " + ex.getMessage());
    }
    catch (Exception ex) {
      // An exception was detected; the program terminates with an error
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_DB_OPERATION.getErrorCode(), 
            "Error: Invalid JDBC driver \"" + m_strJdbcDriver + "\"!" + 
            System.getProperty("line.separator", "\n") + "Exception: " + ex.getMessage());
    }

    // Connects to database
    try {
      m_dbConnection = DriverManager.getConnection(m_strJdbcURL, m_strJdbcUsername, m_strJdbcPassword);
    }
    catch (SQLException ex) {
      // An exception was detected; the program terminates with an error
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_DB_OPERATION.getErrorCode(), 
            "Error: Invalid JDBC connection URL \"" + m_strJdbcURL + "\"!" + 
            System.getProperty("line.separator", "\n") + "Exception: " + ex.getMessage());
    }
    catch (Exception ex) {
      // An exception was detected; the program terminates with an error
      if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
        m_objLogger.log(Level.SEVERE, null, ex);
      }

      doExit(ExitErrorCodes.EXIT_ERROR_BAD_DB_OPERATION.getErrorCode(), 
            "Error: Invalid JDBC connection URL \"" + m_strJdbcURL + "\"!" + 
            System.getProperty("line.separator", "\n") + "Exception: " + ex.getMessage());
    }
    
    // Log info exiting method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.exiting(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
  }
  
  /**
   * Disconnects from the database.
   */
  private static void disconectFromDatabase() {
    // Log info entering method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
    
    // Disconnects from database
    synchronized (m_dbConnection) {
      try {
        if ((m_dbConnection != null) && !m_dbConnection.isClosed()) {
          m_dbConnection.close();
          m_dbConnection = null;
        }
      }
      catch (SQLException ex) {
        // An exception was detected; the program terminates with an error
        if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
          m_objLogger.log(Level.SEVERE, null, ex);
        }
      }
      catch (Exception ex) {
        // An exception was detected; the program terminates with an error
        if ((m_objLogger != null) && ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0)) {
          m_objLogger.log(Level.SEVERE, null, ex);
        }
      }
    }
    
    // Log info exiting method
    if (m_objLogger != null) {
      if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_objLogger.exiting(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      }
    }
  }
  
  /**
   *
   */
  private static void doHtml() {
    Document doc = null;
    try {
      doc = Jsoup.connect("https://v2.bricklink.com/en-us/catalog/color-guide").timeout(m_iHttpTimeout).get();
    } catch (IOException ex) {
      System.getLogger(BricksCollectorHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
    }
    if (doc != null) {
      System.out.println(doc.title());
      Elements newsHeadlines = doc.body().select("div[data-state] h2");
      for (Element headline : newsHeadlines) {
        System.out.println(headline.text() + " | " + headline.attr("class") + " | " + headline.absUrl("href"));
      }
    }
  }
  
  /**
   * Runs the BricksCollectorHelper application.
   *
   * @param arrStrArgs The command line parameters/arguments
   */
  public static void main(String[] arrStrArgs) {
    // Splash Screen Manager
    if (ssManager != null) {
      ssManager.setTextRectangle(new Rectangle(15, 140, 270, 22));
      ssManager.setFontName("Arial");
      ssManager.setFontSize(16);
      //ssManager.setFontStyle(Font.BOLD | Font.ITALIC);
      ssManager.setFontStyle(Font.PLAIN);
      ssManager.setFontColor(Color.BLACK);
      ssManager.setFontBackground(Color.LIGHT_GRAY);
      ssManager.setBarRectangle(new Rectangle(15, 170, 270, 15));
      ssManager.setBarColor(Color.GREEN);
      ssManager.setBarBackground(Color.WHITE);

      ssManager.render("Loading Java base classes...", 30);
    }

    // Processes the command line parameters
    if (ssManager != null) {
      ssManager.render("Parsing the command line parameters...", 55);
    }
    parseArguments(arrStrArgs);

    // Reads the program default settings
    if (ssManager != null) {
      ssManager.render("Reading configuration file...", 50);
    }
    try {
      readConfig();
    }
    catch (Exception ex) {
      Logger.getLogger(BricksCollectorHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // Creates the error log file
    if (ssManager != null) {
      ssManager.render("Creating error log file...", 60);
    }
    if (m_fLogLevel != DEBUG_LEVEL_NONE) {
      Formatter formatter = new SimpleFormatter() {
        @Override
        public String format(LogRecord record) {
          final String strNewLine = System.getProperty("line.separator");
          final SimpleDateFormat logTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
          Calendar cal = new GregorianCalendar();
          cal.setTimeInMillis(record.getMillis());
          String strMsg = logTime.format(cal.getTime()) + "\t" + record.getLevel().getLocalizedName() + "\t";
          Throwable throwable = record.getThrown();
          if (throwable != null) {
            strMsg = strMsg
              + record.getSourceClassName().substring(
                record.getSourceClassName().lastIndexOf(".") + 1,
                record.getSourceClassName().length())
              + "." + record.getSourceMethodName() + "()" + "\t"
              + "Exception:" + throwable.getClass().getName() + " "
              + "Message:" + throwable.getLocalizedMessage();
          } else {
            strMsg = strMsg
              + record.getSourceClassName().substring(
                record.getSourceClassName().lastIndexOf(".") + 1,
                record.getSourceClassName().length())
              + "." + record.getSourceMethodName() + "()" + "\t"
              + record.getMessage();
          }
          return strMsg + strNewLine;
        }
      };

      FileHandler fh = null;
      try {
        fh = new FileHandler("logs" + File.separator + "BricksCollectorHelper%g.log", 10 * 1024 * 1024, 30, false);
        fh.setFormatter(formatter);
        fh.setEncoding("UTF-8");
      } catch (IOException ex) {
        System.getLogger(BricksCollectorHelper.class.getName()).log(System.Logger.Level.ERROR, "Fail to create log file", ex);
      }

      m_objLogger = Logger.getLogger(BricksCollectorHelper.class.getName());
      m_objLogger.setUseParentHandlers(false);
      if (fh != null) {
        m_objLogger.addHandler(fh);
      }
      if (m_fLogLevel == DEBUG_LEVEL_NONE) {
        m_objLogger.setLevel(Level.OFF);
      } else if ((m_fLogLevel & DEBUG_LEVEL_ALL) != 0) {
        m_objLogger.setLevel(Level.ALL);
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      } else if ((m_fLogLevel & DEBUG_LEVEL_INFO) != 0) {
        m_fLogLevel |= DEBUG_LEVEL_INFO | DEBUG_LEVEL_WARNING | DEBUG_LEVEL_ERROR;
        m_objLogger.setLevel(Level.INFO);
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      } else if ((m_fLogLevel & DEBUG_LEVEL_WARNING) != 0) {
        m_fLogLevel |= DEBUG_LEVEL_WARNING | DEBUG_LEVEL_ERROR;
        m_objLogger.setLevel(Level.WARNING);
        m_objLogger.entering(BricksCollectorHelper.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
      } else if ((m_fLogLevel & DEBUG_LEVEL_ERROR) != 0) {
        m_fLogLevel |= DEBUG_LEVEL_ERROR;
        m_objLogger.setLevel(Level.SEVERE);
      }
    }
    
    // Swing translations
    if (ssManager != null) {
      ssManager.render("Translating Java Swing...", 95);
    }
    final InternationalizationUtils translations = InternationalizationUtils.getInstance();
    String strLang = Locale.getDefault().toString();
    InternationalizationUtils.translateJavaDefaultResources(strLang);
    
    // Database connection
    if (ssManager != null) {
      ssManager.render("Connecting to database...", 97);
    }
    conectToDatabase();
    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException ex) {
    }
    disconectFromDatabase();
    
    // Main window widget
    if (ssManager != null) {
      ssManager.render("Creating Main Window...", 98);
    }
    
    // Close the Splash Screen
    if (ssManager != null) {
      ssManager.close();
    }
    
    doHtml();
  }
}
