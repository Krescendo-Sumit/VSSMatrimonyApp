package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vishwakarma.matrimony.seva.util.Api;
import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.MyApplicationUtil;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;

public class SignUp extends AppCompatActivity implements IPickResult,AsyncTaskCompleteListener {

    String name, email, mobile, course, password, confirmpassword, address;
    RadioGroup rbg;
    RadioButton rb1, rb2, rb3;
    Context context;
    ProgressDialog progressDialog;
    Locale myLocale;
    int mYear, mMonth, mDay;
    Spinner et_color, et_month_income;
    String base64_image = "";
    int mHour, mMinute;
    private Handler mHandler;
    Uri uri=null;
    EditText
            et_name,
            et_dob,
            et_age,
            et_email,
            et_mobile1,
            et_mobile2,
            et_height,
            et_gotra,
            et_qualification,
            et_jobdetails,
            et_joblocation,
            et_localadress,
            et_premanent_address,
            et_hobbies,
            et_parner_requirment,
            et_fathername,
            et_father_ocupation,
            et_mothername,
            et_totalbrother,
            et_brotherstatus,
            et_totalsister,
            et_sisterstatus,
            et_mamachekul,
            et_password,
            et_confirmpassword,
            et_birthtime;


    int imageUploadFlag = 0;
    RadioButton rb_cata_bride, rb_catagroom;
    RadioButton rb_janmapatrikaYes, rb_janmapatrikaNo;
    RadioButton rb_isChasmaYes, rb_isChashmaNo;
    RadioButton rb_wishToSeeJPYes, rb_wishToSeeJPNo;
    RadioButton rb_isMangalYes, rb_isMangalNo;
    String color[] = {"Light", "Fair", "Medium", "Dark"};
    String anulaIncome[] = {
            "1-2 Lakh",
            "2-4 Lakh",
            "4-6 Lakh",
            "6-8 Lakh",
            "8-10 Lakh",
            "10-Above Lakh"
    };
    String maritial_array[] = {"Single", "Widowed", "Divorced"};
    String
            str_category = "0",
            str_et_name,
            str_et_dob,
            str_et_age,
            str_et_email,
            str_et_mobile1,
            str_et_mobile2,
            str_isJanmpatrika,
            str_et_height,
            str_et_color,
            str_et_gotra,
            str_isChashma,
            str_et_qualification,
            str_et_jobdetails,
            str_et_joblocation,
            str_et_month_income,
            str_et_localadress,
            str_et_premanent_address,
            str_isWishToSeeJamnpatrika,
            str_isMangal,
            str_et_hobbies,
            str_et_parner_requirment,
            str_et_fathername,
            str_et_father_ocupation,
            str_et_mothername,
            str_et_totalbrother,
            str_et_brotherstatus,
            str_et_totalsister,
            str_et_sisterstatus,
            str_et_mamachekul,
            str_et_password,
            str_et_confirmpassword, str_et_birthtime, str_sp_mariatialStatus;
    Spinner sp_mariatialStatus;
    Button btn_adhar_card;
    PickSetup setup;
    ImageView image_adharcard;
    CheckBox chk_termsandcondition;

