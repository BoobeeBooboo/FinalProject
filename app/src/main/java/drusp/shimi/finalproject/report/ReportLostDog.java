package drusp.shimi.finalproject.report;

/**
 * Created by Shimi on 1/11/2560.
 */

public class ReportLostDog {

    public String dog_name, breed, lost_date, district, phone_number;

    public ReportLostDog() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportLostDog(String dog_name, String breed, String lost_date, String district, String phone_number) {
        this.dog_name = dog_name;
        this.breed = breed;
        this.lost_date = lost_date;
        this.district = district;
        this.phone_number = phone_number;
    }

}
