package vishwakarma.matrimony.seva.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vishwakarma.matrimony.seva.PaymentActivity;
import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.adapter.BhartiAdapter;
import vishwakarma.matrimony.seva.adapter.ContentAdapter;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    BhartiAdapter adapter1;

    LinearLayoutManager mManager;
    Context context;
    ProgressDialog progressDialog;

    TextView txt_days, txt_dates, txt_name, txt_subcousename;
    LinearLayout ll;
    int active_flag = 1;


    ContentAdapter adapter11;
    RecyclerView recyclerView_bhartilist;
    ProgressBar progressBar;
    TextView txt_precentageText;

    String id = "";
    String InstallId = "";

    String subcourseid, username, coursename, courseid, days;
    private long pressedTime;
    View baseView;
    EditText et_searchText;
    Button btn_go;
    String status="";

    public MatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchFragment newInstance(String param1, String param2) {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseView= inflater.inflate(R.layout.fragment_match, container, false);
        context = getContext();
        status = Preferences.get(context, Preferences.USERSTATUS);

        if(status.equals("1")) {

            //  recyclerView_bhartilist = (RecyclerView) findViewById(R.id.rc_listofexams);
            mManager = new LinearLayoutManager(context);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            recyclerView_bhartilist = (RecyclerView) baseView.findViewById(R.id.rc_listofexams);
            mManager = new LinearLayoutManager(context);
            recyclerView_bhartilist.setLayoutManager(mManager);
            // progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
            //    recyclerView_bhartilist.setLayoutManager(mManager);

            if (status.equals("1")) {
                getBhartiList();
            } else {
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                startActivity(intent);
            }
            //  progressDialog.show();
            progressBar = baseView.findViewById(R.id.progressBar);
            txt_precentageText = baseView.findViewById(R.id.txt_precentageText);
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

            txt_days = (TextView) baseView.findViewById(R.id.txt_days);
            txt_subcousename = (TextView) baseView.findViewById(R.id.txt_subcoursename);
            ll = baseView.findViewById(R.id.ll);
            txt_dates = (TextView) baseView.findViewById(R.id.txt_dates);
            txt_name = (TextView) baseView.findViewById(R.id.txt_name);
            et_searchText = (EditText) baseView.findViewById(R.id.et_searchtext);
            btn_go = (Button) baseView.findViewById(R.id.btn_go);
            txt_name.setText("Welcome - " + Preferences.get(context, Preferences.USER_PROFILE_NAME));

            btn_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = et_searchText.getText().toString();
                    if (str.isEmpty()) {
                        et_searchText.setError("Please Enter something.");
                    } else {
                        if (status.equals("1")) {
                            getBhartiList(str);
                        } else {
                            Intent intent = new Intent(getContext(), PaymentActivity.class);
                            startActivity(intent);
                        }

                    }
                }
            });
        }else{
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            startActivity(intent);
        }
        return  baseView;
    }

    private void getBhartiList() {
        try {

            if (!progressDialog.isShowing())
                progressDialog.show();

            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String deviceid = "111";

            String userid = Preferences.get(context, Preferences.USER_ID);
            ;
            Call<List<UserModel>> call = RetrofitClient.getInstance().getMyApi().getMatches(mobile, userid, deviceid);
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //  Toast.makeText(CourseList.this, "Calling..", Toast.LENGTH_SHORT).show();
                    List<UserModel> videos = response.body();
                    try {
                        if(videos!=null) {
                            txt_days.setText(videos.size() + " Founds.");
                            adapter11 = new ContentAdapter((ArrayList) videos, context);
                            recyclerView_bhartilist.setAdapter(adapter11);
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
                            recyclerView_bhartilist.setLayoutAnimation(animation);
                        }
                        else
                        {
                            Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG).show();
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
    private void getBhartiList(String str) {
        try {

            if (!progressDialog.isShowing())
                progressDialog.show();

            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String deviceid = "111";

            String userid = Preferences.get(context, Preferences.USER_ID);
            ;
            Call<List<UserModel>> call = RetrofitClient.getInstance().getMyApi().getMatchesByStr(mobile, userid, deviceid,str);
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //  Toast.makeText(CourseList.this, "Calling..", Toast.LENGTH_SHORT).show();
                    List<UserModel> videos = response.body();
                    try {
                        if(videos!=null)
                            txt_days.setText(videos.size()+" Founds.");
                        adapter11 = new ContentAdapter((ArrayList) videos, context);
                        recyclerView_bhartilist.setAdapter(adapter11);
                        int resId = R.anim.layout_animation_fall_down;
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
                        recyclerView_bhartilist.setLayoutAnimation(animation);
                    } catch (NullPointerException e) {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG).show();
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


}