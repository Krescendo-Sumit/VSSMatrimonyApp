package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;

public class OtherUserProfile extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    TextView txt_name, txt_mobile, txt_batch, txt_email, txt_dhyaasid, txt_address;
RelativeLayout rl;
    WebView dp;
    int onResumecnt=0;
    long pressedTime;
    EditText
            et_name,
            et_dob,
            et_age,
            et_email,
            et_mobile1,
            et_mobile2,
            et_height,
            et_color,
            et_gotra,
            et_qualification,
            et_jobdetails,
            et_joblocation,
            et_month_income,
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
            et_mamachekul;
    RadioButton rb_cata_bride, rb_catagroom;
    RadioButton rb_janmapatrikaYes, rb_janmapatrikaNo;
    RadioButton rb_isChasmaYes, rb_isChashmaNo;
    RadioButton rb_wishToSeeJPYes, rb_wishToSeeJPNo;
    RadioButton rb_isMangalYes, rb_isMangalNo;

String interestedUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        init();
        
    }

    private void init() {
        try{

            context = OtherUserProfile.this;
            progressDialog = new ProgressDialog(context);
            txt_name = findViewById(R.id.txt_name);
            txt_mobile = findViewById(R.id.txt_mobile);
            txt_batch = findViewById(R.id.txt_batch);
            txt_email = findViewById(R.id.txt_email);
            txt_address = findViewById(R.id.txt_address);
            txt_dhyaasid = findViewById(R.id.txt_dhyassid);
            rl = findViewById(R.id.rl);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Toast.makeText(context, "Uplaod Image", Toast.LENGTH_SHORT).show();

                    try{


                        String ff= Preferences.get(context, Preferences.USER_MOBILE);
                        String url= Constants.BASE_URL +"dp/uploadphoto.php?fname="+ff;
                        Preferences.save(context,Preferences.PHOTOURL,url);

                        Intent intent=new Intent(context, WebViewFileUploadTest.class);
                        startActivity(intent);

                    }catch (Exception e)
                    {
                        Toast.makeText(context, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                }
            });

            dp =  findViewById(R.id.dp);
            dp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            dp.getSettings().setAppCacheEnabled(false);
            dp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            et_name = findViewById(R.id.et_name);
            et_email = findViewById(R.id.et_email);
            et_dob = findViewById(R.id.et_dob);
            et_age = findViewById(R.id.et_age);
            et_mobile1 = findViewById(R.id.et_mobile1);
            et_mobile2 = findViewById(R.id.et_mobile2);
            et_height = findViewById(R.id.et_height);
            et_color = findViewById(R.id.et_color);
            et_gotra = findViewById(R.id.et_gotra);
            et_qualification = findViewById(R.id.et_qualification);
            et_jobdetails = findViewById(R.id.et_jobdetails);
            et_joblocation = findViewById(R.id.et_joblocation);
            et_month_income = findViewById(R.id.et_month_income);
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

            checkValid();




        }catch (Exception e)
        {
            
        }
    }

    private void checkValid() {
        try{

            if (CheckConnection.checkInternet(context))
                getUserProfile();
            else {
                //  Toast.makeText(Flash.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(context);
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
    public void getUserProfile() {
        try {
            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String mobile_other = Preferences.get(context, Preferences.OTHER_USER_Mobile);
            String deviceid = "111";

            String userid = Preferences.get(context, Preferences.OTHER_USER_ID);
            ;
            Call<List<UserModel>> call = RetrofitClient.getInstance().getMyApi().getUserDetails(mobile_other, userid, deviceid);
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //  Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                    try{
                    List<UserModel> MyProfiles = response.body();

                    for (UserModel v : MyProfiles) {
                        interestedUser=v.getId();
                        Log.i("Dp Path", Constants.BASE_URL + "dp/" + v.getPhotopath());
                        String url = Constants.BASE_URL + "dp/" + v.getPhotopath();
                        String erurl = Constants.BASE_URL + "dp/noimg.jpg";

                        try {
                            String data = "<html><head><meta name=\"viewport\"\"content=\"width='100%', initial-scale=0.65 \" /></head>";
                            data = data + "<body style=\"background-color:black;\"><center>" +
                                    "<img width=\"80%\" height='100%' src=\"" + url + "\" " +
                                    "onerror=\"this.onerror=null; this.src='" + erurl + "'\"/>" +
                                    "" +
                                    "</center></body></html>";
                            dp.getSettings().setJavaScriptEnabled(true);
                            dp.loadData(data, "text/html", "UTF-8");
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        txt_dhyaasid.setText(" VSS-" + v.getId());
                        txt_name.setText(" " + v.getFullname());
                        txt_email.setText(Html.fromHtml(" <b>Email Id </b><br>" + v.getEmail()));
                        txt_mobile.setText(Html.fromHtml(" <b>Mobile Number </b><br>" + v.getMobile1() + "," + v.getMobile2()));
                        txt_address.setText(Html.fromHtml(" <b>Address </b><br>" + v.getPermanent_Address()));
                        if (v.getUsercategory().equals("1"))
                            txt_batch.setText(" Category :Groom");
                        else
                            txt_batch.setText(" Category :Bride");

                        et_name.setText(v.getFullname());
                        et_dob.setText(v.getDob());
                        et_age.setText(v.getAge());
                        et_email.setText(v.getEmail());
                        et_mobile1.setText(v.getMobile1());
                        et_mobile2.setText(v.getMobile2());
                        et_height.setText(v.getHeight());
                        et_color.setText(v.getColor());
                        et_gotra.setText(v.getGotra());
                        et_qualification.setText(v.getQualification());
                        et_jobdetails.setText(v.getJobdetails());
                        et_joblocation.setText(v.getJoblocation());
                        et_month_income.setText(v.getMonthlyIncome());
                        et_localadress.setText(v.getLocal_address());
                        et_premanent_address.setText(v.getPermanent_Address());
                        et_hobbies.setText(v.getHobbies());
                        et_parner_requirment.setText(v.getPartnerRequirment());
                        et_fathername.setText(v.getFathername());
                        et_father_ocupation.setText(v.getFatherocupation());
                        et_mothername.setText(v.getMothersname());
                        et_totalbrother.setText(v.getTotalbrothers());
                        et_brotherstatus.setText(v.getBothersmaritialStatas());
                        et_totalsister.setText(v.getTotalsister());
                        et_sisterstatus.setText(v.getSistermaritialStatus());
                        et_mamachekul.setText(v.getMamacheKul());


                        if (v.getUsercategory().trim().equals("1"))
                            rb_catagroom.setChecked(true);
                        else if (v.getUsercategory().trim().equals("0"))
                            rb_cata_bride.setChecked(true);


                        if (v.getIsJanmpatrikaAvailable().trim().equals("1"))
                            rb_janmapatrikaYes.setChecked(true);
                        else if (v.getIsJanmpatrikaAvailable().trim().equals("0"))
                            rb_janmapatrikaNo.setChecked(true);


                        if (v.getIsChashma().trim().equals("1"))
                            rb_isChasmaYes.setChecked(true);
                        else if (v.getIsChashma().trim().equals("0"))
                            rb_isChashmaNo.setChecked(true);


                        if (v.getIsWishToSeeJamnpatrika().trim().equals("1"))
                            rb_wishToSeeJPYes.setChecked(true);
                        else if (v.getIsWishToSeeJamnpatrika().trim().equals("0"))
                            rb_wishToSeeJPNo.setChecked(true);


                        if (v.getIsMangal().trim().equals("1"))
                            rb_isMangalYes.setChecked(true);
                        else if (v.getIsMangal().trim().equals("0"))
                            rb_isMangalYes.setChecked(true);




                    }
                    }catch(Exception e)
                    {
                        Toast.makeText(OtherUserProfile.this, "Something went wrong : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        // getMenuList();

                    } catch (NullPointerException e) {
                        Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (
                Exception e) {

        }

    }

    public void showinterest(View view) {

        showInterest();
        Toast.makeText(context, "Show interest", Toast.LENGTH_SHORT).show();
    }

    public void chat(View view) {
        Intent intent=new Intent(context,Chating.class);
        intent.putExtra("oid",interestedUser);

        startActivity(intent);
        Toast.makeText(context, "Chating", Toast.LENGTH_SHORT).show();
    }


    private void showInterest() {
      //  Toast.makeText(context, "Calling", Toast.LENGTH_SHORT).show();
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();
            String userid=Preferences.get(context,Preferences.USER_ID).toString().trim();
         //   String interestid=Preferences.get(context,Preferences.USER_INTERESTED_ID).toString().trim();
            String interestid=interestedUser.trim();

            Call<String> call = RetrofitClient.getInstance().getMyApi().addInterest(
                  userid,interestid);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String saravMenuModels = response.body();
                        try {
                            Toast.makeText(OtherUserProfile.this, ""+saravMenuModels, Toast.LENGTH_SHORT).show();

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
            Log.i("Error is ", e.getMessage());
        }


    }


    public void showimages(View view) {
        try{

            Intent intent=new Intent(context,ProductImageViwer.class);
            startActivity(intent);

        }catch(Exception e)
        {

        }
    }
}