package drusp.shimi.finalproject.report;

/**
 * Created by Shimi on 12/11/2560.
 */

public class ReportAdoptDog {
    public String dog_name, breed, adopt_date, phone_number, reason;

    public ReportAdoptDog() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportAdoptDog(String dog_name, String breed, String adopt_date, String phone_number, String reason) {
        this.dog_name = dog_name;
        this.breed = breed;
        this.adopt_date = adopt_date;
        this.phone_number = phone_number;
        this.reason = reason;
    }

}