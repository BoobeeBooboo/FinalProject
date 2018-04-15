package drusp.shimi.finalproject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdoptDogPosterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewDogPhoto;
    private ImageView imageViewContacts;

    private TextView textViewDogName;
    private TextView textViewGender;
    private TextView textViewAge;
    private TextView textViewReason;
    private TextView textViewLocation1;
    private TextView textViewLocation2;
    private TextView textViewDistrict;
    private TextView textViewPhone;

    private String sBackupPhonrNumber;
    private String sFacebook;
    private String sLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_dog_poster);

        imageViewDogPhoto = (ImageView) findViewById(R.id.adopt_dog_poster_dog_image);
        imageViewContacts = (ImageView) findViewById(R.id.iv_adopt_dog_poster_contacts);

        textViewDogName = (TextView) findViewById(R.id.adopt_dog_poster_dog_name);
        textViewGender = (TextView) findViewById(R.id.adopt_dog_poster_gender);
        textViewAge = (TextView) findViewById(R.id.adopt_dog_poster_age);
        textViewLocation1 = (TextView) findViewById(R.id.adopt_dog_poster_location1);
        textViewLocation2 = (TextView) findViewById(R.id.adopt_dog_poster_location2);
        textViewDistrict = (TextView) findViewById(R.id.adopt_dog_poster_district);
        textViewPhone = (TextView) findViewById(R.id.adopt_dog_poster_phone);
        textViewReason = (TextView) findViewById(R.id.adopt_dog_poster_reason);

        imageViewContacts.setOnClickListener(this);

        String postKey = getIntent().getExtras().getString("postKey");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("adopt_dog_poster");

        assert postKey != null;
        databaseReference.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long postAge = (Long) dataSnapshot.child("age").getValue();
                String postDistrict = (String) dataSnapshot.child("district").getValue();
                String imageDogUrl = (String) dataSnapshot.child("dog_image_url").getValue();
                String postDogName = (String) dataSnapshot.child("dog_name").getValue();
                String postGender = (String) dataSnapshot.child("gender").getValue();
                String postLocation1 = (String) dataSnapshot.child("location1").getValue();
                String postLocation2 = (String) dataSnapshot.child("location2").getValue();
                String postPhone = (String) dataSnapshot.child("phone_number").getValue();
                String postReason = (String) dataSnapshot.child("reason").getValue();
                sBackupPhonrNumber = (String) dataSnapshot.child("backup_phone_number").getValue();
                sFacebook = (String) dataSnapshot.child("facebook").getValue();
                sLine = (String) dataSnapshot.child("line").getValue();
                String sAge = String.valueOf(postAge);

                textViewDogName.setText(postDogName);
                textViewGender.setText(postGender);
                textViewAge.setText(sAge);
                textViewLocation1.setText(postLocation1);
                textViewLocation2.setText(postLocation2);
                textViewDistrict.setText(postDistrict);
                textViewPhone.setText(postPhone);
                textViewReason.setText(postReason);

                Picasso.with(AdoptDogPosterActivity.this).load(imageDogUrl).into(imageViewDogPhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if(view==imageViewContacts){
            showDialogContacts();
        }
    }

    private void showDialogContacts() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_contacts);
        TextView textViewBackUpPhoneNumber = (TextView) dialog.findViewById(R.id.tv_contacts_backup_phone_number);
        textViewBackUpPhoneNumber.setText(sBackupPhonrNumber);
        TextView textViewFacebook = (TextView) dialog.findViewById(R.id.tv_contacts_facebook);
        textViewFacebook.setText(sFacebook);
        TextView textViewLine = (TextView) dialog.findViewById(R.id.tv_contacts_line);
        textViewLine.setText(sLine);
        dialog.show();
    }

}
