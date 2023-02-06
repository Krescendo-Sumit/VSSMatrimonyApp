package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import vishwakarma.matrimony.seva.adapter.BhartiAdapter;
import vishwakarma.matrimony.seva.adapter.RequestProfileAdapter;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseList extends AppCompatActivity implements RequestProfileAdapter.EventListener {
    BhartiAdapter adapter1;

    LinearLayoutManager mManager;
    Context context;
    ProgressDialog progressDialog;

    TextView  txt_subcousename;
    LinearLayout ll;
    int active_flag = 1;


    RequestProfileAdapter adapter11;
    RecyclerView recyclerView_bhartilist;
    ProgressBar progressBar;
    TextView txt_precentageText;

    String id = "";
    String InstallId = "";

    String subcourseid, username, coursename, courseid, days;
    String requestType="0";
    private long pressedTime;
    int categorycnt=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        context = CourseList.this;
        //  recyclerView_bhartilist = (RecyclerView) findViewById(R.id.rc_listofexams);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        recyclerView_bhartilist = (RecyclerView) findViewById(R.id.rc_listofexams);
        mManager = new LinearLayoutManager(context);
        recyclerView_bhartilist.setLayoutManager(mManager);
        // progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        //    recyclerView_bhartilist.setLayoutManager(mManager);
        requestType=getIntent().getStringExtra("type").toString().trim();
        getBhartiList(requestType);
      //  progressDialog.show();
        progressBar = findViewById(R.id.progressBar);
        txt_precentageText = findViewById(R.id.txt_precentageText);
        //  Preferences.save(context,Preferences.USER_MOBILE,"9420329047");





     //   checkValid();


        String strr = Preferences.get(context, Preferences.FEESSTATUS).trim();
        if (strr.equals("1")) {
            Log.i("Enterd in Course", "" + strr);
            try {
                Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_feesalert);

                TextView textView = dialog.findViewById(R.id.txt_details);
                textView.setText(Preferences.get(context, Preferences.FEESDATA));
                Button btn_okay = dialog.findViewById(R.id.btn_okay);
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } catch (Exception e) {
                Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

      //  txt_days = (TextView) findViewById(R.id.txt_days);
        txt_subcousename = (TextView) findViewById(R.id.txt_subcoursename);
        ll = findViewById(R.id.ll);
      //  txt_dates = (TextView) findViewById(R.id.txt_dates);

if(requestType.equals("1"))
{
    txt_subcousename.setText("Comming Interest");
    categorycnt=1;
}else if(requestType.equals("0"))
{
    categorycnt=0;
    txt_subcousename.setText("Sent Interest");
}

    }

    private void checkValid() {
        try{

            if (CheckConnection.checkInternet(CourseList.this)) {
                //   getUserProfile();
            }
            else {
                //  Toast.makeText(Flash.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(CourseList.this);
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


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    private void getBhartiList(String type) {
        try {

            if (!progressDialog.isShowing())
                progressDialog.show();

            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String deviceid = "111";

            String userid = Preferences.get(context, Preferences.USER_ID);
            ;
            Call<List<UserModel>> call = RetrofitClient.getInstance().getMyApi().getMatchesRequest(mobile, userid, deviceid,type);
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                  //  Toast.makeText(CourseList.this, "Calling..", Toast.LENGTH_SHORT).show();
                    List<UserModel> videos = response.body();
                    try {
                        if(videos!=null)
                        //txt_days.setText(videos.size()+" Founds.");
                        adapter11 = new RequestProfileAdapter((ArrayList) videos, context,CourseList.this);
                        recyclerView_bhartilist.setAdapter(adapter11);
                        int resId = R.anim.layout_animation_fall_down;
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
                        recyclerView_bhartilist.setLayoutAnimation(animation);
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
        } catch (Exception e) {

        }
    }

    public void go(View view) {
        try {
            if (active_flag == 0) {
                Snackbar.make(ll, "Contact admin to activate account.", Snackbar.LENGTH_LONG).show();

            } else {
                finish();
                Intent intent = new Intent(CourseList.this, ContentList.class);
                startActivity(intent);
            }
        } catch (Exception e) {

        }
    }

    public void myprofile(View view) {
        try {
           // getUserProfile();
            finish();
            Intent intent=new Intent(CourseList.this,Home.class);
            startActivity(intent);

        } catch (Exception e) {

        }
    }

    public void feesdetails(View view) {
        try {
            //getUserProfile();
           /* Intent intent = new Intent(CourseList.this, Swarankur.class);
            startActivity(intent);*/

        } catch (Exception e) {

        }
    }

    public void files(View view) {
        try {
            finish();
            Intent intent = new Intent(CourseList.this, MyProfile.class);
            startActivity(intent);

        } catch (Exception e) {

        }
    }

    @Override
    public void showCourseName(String name) {
        txt_subcousename.setText(name);
    }

    @Override
    public void addRequestStatus(String otheruserid, int req_type) {
     //   Toast.makeText(context, ""+req_type, Toast.LENGTH_SHORT).show();
      if(categorycnt==1)
        showInterest(otheruserid,req_type);
    }



    private void showInterest(String Otheruser,int type) {
        //  Toast.makeText(context, "Calling", Toast.LENGTH_SHORT).show();
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();
            String userid= Preferences.get(context,Preferences.USER_ID).toString().trim();
            //   String interestid=Preferences.get(context,Preferences.USER_INTERESTED_ID).toString().trim();
            String interestid=Otheruser.trim();;

            Call<String> call = RetrofitClient.getInstance().getMyApi().addInterest_Accept(
                    userid,interestid,type);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String saravMenuModels = response.body();
                        try {
                            Toast.makeText(context, ""+saravMenuModels, Toast.LENGTH_SHORT).show();
                            getBhartiList(requestType);
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



}