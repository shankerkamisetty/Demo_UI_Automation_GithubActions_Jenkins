package base;

public class AppConstants {
    public static final String browserName = System.getProperty("browserName", "chrome");

    /*For GitHub actions use: remote_git
     * For Selenium Grid use: remote*/
    public static final String platform = System.getProperty("platform", "remote_git");

}
