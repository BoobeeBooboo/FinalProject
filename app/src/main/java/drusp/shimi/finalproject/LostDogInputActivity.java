package drusp.shimi.finalproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class LostDogInputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 300;

    private String mSelectedBreed;
    private String mSelectedGender;
    private String mSelectedAge;
    private String mSelectedLocation1;
    private String mSelectedDistrict;
    private String mSelectedVaccinate;
    private String mSelectedHabit;
    private String mSelectedCollar;

    private ImageView imageViewDogPhoto;

    private EditText editTextDogName;
    private EditText editTextLocation2;
    private EditText editTextPhone;
    private EditText editTextBackupPhone;
    private EditText editTextFacebook;
    private EditText editTextLine;
    private EditText editTextReward;

    private Button buttonChoosePhoto;
    private Button buttonSelectBreed;
    private Button buttonSelectGender;
    private Button buttonSelectAge;
    private Button buttonSelectLocation1;
    private Button buttonSelectDistrict;
    private Button buttonSelectLostDate;
    private Button buttonSelectVaccinate;
    private Button buttonSelectHabit;
    private Button buttonSelectCollar;

    private FloatingActionButton fabSubmit;

    private DatePickerDialog datePickerDialog;

    private Uri uriDogPhoto;

    private Bitmap bm;

    private String encodeTobase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_dog_input);

        buttonChoosePhoto = (Button) findViewById(R.id.btn_lost_dog_input_choose);
        buttonSelectBreed = (Button) findViewById(R.id.btn_lost_dog_input_breed);
        buttonSelectGender = (Button) findViewById(R.id.btn_lost_dog_input_gender);
        buttonSelectAge = (Button) findViewById(R.id.btn_lost_dog_input_age);
        buttonSelectLocation1 = (Button) findViewById(R.id.btn_lost_dog_input_location1);
        buttonSelectDistrict = (Button) findViewById(R.id.btn_lost_dog_input_district);
        buttonSelectLostDate = (Button) findViewById(R.id.btn_lost_dog_input_lost_date);
        buttonSelectVaccinate = (Button) findViewById(R.id.btn_lost_dog_input_vaccinate);
        buttonSelectHabit = (Button) findViewById(R.id.btn_lost_dog_input_habit);
        buttonSelectCollar = (Button) findViewById(R.id.btn_lost_dog_input_collar);

        imageViewDogPhoto = (ImageView) findViewById(R.id.iv_lost_dog_input_photo);

        editTextDogName = (EditText) findViewById(R.id.et_lost_dog_input_dog_name);
        editTextLocation2 = (EditText) findViewById(R.id.et_lost_dog_input_location2);
        editTextPhone = (EditText) findViewById(R.id.et_lost_dog_input_phone);
        editTextBackupPhone = (EditText) findViewById(R.id.et_lost_dog_input_backup_phone);
        editTextFacebook = (EditText) findViewById(R.id.et_lost_dog_input_facebook);
        editTextReward = (EditText) findViewById(R.id.et_lost_dog_input_reward);
        editTextLine = (EditText) findViewById(R.id.et_lost_dog_input_line);

        fabSubmit = (FloatingActionButton) findViewById(R.id.fab_lost_dog_input_submit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lost_dog_input);

        buttonChoosePhoto.setOnClickListener(this);
        buttonSelectBreed.setOnClickListener(this);
        buttonSelectGender.setOnClickListener(this);
        buttonSelectAge.setOnClickListener(this);
        buttonSelectLocation1.setOnClickListener(this);
        buttonSelectDistrict.setOnClickListener(this);
        buttonSelectLostDate.setOnClickListener(this);
        buttonSelectVaccinate.setOnClickListener(this);
        buttonSelectHabit.setOnClickListener(this);
        buttonSelectCollar.setOnClickListener(this);

        fabSubmit.setOnClickListener(this);

        // perform click event on edit text
        buttonSelectLostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(LostDogInputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if (month < 10) {
                                    formattedMonth = "0" + month;
                                }
                                if (dayOfMonth < 10) {
                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }
                                buttonSelectLostDate.setText(formattedDayOfMonth + "/"
                                        + formattedMonth + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                buttonSelectLostDate.setTextColor(Color.parseColor("#000000"));
            }
        });
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

    private void showAlertDialogSubmit() {
        new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.lost_dog_input_alert_title)
                .setMessage(R.string.lost_dog_input_alert_msg)
                .setPositiveButton(R.string.lost_dog_input_alert_done, null)
                .setNegativeButton(R.string.lost_dog_input_alert_cancel, null)
                .setPositiveButton(R.string.lost_dog_input_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendData();
                    }
                })
                .setNegativeButton(R.string.lost_dog_input_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showDialogBreed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_breed);

        final String[] items = getResources().getStringArray(R.array.breedTH);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedBreed = items[which];
                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectBreed.setText(mSelectedBreed);
                        buttonSelectBreed.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_gender);

        final String[] items = getResources().getStringArray(R.array.gender);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedGender = items[which];

                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectGender.setText(mSelectedGender);
                        buttonSelectGender.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_age);

