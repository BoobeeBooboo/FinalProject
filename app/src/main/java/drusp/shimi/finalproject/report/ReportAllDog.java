package drusp.shimi.finalproject.report;

/**
 * Created by Shimi on 1/11/2560.
 */

public class ReportAllDog {

    public String status, date, breed, district, phone_number;

    public ReportAllDog() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportAllDog(String status, String date, String breed, String district, String phone_number) {
        this.status = status;
        this.date = date;
        this.breed = breed;
        this.district = district;
        this.phone_number = phone_number;
    }

}
