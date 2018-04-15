package drusp.shimi.finalproject.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shimi on 08/27/2017.
 */

public class MarkerLocation implements Parcelable {

    public static final Creator<MarkerLocation> CREATOR = new Creator<MarkerLocation>() {
        @Override
        public MarkerLocation createFromParcel(Parcel in) {
            return new MarkerLocation(in);
        }

        @Override
        public MarkerLocation[] newArray(int size) {
            return new MarkerLocation[size];
        }
    };

    private Double lat, lng;

    private String poster_id, status, dog_image_url;

    public MarkerLocation() {
    }

    public MarkerLocation(Double lat, Double lng, String status, String dog_image_url, String poster_id) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
        this.dog_image_url = dog_image_url;
        this.poster_id = poster_id;
    }

    public MarkerLocation(Parcel parcel) {
        setLat(parcel.readDouble());
        setLng(parcel.readDouble());
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDog_image_url() {
        return dog_image_url;
    }

    public void setDog_image_url(String dog_image_url) {
        this.dog_image_url = dog_image_url;
    }

    public String getPoster_id() {
        return poster_id;
    }

    public void setPoster_id(String poster_id) {
        this.poster_id = poster_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(getLat());
        parcel.writeDouble(getLng());
    }

}
