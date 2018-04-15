package drusp.shimi.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AdoptDogPostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String sAdoptStatus = "หาบ้าน";

    private TextView textViewPhone;
    private TextView textViewBackupPhone;
    private TextView textViewFacebook;
    private TextView textViewDogName;
    private TextView textViewBreed;
    private TextView textViewGender;
    private TextView textViewAge;
    private TextView textViewVaccinate;
    private TextView textViewHabit;
    private TextView textViewStatus;
    private TextView textViewPost;
    private TextView textViewReason;
    private TextView textViewLocation1;
    private TextView textViewLocation2;
    private TextView textViewDistrict;
    private TextView textViewLine;

    private Uri uriDogPhoto;

    private String sAdoptDay;
    private String sAdoptMonth;
    private String sAdoptYear;
    private String sTimestamp;
    private String sPostDay;
    private String sPostMonth;
    private String sPostYear;
    private String sDistrictEng = null;
    private String sBreedEng = null;

    private FloatingActionButton fabSubmit;

    private StorageReference storageReference;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseUser;

    private FirebaseUser currentUser;

    private Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_dog_post);

        textViewPhone = (TextView) findViewById(R.id.tv_adopt_dog_post_phone);
        textViewBackupPhone = (TextView) findViewById(R.id.tv_adopt_dog_post_backup_phone);
        textViewFacebook = (TextView) findViewById(R.id.tv_adopt_dog_post_facebook);
        textViewDogName = (TextView) findViewById(R.id.tv_adopt_dog_post_dog_name);
        textViewBreed = (TextView) findViewById(R.id.tv_adopt_dog_post_breed);
        textViewGender = (TextView) findViewById(R.id.tv_adopt_dog_post_gender);
        textViewAge = (TextView) findViewById(R.id.tv_adopt_dog_post_age);
        textViewLocation1 = (TextView) findViewById(R.id.tv_adopt_dog_post_location1);
        textViewLocation2 = (TextView) findViewById(R.id.tv_adopt_dog_post_location2);
        textViewDistrict = (TextView) findViewById(R.id.tv_adopt_dog_post_district);
        textViewVaccinate = (TextView) findViewById(R.id.tv_adopt_dog_post_vaccinate);
        textViewHabit = (TextView) findViewById(R.id.tv_adopt_dog_post_habit);
        textViewLine = (TextView) findViewById(R.id.tv_adopt_dog_post_line);
        textViewStatus = (TextView) findViewById(R.id.tv_adopt_dog_post_status);
        textViewPost = (TextView) findViewById(R.id.tv_adopt_dog_post_post);
        textViewReason = (TextView) findViewById(R.id.tv_adopt_dog_post_reason);

        ImageView imageViewDogPhoto = (ImageView) findViewById(R.id.iv_adopt_dog_post_photo);

        fabSubmit = (FloatingActionButton) findViewById(R.id.fab_adopt_dog_post_submit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_adopt_dog_post);

        SharedPreferences sharedPref = getSharedPreferences("adoptDogInput", Context.MODE_PRIVATE);
        String sUriDogPhoto = sharedPref.getString("adoptDogInpuDogFilepath", "");
        String sPhone = sharedPref.getString("adoptDogInputPhone", "");
        String sBackupPhone = sharedPref.getString("adoptDogInputBackupPhone", "");
        String sFacebook = sharedPref.getString("adoptDogInputFacebook", "");
        String sLine = sharedPref.getString("adoptDogInputLine", "");
        if (sBackupPhone.isEmpty()) {
            sBackupPhone = "ไม่มี";
        }
        if (sFacebook.isEmpty()) {
            sFacebook = "ไม่มี";
        }
        if (sLine.isEmpty()) {
            sLine = "ไม่มี";
        }
        String sDogName = sharedPref.getString("adoptDogInputDogName", "");
        String sBreed = sharedPref.getString("adoptDogInputBreed", "");
        String sGender = sharedPref.getString("adoptDogInputGender", "");
        String sAge = sharedPref.getString("adoptDogInputAge", "");
        String sVaccinate = sharedPref.getString("adoptDogInputVaccinate", "");
        String sHabit = sharedPref.getString("adoptDogInputHabit", "");
        String sReason = sharedPref.getString("adoptDogInputReason", "");
        String sLocation1 = sharedPref.getString("adoptDogInputLocation1", "");
        String sLocation2 = sharedPref.getString("adoptDogInputLocation2", "");
        String sDistrict = sharedPref.getString("adoptDogInputDistrict", "");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = mdformat.format(calendar.getTime());

        textViewPost.setText(strDate);
        textViewPhone.setText(sPhone);
        textViewBackupPhone.setText(sBackupPhone);
        textViewFacebook.setText(sFacebook);
        textViewDogName.setText(sDogName);
        textViewBreed.setText(sBreed);
        textViewGender.setText(sGender);
        textViewAge.setText(sAge);
        textViewVaccinate.setText(sVaccinate);
        textViewHabit.setText(sHabit);
        textViewReason.setText(sReason);
        textViewLocation1.setText(sLocation1);
        textViewLocation2.setText(sLocation2);
        textViewDistrict.setText(sDistrict);
        textViewLine.setText(sLine);
        textViewStatus.setText(sAdoptStatus);

        String[] dateParts = strDate.split("/");
        String[] postParts = strDate.split("/");

        sAdoptDay = dateParts[0];
        sAdoptMonth = dateParts[1];
        sAdoptYear = dateParts[2];
        sTimestamp = sAdoptYear + sAdoptMonth + sAdoptDay;

        sPostDay = postParts[0];
        sPostMonth = postParts[1];
        sPostYear = postParts[2];

        uriDogPhoto = Uri.parse(sUriDogPhoto);

        String sDogPhoto = sharedPref.getString("adoptDogInputDogPhoto", "");
        Bitmap bmDogPhoto = null;
        if (!sDogPhoto.equals("")) bmDogPhoto = decodeToBase64(sDogPhoto);
        imageViewDogPhoto.setImageBitmap(bmDogPhoto);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

        fabSubmit.setOnClickListener(this);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_white_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startPosting() {
        final String post_date_val = textViewPost.getText().toString().trim();
        final String dog_name_val = textViewDogName.getText().toString().trim();
        final String breed_val = textViewBreed.getText().toString().trim();
        final String gender_val = textViewGender.getText().toString().trim();
        final String age_val = textViewAge.getText().toString().trim();
        final String location1_val = textViewLocation1.getText().toString().trim();
        final String location2_val = textViewLocation2.getText().toString().trim();
        final String district_val = textViewDistrict.getText().toString().trim();
        final String phone_val = textViewPhone.getText().toString().trim();
        final String facebook_val = textViewFacebook.getText().toString().trim();
        final String line_val = textViewLine.getText().toString().trim();
        final String vaccinate_val = textViewVaccinate.getText().toString().trim();
        final String habit_val = textViewHabit.getText().toString().trim();
        final String status_val = textViewStatus.getText().toString().trim();
        final String reason_val = textViewReason.getText().toString().trim();
        final String backup_phone_val = textViewBackupPhone.getText().toString().trim();

        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Bangkok"));
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(c.getTimeZone());
        final String timestamp = format.format(c.getTime());
        final Long longTimestamp = Long.parseLong(timestamp);
        final Long longTimeQuery = Long.parseLong(sTimestamp);
        final Long longAge = Long.parseLong(age_val);

        final boolean blnAdoptStatus = true;

        StorageReference filePath = storageReference.child("adopt_dog_image").child(uriDogPhoto.getLastPathSegment());

        filePath.putFile(uriDogPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                final String keyAdoptDogPoster = databaseReference.push().getKey();

                databaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> postValues = new HashMap<>();
                        postValues.put("status", status_val);
                        postValues.put("adopt_status", blnAdoptStatus);
                        postValues.put("post_date", post_date_val);
                        postValues.put("timestamp_order", longTimestamp);
                        postValues.put("timestamp_query", longTimeQuery);
                        postValues.put("dog_name", dog_name_val);
                        postValues.put("breed", breed_val);
                        postValues.put("gender", gender_val);
                        postValues.put("age", longAge);
                        postValues.put("location1", location1_val);
                        postValues.put("location2", location2_val);
                        postValues.put("district", district_val);
                        postValues.put("adopt_date", post_date_val);
                        postValues.put("date", post_date_val);
                        postValues.put("phone_number", phone_val);
                        postValues.put("backup_phone_number", backup_phone_val);
                        postValues.put("facebook", facebook_val);
                        postValues.put("line", line_val);
                        postValues.put("vaccinate", vaccinate_val);
                        postValues.put("habit", habit_val);
                        postValues.put("reason", reason_val);
                        assert downloadUrl != null;
                        postValues.put("dog_image_url", downloadUrl.toString());
                        postValues.put("username", dataSnapshot.child("username").getValue());
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/adopt_dog_poster/" + keyAdoptDogPoster, postValues);
                        childUpdates.put("/user_post/" + currentUser.getUid() + "/" + keyAdoptDogPoster, postValues);
                        childUpdates.put("/main_poster/" + keyAdoptDogPoster, postValues);

                        databaseReference.updateChildren(childUpdates);

                        String[] breed = new String[]{"บีเกิล", "บลูด๊อก", "ชิวาวา", "เฟรนช์ บูลด็อก", "โกลเด้น รีทรีฟเวอร์", "แจ๊ค รัสเซล เทอร์เรีย", "ลาบราดอร์ รีทรีฟเวอร์", "พูเดิล", "ปอมเมอเรเนียน", "ปั๊ก"
                                , "ร๊อตต์ไวเลอร์", "ชิสุ", "ไซบีเรียน ฮัสกี้", "ไทยบางแก้ว", "ไทยหลังอาน", "ยอร์คเชียร์ เทอร์เรียร์"};

                        for (String aBreed : breed) {
                            if (breed_val.equals(aBreed)) {
                                switch (breed_val) {
                                    case "บีเกิล":
                                        sBreedEng = "beagle";
                                        break;
                                    case "บลูด๊อก":
                                        sBreedEng = "bulldog";
                                        break;
                                    case "ชิวาวา":
                                        sBreedEng = "chihuahua";
                                        break;
                                    case "เฟรนช์ บูลด็อก":
                                        sBreedEng = "french_bulldog";
                                        break;
                                    case "โกลเด้น รีทรีฟเวอร์":
                                        sBreedEng = "golden_retriever";
                                        break;
                                    case "แจ๊ค รัสเซล เทอร์เรีย":
                                        sBreedEng = "jack_russell_terrier";
                                        break;
                                    case "ลาบราดอร์ รีทรีฟเวอร์":
                                        sBreedEng = "labrador_retriever";
                                        break;
                                    case "พูเดิล":
                                        sBreedEng = "poodle";
                                        break;
                                    case "ปอมเมอเรเนียน":
                                        sBreedEng = "pomeranian";
                                        break;
                                    case "ปั๊ก":
                                        sBreedEng = "pug";
                                        break;
                                    case "ร๊อตต์ไวเลอร์":
                                        sBreedEng = "rottweiler";
                                        break;
                                    case "ชิสุ":
                                        sBreedEng = "shih_tzu";
                                        break;
                                    case "ไซบีเรียน ฮัสกี้":
                                        sBreedEng = "siberian_husky";
                                        break;
                                    case "ไทยบางแก้ว":
                                        sBreedEng = "thai_bangkaew";
                                        break;
                                    case "ไทยหลังอาน":
                                        sBreedEng = "thai_ridgeback";
                                        break;
                                    case "ยอร์คเชียร์ เทอร์เรียร์":
                                        sBreedEng = "yorkshire_terrier";
                                        break;
                                }
                                String[] district = new String[]{"บางบ่อ", "บางพลี", "บางเสาธง", "พระประแดง", "พระสมุทรเจดีย์", "เมืองสมุทรปราการ"};

                                for (String aDistrict : district) {
                                    if (district_val.equals(aDistrict)) {
                                        switch (district_val) {
                                            case "บางบ่อ":
                                                sDistrictEng = "bang_bo";
                                                break;
                                            case "บางพลี":
                                                sDistrictEng = "bang_phli";
                                                break;
                                            case "บางเสาธง":
                                                sDistrictEng = "bang_sao_thong";
                                                break;
                                            case "พระประแดง":
                                                sDistrictEng = "phra_pradaeng";
                                                break;
                                            case "พระสมุทรเจดีย์":
                                                sDistrictEng = "phra_samut_chedi";
                                                break;
                                            case "เมืองสมุทรปราการ":
                                                sDistrictEng = "mueang_samut_prakan";
                                                break;
                                        }
                                        final Integer iAdoptYear = Integer.parseInt(sAdoptYear);
                                        for (int y = 2017; y <= iAdoptYear; y++) {
                                            if (y == iAdoptYear) {
                                                final Integer iAdoptMonth = Integer.parseInt(sAdoptMonth);
                                                for (int m = 0; m <= iAdoptMonth; m++) {
                                                    if (m == iAdoptMonth) {
                                                        final Integer iAdoptDay = Integer.parseInt(sAdoptDay);
                                                        for (int d = 0; d <= iAdoptDay; d++) {
                                                            if (d == iAdoptDay) {
                                                                Map<String, Object> childUpdates1 = new HashMap<>();

                                                                childUpdates1.put("/view/breed/" + sBreedEng + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/breed&ditrict/" + sBreedEng + "/district/" + sDistrictEng + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/breed&district&date/breed/" + sBreedEng + "/district/" + sDistrictEng
                                                                        + "/date/year/" + sAdoptYear + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/breed&district&date/breed/" + sBreedEng + "/district/" + sDistrictEng
                                                                        + "/date/month/" + "/" + sAdoptYear + "/" + sAdoptMonth + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/breed&district&date/breed/" + sBreedEng + "/district/" + sDistrictEng
                                                                        + "/date/day/" + "/" + sAdoptYear + "/" + sAdoptMonth + "/" + sAdoptDay + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed/" + sBreedEng + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&district/breed/" + sBreedEng + "/district/" + sDistrictEng + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&adopt_date/breed/" + sBreedEng + "/adopt_date/year/" + sAdoptYear + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&adopt_date/breed/" + sBreedEng + "/adopt_date/month/" + sAdoptYear + "/"
                                                                        + sAdoptMonth + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&adopt_date/breed/" + sBreedEng + "/adopt_date/day/" + sAdoptYear + "/"
                                                                        + sAdoptMonth + "/" + sAdoptDay + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&district&adopt_date/breed/" + sBreedEng + "/district/" + sDistrictEng
                                                                        + "/adopt_date/year/" + sAdoptYear + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&district&adopt_date/breed/" + sBreedEng + "/district/" + sDistrictEng
                                                                        + "/adopt_date/month/" + "/" + sAdoptYear + "/" + sAdoptMonth + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/breed&district&adopt_date/breed/" + sBreedEng + "/district/" + sDistrictEng
                                                                        + "/adopt_date/day/" + "/" + sAdoptYear + "/" + sAdoptMonth + "/" + sAdoptDay + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/user_post/" + currentUser.getUid() + "/post_date/year/" + sPostYear + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/user_post/" + currentUser.getUid() + "/post_date/month/" + sPostYear + "/" + sPostMonth + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/user_post/" + currentUser.getUid() + "/post_date/day/" + sPostYear + "/" + sPostMonth + "/"
                                                                        + sPostDay + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/district/" + sDistrictEng + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/district/" + sDistrictEng + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/district&adopt_date/district/" + sDistrictEng + "/adopt_date/year/" + sAdoptYear + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/district&adopt_date/district/" + sDistrictEng + "/adopt_date/month/" + sAdoptYear + "/"
                                                                        + sAdoptMonth + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/district&adopt_date/district/" + sDistrictEng + "/adopt_date/day/" + sAdoptYear + "/"
                                                                        + sAdoptMonth + "/" + sAdoptDay + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/adopt_date/year/" + sAdoptYear + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/adopt_date/month/" + sAdoptYear + "/" + sAdoptMonth + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/adopt_dog/adopt_date/day/" + sAdoptYear + "/" + sAdoptMonth + "/" + sAdoptDay + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/date/year/" + sAdoptYear + "/" + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/date/month/" + sAdoptYear + "/" + sAdoptMonth + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/date/day/" + sAdoptYear + "/" + sAdoptMonth + "/" + sAdoptDay + "/"
                                                                        + keyAdoptDogPoster, postValues);

                                                                childUpdates1.put("/view/date/timestamp/" + sTimestamp + "/" + keyAdoptDogPoster, postValues);

                                                                databaseReference.updateChildren(childUpdates1);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    private void showAlertDialogSubmit() {
        new AlertDialog.Builder(AdoptDogPostActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.adopt_dog_post_alert_title)
                .setMessage(R.string.adopt_dog_post_alert_msg)
                .setPositiveButton(R.string.adopt_dog_post_alert_done, null)
                .setNegativeButton(R.string.adopt_dog_post_alert_cancel, null)
                .setPositiveButton(R.string.adopt_dog_post_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startPosting();
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(toMain);
                    }
                })
                .setNegativeButton(R.string.adopt_dog_input_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        if (view == fabSubmit) {
            showAlertDialogSubmit();
        }
    }

}
