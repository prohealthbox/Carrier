package comcmput301f16t01.github.carrier;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by meind on 2016-10-11.
 *
 * Singleton Pattern
 * * modifies/returns a RequestList model
 * * @see Request
 * * @see RequestList
 */

public class UserController {
    private static RiderList riderList = null;
    private static DriverList driverList = null;
    private static User loggedInUser = null;


    public UserController() {
        if (riderList == null) {
            riderList = new RiderList();
        }
        if (driverList == null) {
            driverList = new DriverList();
        }
    }

    /**
     * @return the RiderList held by this controller.
     */
    public static RiderList getRiderList() {
        if (riderList == null) {
            riderList = new RiderList();
        }
        return riderList;
    }

    /**
     * @return the DriverList held by this controller.
     */
    public static DriverList getDriverList() {
        if (driverList == null) {
            driverList = new DriverList();
        }
        return driverList;
    }

    /**
     * Making sure that the username is unique across all riders
     * otherwise it throws an exception
     *
     * return True if it is unique and False if it is similar
     *
     * @param rider
     */
    public boolean uniqueRiderUsername(Rider rider) {
        return false;
    }

    /**
     * Making sure that the username is unique across all drivers
     * otherwise it throws an exception
     *
     * return True if it is unique and False if it is similar
     *
     * @param driver
     */
    public boolean uniqueDriverUsername(Driver driver) {
        return false;
    }


    public void setLoggedInUser(User user){
        loggedInUser = user;
    }

    public User getLoggedInUser(){
        return loggedInUser;
    }

    public void LogOutUser() {
        loggedInUser=null;
    }


    public void setEmail(User user, String email) {
        user.setEmail(email);
    }

    public void setPhone(User user, String phone) {
        user.setPhone(phone);
    }
}
