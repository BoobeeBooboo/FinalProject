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

public class FoundDogPosterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewDogPhoto;
    private ImageView imageViewContacts;

    private TextView TextLocation1;
    private TextView TextLocation2;
    private TextView TextGender;
    private TextView TextCollar;
    private TextView TextDistrict;
    private TextView TextPhone;
    private TextView TexFoundtDate;

    private String sBackupPhonrNumber;
    private String sFacebook;
    private String sLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_dog_poster);

        imageViewDogPhoto = (ImageView) findViewById(R.id.found_dog_poster_dog_image);
        imageViewContacts = (ImageView) findViewById(R.id.iv_found_dog_poster_contacts);

        TextLocation1 = (TextView) findViewById(R.id.found_dog_poster_location1);
        TextLocation2 = (TextView) findViewById(R.id.found_dog_poster_location2);
        TextDistrict = (TextView) findViewById(R.id.found_dog_poster_district);
        TextPhone = (TextView) findViewById(R.id.found_dog_poster_phone);
        TexFoundtDate = (TextView) findViewById(R.id.found_dog_poster_found_date);
        TextGender = (TextView) findViewById(R.id.found_dog_poster_gender);
        TextCollar = (TextView) findViewById(R.id.found_dog_poster_collar);

        imageViewContacts.setOnClickListener(this);

        String postKey = getIntent().getExtras().getString("postKey");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("found_dog_poster");

        assert postKey != null;
        databaseReference.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String postFoundDate = (String) dataSnapshot.child("found_date").getValue();
                String postDistrict = (String) dataSnapshot.child("district").getValue();
                String imageDogUrl = (String) dataSnapshot.child("dog_image_url").getValue();
                String postLocation1 = (String) dataSnapshot.child("location1").getValue();
                String postLocation2 = (String) dataSnapshot.child("location2").getValue();
                String postGender = (String) dataSnapshot.child("gender").getValue();
                String postCollar = (String) dataSnapshot.child("collar").getValue();
                String postPhone = (String) dataSnapshot.child("phone_number").getValue();
                sBackupPhonrNumber = (String) dataSnapshot.child("backup_phone_number").getValue();
                sFacebook = (String) dataSnapshot.child("facebook").getValue();
                sLine = (String) dataSnapshot.child("line").getValue();

                TextLocation1.setText(postLocation1);
                TextLocation2.setText(postLocation2);
                TextDistrict.setText(postDistrict);
                TextPhone.setText(postPhone);
                TexFoundtDate.setText(postFoundDate);
                TextGender.setText(postGender);
                TextCollar.setText(postCollar);

                Picasso.with(FoundDogPosterActivity.this).load(imageDogUrl).into(imageViewDogPhoto);
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
        if (view == imageViewContacts) {
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