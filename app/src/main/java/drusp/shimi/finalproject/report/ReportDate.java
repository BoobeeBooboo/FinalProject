package drusp.shimi.finalproject.report;

/**
 * Created by Shimi on 31/10/2560.
 */

public class ReportDate {

    public String date, breed, district, status;
    public Long timestamp_query;

    public ReportDate() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportDate(String date, String breed, String district, String status, Long timestamp_query) {
        this.date = date;
        this.breed = breed;
        this.district = district;
        this.status = status;
        this.timestamp_query = timestamp_query;
    }

}