package vishwakarma.matrimony.seva.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.model.SignInModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Context context;
    ProgressDialog progressDialog;
    TextView txt_name, txt_mobile, txt_batch, txt_email, txt_dhyaasid, txt_address;
    //  FeesAdapter adapter;
    //  RecyclerView rc_feesdetaills;
    //  LinearLayoutManager mManager;
    //  WebView dp;
    String str = "";
    String strtemp;
    int i = 0;
    TextView txt_slogan;
    TextView txt_marathi;
    TextView txt_english;
    int sloganlength;
    private long pressedTime;
    String vname;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
  View baseView;
  Locale myLocale;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        baseView= inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();
        progressDialog = new ProgressDialog(context);

        txt_name = baseView.findViewById(R.id.txt_name);
        txt_mobile = baseView.findViewById(R.id.txt_mobile);
        txt_batch = baseView.findViewById(R.id.txt_batch);
        txt_email = baseView.findViewById(R.id.txt_email);
        txt_address = baseView.findViewById(R.id.txt_address);
        txt_dhyaasid = baseView.findViewById(R.id.txt_dhyassid);
        txt_slogan = baseView.findViewById(R.id.txt_slogan);
        txt_marathi = baseView.findViewById(R.id.text_marathi);
        txt_english = baseView.findViewById(R.id.text_english);

        txt_marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setLocale("mr");
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                } catch (Exception e) {

                }
            }
        });

        txt_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setLocale("en");
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                } catch (Exception e) {

                }
            }
        });

        vname = "";

        txt_dhyaasid.setText(" VLSS-" + Preferences.get(context, Preferences.USER_ID));
        txt_name.setText(" " + Preferences.get(context, Preferences.USER_PROFILE_NAME));
       Log.i("values",Preferences.get(context, Preferences.USERCATEGORY).trim());
        if (Preferences.get(context, Preferences.USERCATEGORY).trim().equals("0"))
            txt_batch.setText(" Category : Bride");
        else if (Preferences.get(context, Preferences.USERCATEGORY).trim().equals("1"))
            txt_batch.setText(" Category : Groom");
        try {
            i = 0;
            strtemp = "";
            str = "विश्वकर्मीय लोहार सेवा संघ,मुंबई";
            sloganlength = str.length();
            new CountDownTimer(4500, 110) {
                public void onTick(long millisUntilFinished) {

                    if (i < sloganlength) {
                        strtemp += str.charAt(i);
                        Log.i("i >>", i + " " + str.charAt(i));
                        txt_slogan.setText(Html.fromHtml(" " + strtemp));
                    } else {
                        // txt_slogan.setText(Html.fromHtml(" " + strtemp));

                    }
                    i++;
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    //  strtemp = "";
                    // i = 0;
                    //  start();
                    //txt_name.setText(i+"done!");
                }

            }.start();


        }catch(Exception e)
        {

        }
        checkUserActivation();
        return baseView;
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
    }

    private void checkUserActivation() {
        try {
       /*     if (!progressDialog.isShowing())
                progressDialog.show();*/
            String mobile=Preferences.get(context,Preferences.USER_MOBILE);
            String id=Preferences.get(context,Preferences.USER_ID);
            Call<List<SignInModel>> call = RetrofitClient.getInstance().getMyApi().checkActivation(mobile,id);
            call.enqueue(new Callback<List<SignInModel>>() {
                @Override
                public void onResponse(Call<List<SignInModel>> call, Response<List<SignInModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.body() != null) {
                        List<SignInModel> videos = response.body();

                        for (SignInModel v : videos) {


                            Preferences.save(context, Preferences.USERSTATUS, v.getPayment_status());



                            //  Log.i("Values For Instale : ",v.getLocaltoken());
                            //  Preferences.save(context, Preferences.INSTALLID, v.getLocaltoken());
               /*         Preferences.save(context,Preferences.USERMOBILE,v.getMobile());
                        Preferences.save(context,Preferences.USERCOURSENAME,v.getCourseid());
                        Preferences.save(context,Preferences.USERDAYS,v.getDays());
                        Preferences.save(context,Preferences.USERSUBCOURSE,v.getSubcourse());*/

                        }

                    } else {
                        Toast.makeText(context, "Please Enter Valid Mobile and Password", Toast.LENGTH_SHORT).show();
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


}