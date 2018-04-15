package drusp.shimi.finalproject.report;

/**
 * Created by Shimi on 31/10/2560.
 */

public class ReportUsers {

    public String username, email, password, sign_up_date;

    public ReportUsers() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportUsers(String username, String email, String password, String sign_up_date) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.sign_up_date = sign_up_date;
    }

}