//        final String[] items = getResources().getStringArray(R.array.age);
        final String[] items = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedAge = items[which];
                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectAge.setText(mSelectedAge);
                        buttonSelectAge.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_location1);

        final String[] items = getResources().getStringArray(R.array.location);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedLocation1 = items[which];

                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectLocation1.setText(mSelectedLocation1);
                        buttonSelectLocation1.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_district);

        final String[] items = getResources().getStringArray(R.array.district);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedDistrict = items[which];

                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectDistrict.setText(mSelectedDistrict);
                        buttonSelectDistrict.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_vaccinate);

        final String[] items = getResources().getStringArray(R.array.vaccinate);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedVaccinate = items[which];

                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectVaccinate.setText(mSelectedVaccinate);
                        buttonSelectVaccinate.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_habit);

        final String[] items = getResources().getStringArray(R.array.habit);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedHabit = items[which];

                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectHabit.setText(mSelectedHabit);
                        buttonSelectHabit.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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

    public void showDialogCollar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.lost_dog_input_alert_collar);

        final String[] items = getResources().getStringArray(R.array.collar);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedCollar = items[which];

                    }
                });
        String positiveText = getString(R.string.lost_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectCollar.setText(mSelectedCollar);
                        buttonSelectCollar.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.lost_dog_input_alert_cancel);
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

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriDogPhoto = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uriDogPhoto);
                imageViewDogPhoto.setImageBitmap(bm);
                encodeTobase64(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showAlertDialogError(int iCase) {
        String msg = null;
        switch (iCase) {
            case 1:
                msg = getString(R.string.lost_dog_input_alert_case1);
                buttonChoosePhoto.requestFocus();
                break;
            case 2:
                msg = getString(R.string.lost_dog_input_alert_case2);
                editTextDogName.setTextColor(Color.parseColor("#000000"));
                editTextDogName.requestFocus();
                break;
            case 3:
                msg = getString(R.string.lost_dog_input_alert_case3);
                String xBreed = buttonSelectBreed.getText().toString().trim();
                buttonSelectBreed.setText("*" + " " + xBreed);
                buttonSelectBreed.setTextColor(Color.parseColor("#d50000"));
                buttonSelectBreed.setFocusable(true);
                buttonSelectBreed.setFocusableInTouchMode(true);///add this line
                buttonSelectBreed.requestFocus();
                break;
            case 4:
                msg = getString(R.string.lost_dog_input_alert_case4);
                buttonSelectBreed.setTextColor(Color.parseColor("#d50000"));
                buttonSelectBreed.setFocusable(true);
                buttonSelectBreed.setFocusableInTouchMode(true);///add this line
                buttonSelectBreed.requestFocus();
                break;
            case 5:
                msg = getString(R.string.lost_dog_input_alert_case5);
                String xGender = buttonSelectGender.getText().toString().trim();
                buttonSelectGender.setText("*" + " " + xGender);
                buttonSelectGender.setTextColor(Color.parseColor("#d50000"));
                buttonSelectGender.setFocusable(true);
                buttonSelectGender.setFocusableInTouchMode(true);///add this line
                buttonSelectGender.requestFocus();
                break;
            case 6:
                msg = getString(R.string.lost_dog_input_alert_case6);
                buttonSelectGender.setTextColor(Color.parseColor("#d50000"));
                buttonSelectGender.setFocusable(true);
                buttonSelectGender.setFocusableInTouchMode(true);///add this line
                buttonSelectGender.requestFocus();
                break;
            case 7:
                msg = getString(R.string.lost_dog_input_alert_case7);
                String xAge = buttonSelectAge.getText().toString().trim();
                buttonSelectAge.setText("*" + " " + xAge);
                buttonSelectAge.setTextColor(Color.parseColor("#d50000"));
                buttonSelectAge.setFocusable(true);
                buttonSelectAge.setFocusableInTouchMode(true);///add this line
                buttonSelectAge.requestFocus();
                break;
            case 8:
                msg = getString(R.string.lost_dog_input_alert_case8);
                buttonSelectAge.setTextColor(Color.parseColor("#d50000"));
                buttonSelectAge.setFocusable(true);
                buttonSelectAge.setFocusableInTouchMode(true);///add this line
                buttonSelectAge.requestFocus();
                break;
            case 9:
                msg = getString(R.string.lost_dog_input_alert_case9);
                String xLocation1 = buttonSelectLocation1.getText().toString().trim();
                buttonSelectLocation1.setText("*" + " " + xLocation1);
                buttonSelectLocation1.setTextColor(Color.parseColor("#d50000"));
                buttonSelectLocation1.setFocusable(true);
                buttonSelectLocation1.setFocusableInTouchMode(true);///add this line
                buttonSelectLocation1.requestFocus();
                break;
            case 10:
                msg = getString(R.string.lost_dog_input_alert_case10);
                buttonSelectLocation1.setTextColor(Color.parseColor("#d50000"));
                buttonSelectLocation1.setFocusable(true);
                buttonSelectLocation1.setFocusableInTouchMode(true);///add this line
                buttonSelectLocation1.requestFocus();
                break;
            case 11:
                msg = getString(R.string.lost_dog_input_alert_case11);
                editTextLocation2.setTextColor(Color.parseColor("#000000"));
                editTextLocation2.requestFocus();
                break;
            case 12:
                msg = getString(R.string.lost_dog_input_alert_case12);
                String xDistrict = buttonSelectDistrict.getText().toString().trim();
                buttonSelectDistrict.setText("*" + " " + xDistrict);
                buttonSelectDistrict.setTextColor(Color.parseColor("#d50000"));
                buttonSelectDistrict.setFocusable(true);
                buttonSelectDistrict.setFocusableInTouchMode(true);///add this line
                buttonSelectDistrict.requestFocus();
                break;
            case 13:
                msg = getString(R.string.lost_dog_input_alert_case13);
                buttonSelectDistrict.setTextColor(Color.parseColor("#d50000"));
                buttonSelectDistrict.setFocusable(true);
                buttonSelectDistrict.setFocusableInTouchMode(true);///add this line
                buttonSelectDistrict.requestFocus();
                break;
            case 14:
                msg = getString(R.string.lost_dog_input_alert_case14);
                String xLost_date = buttonSelectLostDate.getText().toString().trim();
                buttonSelectLostDate.setText("*" + " " + xLost_date);
                buttonSelectLostDate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectLostDate.setFocusable(true);
                buttonSelectLostDate.setFocusableInTouchMode(true);///add this line
                buttonSelectLostDate.requestFocus();
                break;
            case 15:
                msg = getString(R.string.lost_dog_input_alert_case15);
                buttonSelectLostDate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectLostDate.setFocusable(true);
                buttonSelectLostDate.setFocusableInTouchMode(true);///add this line
                buttonSelectLostDate.requestFocus();
                break;
            case 16:
                msg = getString(R.string.lost_dog_input_alert_case16);
                editTextPhone.setTextColor(Color.parseColor("#000000"));
                editTextPhone.requestFocus();
                break;
            case 17:
                msg = getString(R.string.lost_dog_input_alert_case17);
                String xVaccinate = buttonSelectVaccinate.getText().toString().trim();
                buttonSelectVaccinate.setText("*" + " " + xVaccinate);
                buttonSelectVaccinate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectVaccinate.setFocusable(true);
                buttonSelectVaccinate.setFocusableInTouchMode(true);///add this line
                buttonSelectVaccinate.requestFocus();
                break;
            case 18:
                msg = getString(R.string.lost_dog_input_alert_case18);
                buttonSelectVaccinate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectVaccinate.setFocusable(true);
                buttonSelectVaccinate.setFocusableInTouchMode(true);///add this line
                buttonSelectVaccinate.requestFocus();
                break;
            case 19:
                msg = getString(R.string.lost_dog_input_alert_case19);
                String xHabit = buttonSelectHabit.getText().toString().trim();
                buttonSelectHabit.setText("*" + " " + xHabit);
                buttonSelectHabit.setTextColor(Color.parseColor("#d50000"));
                buttonSelectHabit.setFocusable(true);
                buttonSelectHabit.setFocusableInTouchMode(true);///add this line
                buttonSelectHabit.requestFocus();
                break;
            case 20:
                msg = getString(R.string.lost_dog_input_alert_case20);
                buttonSelectHabit.setTextColor(Color.parseColor("#d50000"));
                buttonSelectHabit.setFocusable(true);
                buttonSelectHabit.setFocusableInTouchMode(true);///add this line
                buttonSelectHabit.requestFocus();
                break;
            case 21:
                msg = getString(R.string.lost_dog_input_alert_case21);
                String xCollar = buttonSelectCollar.getText().toString().trim();
                buttonSelectCollar.setText("*" + " " + xCollar);
                buttonSelectCollar.setTextColor(Color.parseColor("#d50000"));
                buttonSelectCollar.setFocusable(true);
                buttonSelectCollar.setFocusableInTouchMode(true);///add this line
                buttonSelectCollar.requestFocus();
                break;
            case 22:
                msg = getString(R.string.lost_dog_input_alert_case22);
                buttonSelectCollar.setTextColor(Color.parseColor("#d50000"));
                buttonSelectCollar.setFocusable(true);
                buttonSelectCollar.setFocusableInTouchMode(true);///add this line
                buttonSelectCollar.requestFocus();
                break;
            case 23:
                msg = getString(R.string.lost_dog_input_alert_case23);
                editTextDogName.setTextColor(Color.parseColor("#000000"));
                editTextDogName.requestFocus();
                break;
            case 24:
                msg = getString(R.string.lost_dog_input_alert_case24);
                editTextLocation2.setTextColor(Color.parseColor("#000000"));
                editTextLocation2.requestFocus();
                break;
            case 25:
                msg = getString(R.string.lost_dog_input_alert_case25);
                editTextPhone.setTextColor(Color.parseColor("#000000"));
                editTextPhone.requestFocus();
                break;
            case 26:
                msg = getString(R.string.lost_dog_input_alert_case26);
                editTextBackupPhone.setTextColor(Color.parseColor("#000000"));
                editTextBackupPhone.requestFocus();
                break;
            case 27:
                msg = getString(R.string.lost_dog_input_alert_case27);
                editTextFacebook.setTextColor(Color.parseColor("#000000"));
                editTextFacebook.requestFocus();
                break;
            case 28:
                msg = getString(R.string.lost_dog_input_alert_case28);
                editTextLine.setTextColor(Color.parseColor("#000000"));
                editTextLine.requestFocus();
                break;
        }

        new AlertDialog.Builder(LostDogInputActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.lost_dog_input_alert_title)
                .setMessage(msg)
                .setPositiveButton(R.string.lost_dog_input_alert_done, null)
                .setPositiveButton(R.string.lost_dog_input_alert_done, new DialogInterface.OnClickListener() {
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
        final String lost_date = buttonSelectLostDate.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String backup_phone = editTextBackupPhone.getText().toString().trim();
        final String facebook = editTextFacebook.getText().toString().trim();
        final String line = editTextLine.getText().toString().trim();
        final String vaccinate = buttonSelectVaccinate.getText().toString().trim();
        final String habit = buttonSelectHabit.getText().toString().trim();
        final String collar = buttonSelectCollar.getText().toString().trim();
        final String sBreed = getString(R.string.lost_dog_input_breed);
        final String sBreed2 = getString(R.string.lost_dog_input_breed2);
        final String sGender = getString(R.string.lost_dog_input_gender);
        final String sGender2 = getString(R.string.lost_dog_input_gender2);
        final String sAge = getString(R.string.lost_dog_input_age);
        final String sAge2 = getString(R.string.lost_dog_input_age2);
        final String sLocation1 = getString(R.string.lost_dog_input_location1);
        final String sLocation1_2 = getString(R.string.lost_dog_input_location1_2);
        final String sDistrict = getString(R.string.lost_dog_input_district);
        final String sDistrict2 = getString(R.string.lost_dog_input_district2);
        final String sLostDate = getString(R.string.lost_dog_input_lost_date);
        final String sLostDate2 = getString(R.string.lost_dog_input_lost_date2);
        final String sVaccinate = getString(R.string.lost_dog_input_vaccinate);
        final String sVaccinate2 = getString(R.string.lost_dog_input_vaccinate2);
        final String sHabit = getString(R.string.lost_dog_input_habit);
        final String sHabit2 = getString(R.string.lost_dog_input_habit2);
        final String sCollar = getString(R.string.lost_dog_input_collar);
        final String sCollar2 = getString(R.string.lost_dog_input_collar2);

        Integer iCase;
        if (imageViewDogPhoto.getDrawable() != null && !dog_name.isEmpty() && !breed.equals(sBreed) && !breed.equals(sBreed2)
                && !gender.equals(sGender) && !gender.equals(sGender2) && !age.equals(sAge) && !age.equals(sAge2) && !location1.equals(sLocation1)
                && !location1.equals(sLocation1_2) && !location2.isEmpty() && !district.equals(sDistrict) && !district.equals(sDistrict2)
                && !lost_date.equals(sLostDate) && !lost_date.equals(sLostDate2) && !phone.isEmpty() && !vaccinate.equals(sVaccinate) && !vaccinate.equals(sVaccinate2)
                && !habit.equals(sHabit) && !habit.equals(sHabit2) && !collar.equals(sCollar) && !collar.equals(sCollar2)) {
            if (dog_name.length() < 1 || dog_name.length() > 10) {
                iCase = 23;
                showAlertDialogError(iCase);
            } else if (dog_name.length() >= 1 || dog_name.length() <= 10) {
                if (location2.length() < 5 || location2.length() > 13) {
                    iCase = 24;
                    showAlertDialogError(iCase);
                } else if (location2.length() >= 5 || (location2.length() <= 13)) {
                    if (phone.length() < 9 || (phone.length() > 10)) {
                        iCase = 25;
                        showAlertDialogError(iCase);
                    } else if (phone.length() >= 9 || (phone.length() <= 10)) {
                        showAlertDialogSubmit();
                    }
                }
            }
        } else if (imageViewDogPhoto.getDrawable() == null || dog_name.isEmpty() || breed.equals(sBreed) || breed.equals(sBreed2)
                || gender.equals(sGender) || gender.equals(sGender2) || age.equals(sAge) || age.equals(sAge2) || location1.equals(sLocation1)
                || location1.equals(sLocation1_2) || location2.isEmpty() || district.equals(sDistrict) || district.equals(sDistrict2)
                || lost_date.equals(sLostDate) || lost_date.equals(sLostDate2) || phone.isEmpty() || vaccinate.equals(sVaccinate) || vaccinate.equals(sVaccinate2)
                || habit.equals(sHabit) || habit.equals(sHabit2) || collar.equals(sCollar) || collar.equals(sCollar2)) {
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
            } else if (lost_date.equals(sLostDate)) {
                iCase = 14;
                showAlertDialogError(iCase);
            } else if (lost_date.equals(sLostDate2)) {
                iCase = 15;
                showAlertDialogError(iCase);
            } else if (phone.isEmpty()) {
                iCase = 16;
                showAlertDialogError(iCase);
            } else if (vaccinate.equals(sVaccinate)) {
                iCase = 17;
                showAlertDialogError(iCase);
            } else if (vaccinate.equals(sVaccinate2)) {
                iCase = 18;
                showAlertDialogError(iCase);
            } else if (habit.equals(sHabit)) {
                iCase = 19;
                showAlertDialogError(iCase);
            } else if (habit.equals(sHabit2)) {
                iCase = 20;
                showAlertDialogError(iCase);
            } else if (collar.equals(sCollar)) {
                iCase = 21;
                showAlertDialogError(iCase);
            } else if (collar.equals(sCollar2)) {
                iCase = 22;
                showAlertDialogError(iCase);
            }
        }
    }

    private void sendData() {
        final String sUriDogPhoto = uriDogPhoto.toString();

        SharedPreferences sharedPref = getSharedPreferences("lostDogInput", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lostDogInputDogPhoto", encodeTobase64(bm));
        editor.putString("lostDogInputDogFilepath", sUriDogPhoto);
        editor.putString("lostDogInputDogName", editTextDogName.getText().toString().trim());
        editor.putString("lostDogInputBreed", buttonSelectBreed.getText().toString().trim());
        editor.putString("lostDogInputGender", buttonSelectGender.getText().toString().trim());
        editor.putString("lostDogInputAge", buttonSelectAge.getText().toString().trim());
        editor.putString("lostDogInputLocation1", buttonSelectLocation1.getText().toString().trim());
        editor.putString("lostDogInputLocation2", editTextLocation2.getText().toString().trim());
        editor.putString("lostDogInputDistrict", buttonSelectDistrict.getText().toString().trim());
        editor.putString("lostDogInputLostDate", buttonSelectLostDate.getText().toString().trim());
        editor.putString("lostDogInputPhone", editTextPhone.getText().toString().trim());
        editor.putString("lostDogInputBackupPhone", editTextBackupPhone.getText().toString().trim());
        editor.putString("lostDogInputFacebook", editTextFacebook.getText().toString().trim());
        editor.putString("lostDogInputLine", editTextLine.getText().toString().trim());
        editor.putString("lostDogInputVaccinate", buttonSelectVaccinate.getText().toString().trim());
        editor.putString("lostDogInputHabit", buttonSelectHabit.getText().toString().trim());
        editor.putString("lostDogInputCollar", buttonSelectCollar.getText().toString().trim());
        editor.putString("lostDogInputReward", editTextReward.getText().toString().trim());
        editor.apply();
        Intent toLostDogLocationIntent = new Intent(getApplicationContext(), LostDogLocationActivity.class);
        startActivity(toLostDogLocationIntent);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonChoosePhoto) {
            showFileChooser();
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
        } else if (view == buttonSelectCollar) {
            showDialogCollar();
        }
    }

}
