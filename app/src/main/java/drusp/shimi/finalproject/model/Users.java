package drusp.shimi.finalproject.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Shimi on 16/10/2560.
 */

@IgnoreExtraProperties
public class Users {

    public String username, email;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String username, String email) {
        this.username = username;
        this.email = email;
    }

}


