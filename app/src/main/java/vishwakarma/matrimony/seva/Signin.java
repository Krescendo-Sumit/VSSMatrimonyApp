package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import vishwakarma.matrimony.seva.model.SignInModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signin extends AppCompatActivity {
    ProgressDialog progressDialog;
    Context context;
    EditText et_mobile, et_pass;

    String uname, pass, deviceid;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        context = Signin.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        et_mobile = findViewById(R.id.et_uname);
        et_pass = findViewById(R.id.et_pass);
        try {
      /*      Resources res = context.getResources();
// Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(new Locale("mr")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
            res.updateConfiguration(conf, dm);*/

            //   setLocale("mr");
        } catch (Exception e) {
            Toast.makeText(context, "Langiange Translator : " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void login(View v) {
        try {

            uname = et_mobile.getText().toString().trim();
            pass = et_pass.getText().toString().trim();
            deviceid = "111";

            if (CheckConnection.checkInternet(Signin.this))
                getBhartiList();
            else {
                //  Toast.makeText(Flash.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(Signin.this);
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


        } catch (Exception e) {

        }
    }

    private void getBhartiList() {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            Call<List<SignInModel>> call = RetrofitClient.getInstance().getMyApi().checkLogin(uname, pass, deviceid);
            call.enqueue(new Callback<List<SignInModel>>() {
                @Override
                public void onResponse(Call<List<SignInModel>> call, Response<List<SignInModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.body() != null) {
                        List<SignInModel> videos = response.body();

                        for (SignInModel v : videos) {

                            Preferences.save(context, Preferences.USER_MOBILE, v.getMobile1());
                            Preferences.save(context, Preferences.USER_ID, v.getId());
                            Preferences.save(context, Preferences.USER_PROFILE_NAME, v.getFullname());
                            Preferences.save(context, Preferences.USERCATEGORY, v.getUsercategory());
                            Preferences.save(context, Preferences.USERSTATUS, v.getUserstatus());

               

                            //  Log.i("Values For Instale : ",v.getLocaltoken());
                            //  Preferences.save(context, Preferences.INSTALLID, v.getLocaltoken());
               /*         Preferences.save(context,Preferences.USERMOBILE,v.getMobile());
                        Preferences.save(context,Preferences.USERCOURSENAME,v.getCourseid());
                        Preferences.save(context,Preferences.USERDAYS,v.getDays());
                        Preferences.save(context,Preferences.USERSUBCOURSE,v.getSubcourse());*/

                        }
                        finish();
                        Intent intent = new Intent(context, NewHome.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Signin.this, "Please Enter Valid Mobile and Password", Toast.LENGTH_SHORT).show();
                    }
                }



                @Override
                public void onFailure(Call<List<SignInModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }
    }

    public void signup(View view) {
        Intent intent = new Intent(context, SignUp.class);
        startActivity(intent);

        //setLocale("en");
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

    public void forgetpassword(View view)
    {
        try{

            Intent intent=new Intent(context,ForgetPassword.class);
            startActivity(intent);

        }catch(Exception e)
        {

        }
    }
}