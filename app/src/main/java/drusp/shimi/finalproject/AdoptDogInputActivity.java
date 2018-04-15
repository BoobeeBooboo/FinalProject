package drusp.shimi.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdoptDogInputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 200;

    private String mSelectedBreed;
    private String mSelectedGender;
    private String mSelectedAge;
    private String mSelectedLocation1;
    private String mSelectedDistrict;
    private String mSelectedVaccinate;
    private String mSelectedHabit;
    private String mSelectedReason;

    private ImageView imageViewDogPhoto;

    private EditText editTextDogName;
    private EditText editTextLocation2;
    private EditText editTextPhone;
    private EditText editTextBackupPhone;
    private EditText editTextFacebook;
    private EditText editTextLine;

    private Button buttonChoosePhoto;
    private Button buttonSelectBreed;
    private Button buttonSelectGender;
    private Button buttonSelectAge;
    private Button buttonSelectLocation1;
    private Button buttonSelectDistrict;
    private Button buttonSelectVaccinate;
    private Button buttonSelectHabit;
    private Button buttonSelectReason;

    private FloatingActionButton fabSubmit;

    private Uri uri;

    private Bitmap bm;

    private String encodeTobase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_dog_input);

        buttonChoosePhoto = (Button) findViewById(R.id.btn_adopt_dog_input_choose);
        buttonSelectBreed = (Button) findViewById(R.id.btn_adopt_dog_input_breed);
        buttonSelectGender = (Button) findViewById(R.id.btn_adopt_dog_input_gender);
        buttonSelectAge = (Button) findViewById(R.id.btn_adopt_dog_input_age);
        buttonSelectLocation1 = (Button) findViewById(R.id.btn_adopt_dog_input_location1);
        buttonSelectDistrict = (Button) findViewById(R.id.btn_adopt_dog_input_district);
        buttonSelectVaccinate = (Button) findViewById(R.id.btn_adopt_dog_input_vaccinate);
        buttonSelectHabit = (Button) findViewById(R.id.btn_adopt_dog_input_habit);
        buttonSelectReason = (Button) findViewById(R.id.btn_adopt_dog_input_reason);

        fabSubmit = (FloatingActionButton) findViewById(R.id.fab_adopt_dog_input_submit);

        imageViewDogPhoto = (ImageView) findViewById(R.id.iv_adopt_dog_input_photo);

        editTextDogName = (EditText) findViewById(R.id.et_adopt_dog_input_dog_name);
        editTextPhone = (EditText) findViewById(R.id.et_adopt_dog_input_phone);
        editTextBackupPhone = (EditText) findViewById(R.id.et_adopt_dog_input_backup_phone);
        editTextFacebook = (EditText) findViewById(R.id.et_adopt_dog_input_facebook);
        editTextLine = (EditText) findViewById(R.id.et_adopt_dog_input_line);
        editTextLocation2 = (EditText) findViewById(R.id.et_adopt_dog_input_location2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_adopt_dog_input);

        fabSubmit.setOnClickListener(this);

        buttonChoosePhoto.setOnClickListener(this);
        buttonSelectBreed.setOnClickListener(this);
        buttonSelectGender.setOnClickListener(this);
        buttonSelectAge.setOnClickListener(this);
        buttonSelectLocation1.setOnClickListener(this);
        buttonSelectDistrict.setOnClickListener(this);
        buttonSelectVaccinate.setOnClickListener(this);
        buttonSelectHabit.setOnClickListener(this);
        buttonSelectReason.setOnClickListener(this);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_white_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพสุนัข"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewDogPhoto.setImageBitmap(bm);
                encodeTobase64(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlertDialogSubmit() {
        new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.adopt_dog_input_alert_title)
                .setMessage(R.string.adopt_dog_input_alert_msg)
                .setPositiveButton(R.string.adopt_dog_input_alert_done, null)
                .setNegativeButton(R.string.adopt_dog_input_alert_cancel, null)
                .setPositiveButton(R.string.adopt_dog_input_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendData();
                    }
                })
                .setNegativeButton(R.string.adopt_dog_input_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showDialogBreed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_breed);

        final String[] items = getResources().getStringArray(R.array.breedTH);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedBreed = items[which];
                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectBreed.setText(mSelectedBreed);
                        buttonSelectBreed.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogGender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_gender);

        final String[] items = getResources().getStringArray(R.array.gender);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedGender = items[which];

                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectGender.setText(mSelectedGender);
                        buttonSelectGender.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogAge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_age);

        final String[] items = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedAge = items[which];
                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectAge.setText(mSelectedAge);
                        buttonSelectAge.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogLocation1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_location1);

        final String[] items = getResources().getStringArray(R.array.location);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedLocation1 = items[which];

                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectLocation1.setText(mSelectedLocation1);
                        buttonSelectLocation1.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogDistrict() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_district);

        final String[] items = getResources().getStringArray(R.array.district);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedDistrict = items[which];

                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectDistrict.setText(mSelectedDistrict);
                        buttonSelectDistrict.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogVaccinate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_vaccinate);

        final String[] items = getResources().getStringArray(R.array.vaccinate);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedVaccinate = items[which];

                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectVaccinate.setText(mSelectedVaccinate);
                        buttonSelectVaccinate.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogHabit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_habit);

        final String[] items = getResources().getStringArray(R.array.habit);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedHabit = items[which];

                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectHabit.setText(mSelectedHabit);
                        buttonSelectHabit.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogReason() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.adopt_dog_input_alert_reason);

        final String[] items = getResources().getStringArray(R.array.reason);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedReason = items[which];

                    }
                });
        String positiveText = getString(R.string.adopt_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectReason.setText(mSelectedReason);
                        buttonSelectReason.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.adopt_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showAlertDialogError(int iCase) {
        String msg = null;
        switch (iCase) {
            case 1:
                msg = getString(R.string.adopt_dog_input_alert_case1);
                buttonChoosePhoto.requestFocus();
                break;
            case 2:
                msg = getString(R.string.adopt_dog_input_alert_case2);
                editTextDogName.setTextColor(Color.parseColor("#000000"));
                editTextDogName.requestFocus();
                break;
            case 3:
                msg = getString(R.string.adopt_dog_input_alert_case3);
                String xBreed = buttonSelectBreed.getText().toString().trim();
                buttonSelectBreed.setText("*" + " " + xBreed);
                buttonSelectBreed.setTextColor(Color.parseColor("#d50000"));
                buttonSelectBreed.setFocusable(true);
                buttonSelectBreed.setFocusableInTouchMode(true);///add this line
                buttonSelectBreed.requestFocus();
                break;
            case 4:
                msg = getString(R.string.adopt_dog_input_alert_case4);
                buttonSelectBreed.setTextColor(Color.parseColor("#d50000"));
                buttonSelectBreed.setFocusable(true);
                buttonSelectBreed.setFocusableInTouchMode(true);///add this line
                buttonSelectBreed.requestFocus();
                break;
            case 5:
                msg = getString(R.string.adopt_dog_input_alert_case5);
                String xGender = buttonSelectGender.getText().toString().trim();
                buttonSelectGender.setText("*" + " " + xGender);
                buttonSelectGender.setTextColor(Color.parseColor("#d50000"));
                buttonSelectGender.setFocusable(true);
                buttonSelectGender.setFocusableInTouchMode(true);///add this line
                buttonSelectGender.requestFocus();
                break;
            case 6:
                msg = getString(R.string.adopt_dog_input_alert_case6);
                buttonSelectGender.setTextColor(Color.parseColor("#d50000"));
                buttonSelectGender.setFocusable(true);
                buttonSelectGender.setFocusableInTouchMode(true);///add this line
                buttonSelectGender.requestFocus();
                break;
            case 7:
                msg = getString(R.string.adopt_dog_input_alert_case7);
                String xAge = buttonSelectAge.getText().toString().trim();
                buttonSelectAge.setText("*" + " " + xAge);
                buttonSelectAge.setTextColor(Color.parseColor("#d50000"));
                buttonSelectAge.setFocusable(true);
                buttonSelectAge.setFocusableInTouchMode(true);///add this line
                buttonSelectAge.requestFocus();
                break;
            case 8:
                msg = getString(R.string.adopt_dog_input_alert_case8);
                buttonSelectAge.setTextColor(Color.parseColor("#d50000"));
                buttonSelectAge.setFocusable(true);
                buttonSelectAge.setFocusableInTouchMode(true);///add this line
                buttonSelectAge.requestFocus();
                break;
            case 9:
                msg = getString(R.string.adopt_dog_input_alert_case9);
                String xLocation1 = buttonSelectLocation1.getText().toString().trim();
                buttonSelectLocation1.setText("*" + " " + xLocation1);
                buttonSelectLocation1.setTextColor(Color.parseColor("#d50000"));
                buttonSelectLocation1.setFocusable(true);
                buttonSelectLocation1.setFocusableInTouchMode(true);///add this line
                buttonSelectLocation1.requestFocus();
                break;
            case 10:
                msg = getString(R.string.adopt_dog_input_alert_case10);
                buttonSelectLocation1.setTextColor(Color.parseColor("#d50000"));
                buttonSelectLocation1.setFocusable(true);
                buttonSelectLocation1.setFocusableInTouchMode(true);///add this line
                buttonSelectLocation1.requestFocus();
                break;
            case 11:
                msg = getString(R.string.adopt_dog_input_alert_case11);
                editTextLocation2.setTextColor(Color.parseColor("#000000"));
                editTextLocation2.requestFocus();
                break;
            case 12:
                msg = getString(R.string.adopt_dog_input_alert_case12);
                String xDistrict = buttonSelectDistrict.getText().toString().trim();
                buttonSelectDistrict.setText("*" + " " + xDistrict);
                buttonSelectDistrict.setTextColor(Color.parseColor("#d50000"));
                buttonSelectDistrict.setFocusable(true);
                buttonSelectDistrict.setFocusableInTouchMode(true);///add this line
                buttonSelectDistrict.requestFocus();
                break;
            case 13:
                msg = getString(R.string.adopt_dog_input_alert_case13);
                buttonSelectDistrict.setTextColor(Color.parseColor("#d50000"));
                buttonSelectDistrict.setFocusable(true);
                buttonSelectDistrict.setFocusableInTouchMode(true);///add this line
                buttonSelectDistrict.requestFocus();
                break;
            case 14:
                msg = getString(R.string.adopt_dog_input_alert_case14);
                editTextPhone.setTextColor(Color.parseColor("#000000"));
                editTextPhone.requestFocus();
                break;
            case 15:
                msg = getString(R.string.adopt_dog_input_alert_case15);
                String xVaccinate = buttonSelectVaccinate.getText().toString().trim();
                buttonSelectVaccinate.setText("*" + " " + xVaccinate);
                buttonSelectVaccinate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectVaccinate.setFocusable(true);
                buttonSelectVaccinate.setFocusableInTouchMode(true);///add this line
                buttonSelectVaccinate.requestFocus();
                break;
            case 16:
                msg = getString(R.string.adopt_dog_input_alert_case16);
                buttonSelectVaccinate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectVaccinate.setFocusable(true);
                buttonSelectVaccinate.setFocusableInTouchMode(true);///add this line
                buttonSelectVaccinate.requestFocus();
                break;
            case 17:
                msg = getString(R.string.adopt_dog_input_alert_case17);
                String xHabit = buttonSelectHabit.getText().toString().trim();
                buttonSelectHabit.setText("*" + " " + xHabit);
                buttonSelectHabit.setTextColor(Color.parseColor("#d50000"));
                buttonSelectHabit.setFocusable(true);
                buttonSelectHabit.setFocusableInTouchMode(true);///add this line
                buttonSelectHabit.requestFocus();
                break;
            case 18:
                msg = getString(R.string.adopt_dog_input_alert_case18);
                buttonSelectHabit.setTextColor(Color.parseColor("#d50000"));
                buttonSelectHabit.setFocusable(true);
                buttonSelectHabit.setFocusableInTouchMode(true);///add this line
                buttonSelectHabit.requestFocus();
                break;
            case 19:
                msg = getString(R.string.adopt_dog_input_alert_case19);
                String xReason = buttonSelectReason.getText().toString().trim();
                buttonSelectReason.setText("*" + " " + xReason);
                buttonSelectReason.setTextColor(Color.parseColor("#d50000"));
                buttonSelectReason.setFocusable(true);
                buttonSelectReason.setFocusableInTouchMode(true);///add this line
                buttonSelectReason.requestFocus();
                break;
            case 20:
                msg = getString(R.string.adopt_dog_input_alert_case20);
                buttonSelectReason.setTextColor(Color.parseColor("#d50000"));
                buttonSelectReason.setFocusable(true);
                buttonSelectReason.setFocusableInTouchMode(true);///add this line
                buttonSelectReason.requestFocus();
                break;
            case 21:
                msg = getString(R.string.adopt_dog_input_alert_case21);
                editTextDogName.setTextColor(Color.parseColor("#000000"));
                editTextDogName.requestFocus();
                break;
            case 22:
                msg = getString(R.string.adopt_dog_input_alert_case22);
                editTextLocation2.setTextColor(Color.parseColor("#000000"));
                editTextLocation2.requestFocus();
                break;
            case 23:
                msg = getString(R.string.adopt_dog_input_alert_case23);
                editTextPhone.setTextColor(Color.parseColor("#000000"));
                editTextPhone.requestFocus();
                break;
        }

        new AlertDialog.Builder(AdoptDogInputActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.adopt_dog_input_alert_title)
                .setMessage(msg)
                .setPositiveButton(R.string.adopt_dog_input_alert_done, null)
                .setPositiveButton(R.string.adopt_dog_input_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void submit() {
        final String dog_name = editTextDogName.getText().toString().trim();
        final String breed = buttonSelectBreed.getText().toString().trim();
        final String gender = buttonSelectGender.getText().toString().trim();
        final String age = buttonSelectAge.getText().toString().trim();
        final String location1 = buttonSelectLocation1.getText().toString().trim();
        final String location2 = editTextLocation2.getText().toString().trim();
        final String district = buttonSelectDistrict.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String vaccinate = buttonSelectVaccinate.getText().toString().trim();
        final String habit = buttonSelectHabit.getText().toString().trim();
        final String reason = buttonSelectReason.getText().toString().trim();
        final String sBreed = getString(R.string.adopt_dog_input_breed);
        final String sBreed2 = getString(R.string.adopt_dog_input_breed2);
        final String sGender = getString(R.string.adopt_dog_input_gender);
        final String sGender2 = getString(R.string.adopt_dog_input_gender2);
        final String sAge = getString(R.string.adopt_dog_input_age);
        final String sAge2 = getString(R.string.adopt_dog_input_age2);
        final String sLocation1 = getString(R.string.adopt_dog_input_location1);
        final String sLocation1_2 = getString(R.string.adopt_dog_input_location1_2);
        final String sDistrict = getString(R.string.adopt_dog_input_district);
        final String sDistrict2 = getString(R.string.adopt_dog_input_district2);
        final String sVaccinate = getString(R.string.adopt_dog_input_vaccinate);
        final String sVaccinate2 = getString(R.string.adopt_dog_input_vaccinate2);
        final String sHabit = getString(R.string.adopt_dog_input_habit);
        final String sHabit2 = getString(R.string.adopt_dog_input_habit2);
        final String sReason = getString(R.string.adopt_dog_input_reason);
        final String sReason2 = getString(R.string.adopt_dog_input_reason2);

        Integer iCase;
        if (imageViewDogPhoto.getDrawable() != null && !dog_name.isEmpty() && !breed.equals(sBreed) && !breed.equals(sBreed2)
                && !gender.equals(sGender) && !gender.equals(sGender2) && !age.equals(sAge) && !age.equals(sAge2) && !location1.equals(sLocation1)
                && !location1.equals(sLocation1_2) && !location2.isEmpty() && !district.equals(sDistrict) && !district.equals(sDistrict2)
                && !phone.isEmpty() && !vaccinate.equals(sVaccinate) && !vaccinate.equals(sVaccinate2) && !habit.equals(sHabit)
                && !habit.equals(sHabit2) && !reason.equals(sReason) && !reason.equals(sReason2)) {
            if (dog_name.length() < 1 || dog_name.length() > 10) {
                iCase = 21;
                showAlertDialogError(iCase);
            } else if (dog_name.length() >= 1 || dog_name.length() <= 10) {
                if (location2.length() < 5 || location2.length() > 13) {
                    iCase = 22;
                    showAlertDialogError(iCase);
                } else if (location2.length() >= 5 || (location2.length() <= 13)) {
                    if (phone.length() < 9 || (phone.length() > 10)) {
                        iCase = 23;
                        showAlertDialogError(iCase);
                    } else if (phone.length() >= 9 || (phone.length() <= 10)) {
                        showAlertDialogSubmit();
                    }
                }
            }
        } else if (imageViewDogPhoto.getDrawable() == null || dog_name.isEmpty() || breed.equals(sBreed) || breed.equals(sBreed2)
                || gender.equals(sGender) || gender.equals(sGender2) || age.equals(sAge) || age.equals(sAge2) || location1.equals(sLocation1)
                || location1.equals(sLocation1_2) || location2.isEmpty() || district.equals(sDistrict) || district.equals(sDistrict2)
                || phone.isEmpty() || vaccinate.equals(sVaccinate) || vaccinate.equals(sVaccinate2) || habit.equals(sHabit)
                || habit.equals(sHabit2) || reason.equals(sReason) || reason.equals(sReason2)) {
            if (imageViewDogPhoto.getDrawable() == null) {
                iCase = 1;
                showAlertDialogError(iCase);
            } else if (dog_name.isEmpty()) {
                iCase = 2;
                showAlertDialogError(iCase);
            } else if (breed.equals(sBreed)) {
                iCase = 3;
                showAlertDialogError(iCase);
            } else if (breed.equals(sBreed2)) {
                iCase = 4;
                showAlertDialogError(iCase);
            } else if (gender.equals(sGender)) {
                iCase = 5;
                showAlertDialogError(iCase);
            } else if (gender.equals(sGender2)) {
                iCase = 6;
                showAlertDialogError(iCase);
            } else if (age.equals(sAge)) {
                iCase = 7;
                showAlertDialogError(iCase);
            } else if (age.equals(sAge2)) {
                iCase = 8;
                showAlertDialogError(iCase);
            } else if (location1.equals(sLocation1)) {
                iCase = 9;
                showAlertDialogError(iCase);
            } else if (location1.equals(sLocation1_2)) {
                iCase = 10;
                showAlertDialogError(iCase);
            } else if (location2.isEmpty()) {
                iCase = 11;
                showAlertDialogError(iCase);
            } else if (district.equals(sDistrict)) {
                iCase = 12;
                showAlertDialogError(iCase);
            } else if (district.equals(sDistrict2)) {
                iCase = 13;
                showAlertDialogError(iCase);
            } else if (phone.isEmpty()) {
                iCase = 14;
                showAlertDialogError(iCase);
            } else if (vaccinate.equals(sVaccinate)) {
                iCase = 15;
                showAlertDialogError(iCase);
            } else if (vaccinate.equals(sVaccinate2)) {
                iCase = 16;
                showAlertDialogError(iCase);
            } else if (habit.equals(sHabit)) {
                iCase = 17;
                showAlertDialogError(iCase);
            } else if (habit.equals(sHabit2)) {
                iCase = 18;
                showAlertDialogError(iCase);
            } else if (reason.equals(sReason)) {
                iCase = 19;
                showAlertDialogError(iCase);
            } else if (reason.equals(sReason2)) {
                iCase = 20;
                showAlertDialogError(iCase);
            }
        }
    }

    private void sendData() {
        final String sUriFDogPhoto = uri.toString();

        SharedPreferences sharedPref = getSharedPreferences("adoptDogInput", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("adoptDogInputDogPhoto", encodeTobase64(bm));
        editor.putString("adoptDogInpuDogFilepath", sUriFDogPhoto);
        editor.putString("adoptDogInputDogName", editTextDogName.getText().toString().trim());
        editor.putString("adoptDogInputBreed", buttonSelectBreed.getText().toString().trim());
        editor.putString("adoptDogInputAge", buttonSelectAge.getText().toString().trim());
        editor.putString("adoptDogInputGender", buttonSelectGender.getText().toString().trim());
        editor.putString("adoptDogInputLocation1", buttonSelectLocation1.getText().toString().trim());
        editor.putString("adoptDogInputLocation2", editTextLocation2.getText().toString().trim());
        editor.putString("adoptDogInputDistrict", buttonSelectDistrict.getText().toString().trim());
        editor.putString("adoptDogInputPhone", editTextPhone.getText().toString().trim());
        editor.putString("adoptDogInputBackupPhone", editTextBackupPhone.getText().toString().trim());
        editor.putString("adoptDogInputFacebook", editTextFacebook.getText().toString().trim());
        editor.putString("adoptDogInputLine", editTextLine.getText().toString().trim());
        editor.putString("adoptDogInputVaccinate", buttonSelectVaccinate.getText().toString().trim());
        editor.putString("adoptDogInputHabit", buttonSelectHabit.getText().toString().trim());
        editor.putString("adoptDogInputReason", buttonSelectReason.getText().toString().trim());
        editor.apply();
        Intent toAdoptPostIntent = new Intent(getApplicationContext(), AdoptDogPostActivity.class);
        startActivity(toAdoptPostIntent);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonChoosePhoto) {
            chooseImage();
        } else if (view == fabSubmit) {
            submit();
        } else if (view == buttonSelectBreed) {
            showDialogBreed();
        } else if (view == buttonSelectGender) {
            showDialogGender();
        } else if (view == buttonSelectAge) {
            showDialogAge();
        } else if (view == buttonSelectLocation1) {
            showDialogLocation1();
        } else if (view == buttonSelectDistrict) {
            showDialogDistrict();
        } else if (view == buttonSelectVaccinate) {
            showDialogVaccinate();
        } else if (view == buttonSelectHabit) {
            showDialogHabit();
        } else if (view == buttonSelectReason) {
            showDialogReason();
        }
    }

}