    String file_path = null;
    Button upload;
    TextView txt_adharstatus;
    String mMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        context = SignUp.this;
        mHandler = new Handler(Looper.getMainLooper());
        setup = new PickSetup();
        init();
    }

    private void init() {

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_dob = findViewById(R.id.et_dob);
        et_age = findViewById(R.id.et_age);
        et_mobile1 = findViewById(R.id.et_mobile1);
        et_mobile2 = findViewById(R.id.et_mobile2);
        et_height = findViewById(R.id.et_height);
        et_color = findViewById(R.id.et_color);
        txt_adharstatus = findViewById(R.id.txt_adharstatus);

        ArrayAdapter arrayAdapter_color = new ArrayAdapter(context, R.layout.type_item, color);
        et_color.setAdapter(arrayAdapter_color);


        et_gotra = findViewById(R.id.et_gotra);
        et_qualification = findViewById(R.id.et_qualification);
        et_jobdetails = findViewById(R.id.et_jobdetails);
        et_joblocation = findViewById(R.id.et_joblocation);
        et_month_income = findViewById(R.id.et_month_income);
        ArrayAdapter arrayAdapter_anual = new ArrayAdapter(context, R.layout.type_item, anulaIncome);
        et_month_income.setAdapter(arrayAdapter_anual);

        et_localadress = findViewById(R.id.et_localadress);
        et_premanent_address = findViewById(R.id.et_premanent_address);
        et_hobbies = findViewById(R.id.et_hobbies);
        et_parner_requirment = findViewById(R.id.et_parner_requirment);
        et_fathername = findViewById(R.id.et_fathername);
        et_father_ocupation = findViewById(R.id.et_father_ocupation);
        et_mothername = findViewById(R.id.et_mothername);
        et_totalbrother = findViewById(R.id.et_totalbrother);
        et_brotherstatus = findViewById(R.id.et_brotherstatus);
        et_totalsister = findViewById(R.id.et_totalsister);
        et_sisterstatus = findViewById(R.id.et_sisterstatus);
        et_mamachekul = findViewById(R.id.et_mamachekul);
        et_password = findViewById(R.id.et_password);
        et_confirmpassword = findViewById(R.id.et_confirmpassword);
        et_birthtime = findViewById(R.id.et_birthtime);

        rb_cata_bride = findViewById(R.id.rb_bride);
        rb_catagroom = findViewById(R.id.rb_groom);

        rb_janmapatrikaYes = findViewById(R.id.rb_seetojanmpatrikayes);
        rb_janmapatrikaNo = findViewById(R.id.rb_seetojanmpatrikano);

        rb_isChasmaYes = findViewById(R.id.rb_chamayes);
        rb_isChashmaNo = findViewById(R.id.rb_chamano);

        rb_wishToSeeJPYes = findViewById(R.id.rb_seetojanmpatrikayes);
        rb_wishToSeeJPNo = findViewById(R.id.rb_seetojanmpatrikano);

        rb_isMangalYes = findViewById(R.id.rb_ismangalyes);
        rb_isMangalNo = findViewById(R.id.rb_ismangalno);

        btn_adhar_card = findViewById(R.id.btn_upload_adhar);
        image_adharcard = findViewById(R.id.img_adhar);
        chk_termsandcondition = findViewById(R.id.chk_termsandcondition);


        sp_mariatialStatus = findViewById(R.id.sp_maritialstatus);
        ArrayAdapter arrayAdapter_maritialstatus = new ArrayAdapter(context, R.layout.type_item, maritial_array);
        sp_mariatialStatus.setAdapter(arrayAdapter_maritialstatus);


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Wait..");


        btn_adhar_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup().setPickTypes(EPickType.CAMERA)).show(SignUp.this);

            /*    new ChooserDialog(context)
                        .withStartFile("path")
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(context, "FILE: " + path, Toast.LENGTH_SHORT).show();
                           file_path=path;
                            }
                        })
                        // to handle the back key pressed or clicked outside the dialog:
                        .withOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                Log.d("CANCEL", "CANCEL");
                                dialog.cancel(); // MUST have
                            }
                        })
                        .build()
                        .show();

*/
            }
        });

        et_dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */

                        String ssm = "", ssd = "";
                        if ((selectedmonth + 1) < 10)
                            ssm = "0" + (selectedmonth + 1);
                        else
                            ssm = "" + (selectedmonth + 1);
                        if ((selectedday) < 10)
                            ssd = "0" + selectedday;
                        else
                            ssd = "" + selectedday;

                        String dd = ssd + "-" + (ssm) + "-" + selectedyear;
                        et_dob.setText(dd);
                        et_age.setText(getAge(selectedyear, selectedmonth, selectedday));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 573948000000L);
                mDatePicker.setTitle(getResources().getString(R.string.dob));
                mDatePicker.show();

            }
        });

        et_birthtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                et_birthtime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

    }

    public void goback(View view) {
        finish();
    }

    private String getAge(int year, int month, int day) {
        Log.i("Date ", year + ":" + month + ":" + day);


        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void signup(View view) {

        try {
            str_category = "";
            if (rb_catagroom.isChecked())
                str_category = "1";
            if (rb_cata_bride.isChecked())
                str_category = "0";
            str_et_name = et_name.getText().toString().trim();
            str_et_dob = et_dob.getText().toString().trim();
            str_et_age = et_age.getText().toString().trim();
            str_et_email = et_email.getText().toString().trim();
            str_et_mobile1 = et_mobile1.getText().toString().trim();
            str_et_mobile2 = et_mobile2.getText().toString().trim();
            str_isJanmpatrika = "";
            if (rb_janmapatrikaYes.isChecked())
                str_isJanmpatrika = "1";
            if (rb_janmapatrikaNo.isChecked())
                str_isJanmpatrika = "0";
            str_et_height = et_height.getText().toString().trim();
            str_et_color = et_color.getSelectedItem().toString().trim();
            str_et_gotra = et_gotra.getText().toString().trim();

            str_isChashma = "";
            if (rb_isChasmaYes.isChecked())
                str_isChashma = "1";
            if (rb_isChashmaNo.isChecked())
                str_isChashma = "0";

            str_et_qualification = et_qualification.getText().toString().trim();
            str_et_jobdetails = et_jobdetails.getText().toString().trim();
            str_et_joblocation = et_joblocation.getText().toString().trim();
            str_et_month_income = et_month_income.getSelectedItem().toString().trim();
            str_sp_mariatialStatus = sp_mariatialStatus.getSelectedItem().toString().trim();
            str_et_localadress = et_localadress.getText().toString().trim();
            str_et_premanent_address = et_premanent_address.getText().toString().trim();
            str_isWishToSeeJamnpatrika = "";
            if (rb_wishToSeeJPYes.isChecked())
                str_isWishToSeeJamnpatrika = "1";
            if (rb_wishToSeeJPNo.isChecked())
                str_isWishToSeeJamnpatrika = "0";

            str_isMangal = "";
            if (rb_isMangalYes.isChecked())
                str_isMangal = "1";
            if (rb_isMangalNo.isChecked())
                str_isMangal = "0";

            str_et_hobbies = et_hobbies.getText().toString().trim();
            str_et_parner_requirment = et_parner_requirment.getText().toString().trim();
            str_et_fathername = et_fathername.getText().toString().trim();
            str_et_father_ocupation = et_father_ocupation.getText().toString().trim();
            str_et_mothername = et_mothername.getText().toString().trim();
            str_et_totalbrother = et_totalbrother.getText().toString().trim();
            str_et_brotherstatus = et_brotherstatus.getText().toString().trim();
            str_et_totalsister = et_totalsister.getText().toString().trim();
            str_et_sisterstatus = et_sisterstatus.getText().toString().trim();
            str_et_mamachekul = et_mamachekul.getText().toString().trim();
            str_et_password = et_password.getText().toString().trim();
            str_et_confirmpassword = et_confirmpassword.getText().toString().trim();
            str_et_birthtime = et_birthtime.getText().toString().trim();


            int flag = 0;
            if (str_category.isEmpty()) {
                flag++;
                Toast.makeText(context, "Please select Category.", Toast.LENGTH_SHORT).show();
            }
            if (str_et_name.isEmpty()) {
                flag++;
                et_name.setError("Enter Name");
                et_name.requestFocus();
            }
            if (str_et_dob.isEmpty()) {
                flag++;
                et_dob.setError("Choose DOB");
                et_dob.requestFocus();
            }
            if (str_et_age.isEmpty()) {
                flag++;
                et_age.setError("Enter Age");
                et_age.requestFocus();
            }
            if (str_et_email.isEmpty()) {
                flag++;
                et_email.setError("Enter Email");
                et_email.requestFocus();
            }
            if (str_et_mobile1.isEmpty()) {
                flag++;
                et_email.setError("Enter Email");
                et_email.requestFocus();
            }/*if(str_et_mobile2.isEmpty()){
                et_email.setError("Enter Email");
                et_email.requestFocus();
            }*/
            if (str_isJanmpatrika.isEmpty()) {
                flag++;
                Toast.makeText(context, "Choose Is Janmapatrika Available.", Toast.LENGTH_SHORT).show();
            }
            if (str_et_height.isEmpty()) {
                flag++;
                et_height.setError("Enter Height ");
                et_height.requestFocus();
            }
            if (str_et_color.isEmpty()) {
                flag++;
                Toast.makeText(context, "Please Choose your Color", Toast.LENGTH_SHORT).show();
                et_color.requestFocus();
            }
            if (str_et_gotra.isEmpty()) {
                flag++;
                et_gotra.setError("Enter Value");
                et_gotra.requestFocus();
            }
            if (str_isChashma.isEmpty()) {
                flag++;
                et_gotra.setError("Enter Value");
                et_gotra.requestFocus();
            }
            if (str_et_qualification.isEmpty()) {
                flag++;
                et_qualification.setError("Enter Value");
                et_qualification.requestFocus();

            }
            if (str_et_jobdetails.isEmpty()) {
                flag++;
                et_jobdetails.setError("Enter Value");
                et_jobdetails.requestFocus();
            }
            if (str_et_joblocation.isEmpty()) {
                flag++;
                et_joblocation.setError("Enter Value");
                et_joblocation.requestFocus();
            }
            if (str_et_month_income.isEmpty()) {
                flag++;
                Toast.makeText(context, "Please choose annual income.", Toast.LENGTH_SHORT).show();
                et_month_income.requestFocus();
            }
            if (str_et_localadress.isEmpty()) {
                flag++;
                et_localadress.setError("Enter Value");
                et_localadress.requestFocus();
            }
            if (str_et_premanent_address.isEmpty()) {
                flag++;
                et_premanent_address.setError("Enter Value");
                et_premanent_address.requestFocus();
            }
            if (str_isWishToSeeJamnpatrika.isEmpty()) {
                Toast.makeText(context, "Please select is your are intrested to see Jamnpatrika.", Toast.LENGTH_SHORT).show();
                flag++;

            }
            if (str_isMangal.isEmpty()) {
                Toast.makeText(context, "Please select is You have Magal.", Toast.LENGTH_SHORT).show();
                flag++;
            }
            if (str_et_hobbies.isEmpty()) {
                flag++;
                et_hobbies.setError("Enter Value");
                et_hobbies.requestFocus();
            }
            if (str_et_parner_requirment.isEmpty()) {
                flag++;
                et_parner_requirment.setError("Enter Value");
                et_parner_requirment.requestFocus();
            }
            if (str_et_fathername.isEmpty()) {
                flag++;
                et_fathername.setError("Enter Value");
                et_fathername.requestFocus();
            }
            if (str_et_father_ocupation.isEmpty()) {
                flag++;
                et_father_ocupation.setError("Enter Value");
                et_father_ocupation.requestFocus();
            }
            if (str_et_mothername.isEmpty()) {
                flag++;
                et_mothername.setError("Enter Value");
                et_mothername.requestFocus();
            }
            if (str_et_totalbrother.isEmpty()) {
                flag++;
                et_totalbrother.setError("Enter Value");
                et_totalbrother.requestFocus();
            }
            if (str_et_brotherstatus.isEmpty()) {
                flag++;
                et_brotherstatus.setError("Enter Value");
                et_brotherstatus.requestFocus();
            }
            if (str_et_totalsister.isEmpty()) {
                flag++;
                et_totalsister.setError("Enter Value");
                et_totalsister.requestFocus();
            }
            if (str_et_sisterstatus.isEmpty()) {
                flag++;
                et_sisterstatus.setError("Enter Value");
                et_sisterstatus.requestFocus();
            }
            if (str_et_mamachekul.isEmpty()) {
                flag++;
                et_mamachekul.setError("Enter Value");
                et_mamachekul.requestFocus();
            }
            if (str_et_password.isEmpty()) {
                flag++;
                et_password.setError("Enter Value");
                et_password.requestFocus();
            }
            if (str_et_birthtime.isEmpty()) {
                flag++;
                et_birthtime.setError("Enter Value");
                et_birthtime.requestFocus();
            }
            if (!(str_et_password.equals(str_et_confirmpassword))) {
                flag++;
                et_confirmpassword.setError("Confirm password not match.");
                et_confirmpassword.requestFocus();
            }
            if (imageUploadFlag == 0) {
                flag++;
                Toast.makeText(context, "Please Choose Adharcard photo", Toast.LENGTH_LONG).show();
            }

            if (!chk_termsandcondition.isChecked()) {
                flag++;
                Toast.makeText(context, "Please Accept Terms and Conditions", Toast.LENGTH_LONG).show();
            }


            if (flag > 0) {
                Toast.makeText(context, "You have missing " + flag + " values to enter.", Toast.LENGTH_SHORT).show();

            } else {


                if (CheckConnection.checkInternet(SignUp.this)) {
                    signingup();
                } else {
                    //  Toast.makeText(Flash.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog(SignUp.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.setContentView(R.layout.popup_retry);
                    Button btn_retry = dialog.findViewById(R.id.btn_retry);
                    btn_retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //  checkValid();
                        }
                    });
                    dialog.show();

                }


            }


        } catch (Exception e) {
            Log.i("Error in Onclick ", "" + e.getMessage());
        }
    }


    private void signingup() {
        // Toast.makeText(context, "Calling", Toast.LENGTH_SHORT).show();
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();


            Call<String> call = RetrofitClient.getInstance().getMyApi().insertUser(
                    "0",
                    str_category,
                    str_et_name,
                    str_et_dob,
                    str_et_age,
                    str_et_email,
                    str_et_mobile1,
                    str_et_mobile2,
                    str_isJanmpatrika,
                    str_et_height,
                    str_et_color,
                    str_et_gotra,
                    str_isChashma,
                    str_et_qualification,
                    str_et_jobdetails,
                    str_et_joblocation,
                    str_et_month_income,
                    str_et_localadress,
                    str_et_premanent_address,
                    str_isWishToSeeJamnpatrika,
                    str_isMangal,
                    str_et_hobbies,
                    str_et_parner_requirment,
                    str_et_fathername,
                    str_et_father_ocupation,
                    str_et_mothername,
                    str_et_totalbrother,
                    str_et_brotherstatus,
                    str_et_totalsister,
                    str_et_sisterstatus,
                    str_et_mamachekul,
                    "",
                    "",
                    str_et_password,
                    str_sp_mariatialStatus,
                    str_et_birthtime, base64_image
            );
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String saravMenuModels = response.body();
                        try {
                            //==============
                            if (saravMenuModels.contains("Mobile Number is already Exist")) {
                                Toast.makeText(SignUp.this, "User Already Reistered.", Toast.LENGTH_SHORT).show();
                            } else {

                                JSONArray jsonArray = new JSONArray(saravMenuModels.trim());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("fullname");
                                    String mobile = jsonObject.getString("mobile1");
                                    String status = jsonObject.getString("userstatus");
                                    String userid = jsonObject.getString("id");
                                    String usercategory = jsonObject.getString("usercategory");

                                    Preferences.save(context, Preferences.USER_PROFILE_NAME, name);
                                    Preferences.save(context, Preferences.USER_ID, userid);
                                    Preferences.save(context, Preferences.USER_MOBILE, mobile);
                                    Preferences.save(context, Preferences.USERCATEGORY, usercategory);
                                    Preferences.save(context, Preferences.USERSTATUS, status);

                                    Intent intent = new Intent(context, NewHome.class);
                                    startActivity(intent);

                                }
                            }

                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //=======


                    } else {
                        Toast.makeText(context, "माहिती उपलब्ध नाही.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.i("Error is ", e.getMessage());
        }


    }

    public void marathi(View v) {
        try {
            setLocale("mr");
            startActivity(getIntent());
        } catch (Exception e) {

        }
    }

    public void english(View v) {
        try {
            setLocale("en");
            startActivity(getIntent());
        } catch (Exception e) {

        }
    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
/*Intent refresh = new Intent(this, AndroidLocalize.class);
startActivity(refresh);*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        //  lblLang.setText(R.string.langselection);
        super.onConfigurationChanged(newConfig);
        Toast.makeText(context, "Languange Changed", Toast.LENGTH_SHORT).show();

        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {
            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
        } else if (newConfig.locale == Locale.FRENCH) {
            Toast.makeText(this, "French", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPickResult(PickResult r) {
        try {
            image_adharcard.setImageBitmap(r.getBitmap());

/*

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            r.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encoded=Base64.encode(b);
*/

            //  Log.i("Base64 ee:", encoded);
           /* Uri uri = r.getUri();
            File file = new File(uri.getPath());//create path from uri
            final String[] split = file.getPath().split(":");//split the path.
            String filePath = split[0];
            Log.i("Image Path",split.length+"Path : "+filePath);*/
            //    Log.i("Base64 :", MyApplicationUtil.getImageDatadetail(r.getPath()));
            file_path = r.getPath();
            uri = r.getUri();
            FileOutputStream out = new FileOutputStream(file_path);
            r.getBitmap().compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.i("Error is ", e.getMessage());
        }
    }

    public void terms(View view) {
        try {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.popup_termsandcondition);
            dialog.show();

        } catch (Exception e) {

        }
    }

    public void upload(View view) {
        try {

            if (file_path != null) {
             /*   if (!progressDialog.isShowing()) {
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Uploading adhar ...");
                    progressDialog.show();
                }*/
   //             UploadFile();
 //PDF

        // uploadPDFfile(file_path);
                progressDialog.setMessage("Adhar uploading please wait..");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                uploadFile(file_path);


            } else {
                Toast.makeText(context, "Please Select File First", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    private void UploadFile() {

        //uploadFile(file_path);
       // uploadFiles(file_path);


        UploadTask uploadTask=new UploadTask();
        uploadTask.execute(new String[]{file_path});
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equalsIgnoreCase("true")) {
                Toast.makeText(context, "Adhar Uploaded Success.", Toast.LENGTH_SHORT).show();
                imageUploadFlag = 1;
            } else {
                Toast.makeText(context, "Failed Upload", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Uploading Adhar..");
            progressDialog.show();
            //  progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
           /* if (uploadFile(strings[0])) {
                return "true";
            } else {
                return "failed";
            }*/
            return "";
        }
    }

    private void uploadFile(String path) {
        try {
          //  Toast.makeText(context, "File path"+path, Toast.LENGTH_SHORT).show();
              File file1=new File(path);


        //    File file1 = null;
            RequestBody requestBody1 = null;
            MultipartBody.Part imageFileBody1 = null;
            Bitmap bitmap = null;
           try {
                    if (file1 != null) {
                        ///      MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("PrimaryImage", file1.getName(), requestBody);
                     //u    bitmap = BitmapFactory.decodeFile(file1.getPath());
                       // bitmap.compress(Bitmap.CompressFormat.JPEG, 40, new FileOutputStream(file1));
                        requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                        imageFileBody1 = MultipartBody.Part.createFormData("files", file1.getName(), requestBody1);
                    }
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }


            Call<String> call = RetrofitClient.getInstance().getMyApi().uploadProductQualityImage(imageFileBody1,requestBody1);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(SignUp.this, " Success "+response.body(), Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(SignUp.this, "Fail "+response.body(), Toast.LENGTH_SHORT).show();


                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(SignUp.this, " Error -"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });







           // return true;
        } catch (Exception e) {
            Log.i("Error is", e.getMessage());
          //  return false;
        }
            /*File file=new File(path);
            base64_image=file.getName();
            try{
                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files",file.getName(), RequestBody.create(MediaType.parse("image/*"),file))
                        .addFormDataPart("some_key","some_value")
                        .addFormDataPart("submit","submit")
                        .build();

                Request request=new Request.Builder()
                        .url("http://crm.konarkgroup.com:83/v1/attachments/upload?moduleType=5")
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();

                client.newCall(request).enqueue(new okhttp3.Callback() {


                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                          Log.i("On Respo",""+response.body().string());
                        Toast.makeText(context, ""+response.body().string(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.i("On Respo",""+ e.getMessage());
                    }
                });
                return true;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }*/

    }

    private boolean uploadFiles(String path) {
        try {
            File file = new File(path);
     /*       Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(file));
*/
            base64_image = file.getName();
            try {
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .addFormDataPart("some_key", "some_value")
                        .addFormDataPart("submit", "submit")
                        .build();

                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "upload.php")
                        .post(requestBody)


                        .build();

                OkHttpClient client = new OkHttpClient();

                client.newCall(request).enqueue(new okhttp3.Callback() {


                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                        try {
                            Log.i("On Respo", "" + response.body().string());
                            //   String sms = response.body().string();
                            mMessage = response.toString();

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // stop("success"); // must be inside run()
                                    //  imageUploadFlag=1;
                                    txt_adharstatus.setText(mMessage);
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                            if ((response.body().string().trim()).equals("uploaded")) {
                                imageUploadFlag = 1;
                                Log.i("Log ", "" + imageUploadFlag);
                            } else {
                                imageUploadFlag = 0;
                              /* mHandler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       // stop("success"); // must be inside run()
                                       progressDialog.setMessage("Retrying to upload adhar ...");
                                       uploadFile(file_path);
                                   }
                               });*/

                            }
                        } catch (Exception e) {
                            Log.i("Error is ", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        try {
                            Log.i("On Respo", "" + e.getMessage());
                            mMessage = e.getMessage();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //  stop("" + e.getMessage()); // must be inside run()
                                    txt_adharstatus.setText(mMessage);
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } catch (Exception e1) {

                        }
                    }
                });
                return true;
            } catch (Exception e) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            return false;
        }
    }

    private void stop(String message) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Upload : " + message, Toast.LENGTH_SHORT).show();

    }



    private void uploadPDFfile(String path) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", Constants.BASE_URL+"upload.php");
        map.put("filename", path);
        new MultiPartRequester(this, map, 111, this);
        Toast.makeText(context, "File  UplaodStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        Log.d("res", response);
/*        switch (serviceCode) {

            case GALLERY:
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject.toString().replace("\\\\","");
                    if (jsonObject.getString("status").equals("true")) {

                        arraylist = new ArrayList<HashMap<String, String>>();
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataobj = dataArray.getJSONObject(i);
                            url = dataobj.optString("pathToFile");
                        }
                        tv.setText(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }*/
    }




}