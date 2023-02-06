package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vishwakarma.matrimony.seva.adapter.FeesAdapter;
import vishwakarma.matrimony.seva.model.FeesModel;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    TextView txt_name, txt_mobile, txt_batch, txt_email, txt_dhyaasid, txt_address;
    FeesAdapter adapter;
    RecyclerView rc_feesdetaills;
    LinearLayoutManager mManager;
    WebView dp;
    int onResumecnt=0;
    long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        context = MyProfile.this;
        progressDialog = new ProgressDialog(context);

        txt_name = findViewById(R.id.txt_name);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_batch = findViewById(R.id.txt_batch);
        txt_email = findViewById(R.id.txt_email);
        txt_address = findViewById(R.id.txt_address);
        txt_dhyaasid = findViewById(R.id.txt_dhyassid);

        dp =  findViewById(R.id.dp);
        dp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        dp.getSettings().setAppCacheEnabled(false);
        dp.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        rc_feesdetaills = (RecyclerView) findViewById(R.id.rc_listoffees);
        mManager = new LinearLayoutManager(context);
        rc_feesdetaills.setLayoutManager(mManager);
        checkValid();

    }

    private void checkValid() {
        try{

            if (CheckConnection.checkInternet(MyProfile.this))
                getUserProfile();
            else {
                //  Toast.makeText(Flash.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(MyProfile.this);
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
    protected void onResume() {
        super.onResume();
        if(onResumecnt==0)
        {
         onResumecnt=1;
        }else {
            getUserProfile();
        }
    }

    public void getUserProfile() {
        try {
            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String deviceid = "111";

            String userid = Preferences.get(context, Preferences.USER_ID);
            ;
            Call<List<UserModel>> call = RetrofitClient.getInstance().getMyApi().getUserDetails(mobile, userid, deviceid);
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //  Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                    List<UserModel> MyProfiles = response.body();
                    for (UserModel v : MyProfiles) {
                        Log.i("Dp Path", Constants.BASE_URL + "dp/" + v.getPhotopath());
                        String url=Constants.BASE_URL + "dp/" + v.getPhotopath();
                        String erurl=Constants.BASE_URL + "dp/noimg.jpg";

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
                            Toast.makeText(MyProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        txt_dhyaasid.setText(" VSS-" + v.getId());
                        txt_name.setText(" " + v.getFullname());
                        txt_email.setText(Html.fromHtml(" <b>Email Id </b><br>" + v.getEmail()));
                        txt_mobile.setText(Html.fromHtml(" <b>Mobile Number </b><br>" + v.getMobile1()+","+v.getMobile2()));
                        txt_address.setText(Html.fromHtml(" <b>Address </b><br>" + v.getPermanent_Address()));
                        if(v.getUsercategory().equals("1"))
                        txt_batch.setText(" Category :Groom");
                        else
                            txt_batch.setText(" Category :Bride");

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

    private void getMenuList() {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();
            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String userid = Preferences.get(context, Preferences.USER_ID);
            ;
            Call<List<FeesModel>> call = RetrofitClient.getInstance().getMyApi().getFeesDetails(mobile, userid);
            call.enqueue(new Callback<List<FeesModel>>() {
                @Override
                public void onResponse(Call<List<FeesModel>> call, Response<List<FeesModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //    Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                    if (response.body() != null) {
                        List<FeesModel> saravMenuModels = response.body();
                        try {
                            adapter = new FeesAdapter((ArrayList) saravMenuModels, context);
                            rc_feesdetaills.setAdapter(adapter);
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "No Installment Details Found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<FeesModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }

    }

    public void upload(View view) {
        Toast.makeText(context, "Uplaod Image", Toast.LENGTH_SHORT).show();
        
        try{

/*            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.popup_imageuplaod);
            
            WebView web=dialog.findViewById(R.id.web);
            web.setWebViewClient(new Browser_Home());
            web.setWebChromeClient(new ChromeClient());
            WebSettings webSettings = web.getSettings();

            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAppCacheEnabled(true);*/
            String ff= Preferences.get(context, Preferences.USER_MOBILE);
            String url= Constants.BASE_URL +"dp/uploadphoto.php?fname="+ff;
            Preferences.save(context,Preferences.PHOTOURL,url);

    Intent intent=new Intent(context,WebViewFileUploadTest.class);
    startActivity(intent);

  /*          loadWebSite(url ,web);
            dialog.show();
            */
        }catch (Exception e)
        {
            Toast.makeText(context, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
    }

    private void loadWebSite(String u ,WebView webView) {

        webView.loadUrl(u);

    }

    public void myprofile(View view) {
        try {
            finish();
            Intent intent=new Intent(MyProfile.this,Home.class);
            startActivity(intent);

        }catch(Exception e)
        {

        }
    }

    public void feesdetails(View view) {
        try {
            finish();
            Intent intent=new Intent(MyProfile.this,CourseList.class);
            startActivity(intent);

        }catch(Exception e)
        {

        }
    }

    public void files(View view) {
        try {
            getUserProfile();

        }catch(Exception e)
        {

        }
    }

    private class Browser_Home extends WebViewClient {
        Browser_Home() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
            Toast.makeText(MyProfile.this, "Error Occur", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(MyProfile.this, ErroActivity.class);
            startActivity(intent);
            // view.loadData(data, "text/html", "UTF-8");
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultMyProfilePoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onProgressChanged(WebView view, int progress) {
            //Make the bar disappear after URL is loaded, and changes string to Loading...
            //  setTitle("Loading...");
           // setProgress(progress * 100); //Make the bar disappear after URL is loaded

        }


        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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
}