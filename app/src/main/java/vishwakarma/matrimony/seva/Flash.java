package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Flash extends AppCompatActivity {
    ImageView btnimg;
    Context context;
    String data = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        context = Flash.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait.");

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        btnimg = findViewById(R.id.img_btn);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.logoanim);

        btnimg.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                try {

                    //  Toast.makeText(Flash.this, ""+db.getUserID(), Toast.LENGTH_SHORT).show();

                    String mobile = Preferences.get(context, Preferences.USER_MOBILE);
                    if (mobile != null && (!mobile.trim().equals(""))) {
      //    checkValid();
                        /*      Intent intent = new Intent(context, CourseList.class);
                        startActivity(intent);*/

                        finish();
                       Intent intent = new Intent(context, NewHome.class);
                     //   Intent intent = new Intent(context, Signin.class);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(context, Signin.class);
                        startActivity(intent);
                    }
               /*     Intent intent = new Intent(context, Signin.class);
                    startActivity(intent);*/
                } catch (Exception e) {
                    Toast.makeText(Flash.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Preferences.save(context, Preferences.FEESSTATUS, "0");
        Preferences.save(context, Preferences.FEESDATA, "");


    }

    private void checkValid() {
        try{

            if (CheckConnection.checkInternet(Flash.this))
                validateFees();
            else {
                //  Toast.makeText(Flash.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(Flash.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_retry);
                Button btn_retry=dialog.findViewById(R.id.btn_retry);
                btn_retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        checkValid();
                    }
                });
                dialog.show();

            }

        }catch (Exception e)
        {

        }
    }


    private void validateFees() {
        try {
            data = "";
            if (!progressDialog.isShowing())
                progressDialog.show();

            String uid = Preferences.get(context, Preferences.USER_ID);


            Call<String> call = RetrofitClient.getInstance().getMyApi().validateFees(uid);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String saravMenuModels = response.body().trim();
                        try {

                            String day1, day2, day3, day4, day5, day6;

                            try {
                                JSONArray jsonArray = new JSONArray(saravMenuModels);
                                JSONObject jsonObject = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    day1 = jsonObject.getString("DAY1").trim();
                                    day2 = jsonObject.getString("DAY2").trim();
                                    day3 = jsonObject.getString("DAY3").trim();
                                    day4 = jsonObject.getString("DAY4").trim();
                                    day5 = jsonObject.getString("DAY5").trim();
                                    day6 = jsonObject.getString("DAY6").trim();
                                    Log.i("Task 1", " " + day1);
                                    Log.i("Task 2", " " + day2);
                                    Log.i("Task 3", " " + day3);
                                    Log.i("Task 4", " " + day4);
                                    Log.i("Task 5", " " + day5);
                                    Log.i("Task 6", " " + day6);

                                    if (!(day1.equals("No"))) {

                                        int k = Integer.parseInt(day1.trim());
                                        if (jsonObject.getString("st1").trim().equals("0") && k > -10) {
                                            data += "\n Installment 1 is Pending , Please pay it as early as possible.\n Installment Date :" + jsonObject.getString("ints_1date").trim() + " (YYYY-MM-DD)";
                                            Preferences.save(context, Preferences.FEESSTATUS, "1");
                                            Log.i("Pass 1", " " + data);
                                        }//        1121
                                    }
                                    if (!(day2.equals("No"))) {
                                        int k = Integer.parseInt(day2.trim());
                                        Log.i("Entered", " Enter 2");
                                        if (jsonObject.getString("st2").trim().equals("0") && k > -10) {
                                            data += "\n Installment 2 is Pending , Please pay it as early as possible.\n Installment Date :" + jsonObject.getString("ints_2date").trim() + " (YYYY-MM-DD)";
                                            Preferences.save(context, Preferences.FEESSTATUS, "1");
                                            Log.i("Pass 2", " " + data);
                                        }
                                    }
                                    if (!(day3.equals("No"))) {
                                        int k = Integer.parseInt(day3.trim());
                                        if (jsonObject.getString("st3").trim().equals("0") && k > -10) {
                                            data += "\n Installment 3 is Pending , Please pay it as early as possible.\n Installment Date :" + jsonObject.getString("ints_3date").trim() + " (YYYY-MM-DD)";
                                            Preferences.save(context, Preferences.FEESSTATUS, "1");
                                            Log.i("Pass 3", " " + data);
                                        }
                                    }
                                    if (!(day4.equals("No"))) {
                                        int k = Integer.parseInt(day4.trim());
                                        if (jsonObject.getString("st4").trim().equals("0") && k > -10) {
                                            data += "\n Installment 4 is Pending , Please pay it as early as possible.\n Installment Date :" + jsonObject.getString("ints_4date").trim() + " (YYYY-MM-DD)";
                                            Preferences.save(context, Preferences.FEESSTATUS, "1");
                                            Log.i("Pass 4", " " + data);
                                        }
                                    }
                                    if (!(day5.equals("No"))) {
                                        int k = Integer.parseInt(day5.trim());
                                        if (jsonObject.getString("st5").trim().equals("0") && k > -10) {
                                            data += "\n Installment 5 is Pending , Please pay it as early as possible.\n Installment Date :" + jsonObject.getString("ints_5date").trim() + " (YYYY-MM-DD)";
                                            Preferences.save(context, Preferences.FEESSTATUS, "1");
                                            Log.i("Pass 5", " " + data);
                                        }
                                    }
                                    if (!(day6.equals("No"))) {
                                        int k = Integer.parseInt(day6.trim());
                                        if (jsonObject.getString("st6").trim().equals("0") && k > -10) {
                                            data += "\n Installment 6 is Pending , Please pay it as early as possible.\n Installment Date :" + jsonObject.getString("ints_6date").trim() + " (YYYY-MM-DD)";
                                            Preferences.save(context, Preferences.FEESSTATUS, "1");
                                            Log.i("Pass 6", " " + data);
                                        }
                                    }
                                    data += "\n Ignore this message if you paid already.\n Thank you.";
                                    Preferences.save(context, Preferences.FEESDATA, data);
                                }
                            } catch (Exception e) {
                                //     Toast.makeText(Flash.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            // Toast.makeText(Flash.this, ""+data+" Status "+Preferences.get(context, Preferences.FEESSTATUS), Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(context, Home.class);
                            startActivity(intent);

                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
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

        }


    }


}