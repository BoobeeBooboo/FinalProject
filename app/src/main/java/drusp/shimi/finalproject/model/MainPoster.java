package drusp.shimi.finalproject.model;

/**
 * Created by Shimi on 08/25/2017.
 */

public class MainPoster {

    private String dog_image_url, status, date, location1, location2, district, phone_number;

    private Long timestamp;

    public MainPoster() {
    }

    public MainPoster(String dog_image_url, String status, String date, String location1, String location2, String district,
                      String phone_number, Long timestamp) {
        this.dog_image_url = dog_image_url;
        this.status = status;
        this.date = date;
        this.location1 = location1;
        this.location2 = location2;
        this.district = district;
        this.phone_number = phone_number;
        this.timestamp = timestamp;
    }

    public String getDog_image_url() {
        return dog_image_url;
    }

    public void setDog_image_url(String dog_image_url) {
        this.dog_image_url = dog_image_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation1() {
        return location1;
    }

    public void setLocation1(String location1) {
        this.location1 = location1;
    }

    public String getLocation2() {
        return location2;
    }

    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
