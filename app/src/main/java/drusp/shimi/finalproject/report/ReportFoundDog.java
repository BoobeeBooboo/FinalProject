package drusp.shimi.finalproject.report;

/**
 * Created by Shimi on 1/11/2560.
 */

public class ReportFoundDog {

    public String found_date, breed, district, phone_number;

    public ReportFoundDog() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportFoundDog(String found_date, String breed, String district, String phone_number) {
        this.found_date = found_date;
        this.breed = breed;
        this.district = district;
        this.phone_number = phone_number;
    }

}
