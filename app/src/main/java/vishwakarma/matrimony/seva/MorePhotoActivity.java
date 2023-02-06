package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;

public class MorePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    WebView web1, web2, web3, web4;
    TextView txt_1;
    TextView txt_2;
    TextView txt_3;
    TextView txt_4;
    String url1="";
    String url2="";
    String url3="";
    String url4="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        context = MorePhotoActivity.this;
        web1 = findViewById(R.id.web1);
        web2 = findViewById(R.id.web2);
        web3 = findViewById(R.id.web3);
        web4 = findViewById(R.id.web4);
        txt_1=findViewById(R.id.txt_1);
        txt_2=findViewById(R.id.txt_2);
        txt_3=findViewById(R.id.txt_3);
        txt_4=findViewById(R.id.txt_4);

        txt_1.setOnClickListener(this);
        txt_2.setOnClickListener(this);
        txt_3.setOnClickListener(this);
        txt_4.setOnClickListener(this);
        showImages();

    }
    public void showImages(){
        String ff = Preferences.get(context, Preferences.USER_MOBILE);
         url1 = Constants.BASE_URL + "dp/" + ff + "_One.jpg";
         url2 = Constants.BASE_URL + "dp/" + ff + "_Two.jpg";
         url3 = Constants.BASE_URL + "dp/" + ff + "_Three.jpg";
         url4 = Constants.BASE_URL + "dp/" + ff + "_Four.jpg";
        loadMyUrl(web1,url1);
        loadMyUrl(web2,url2);
        loadMyUrl(web3,url3);
        loadMyUrl(web4,url4);
    }
    public void loadMyUrl(WebView webView,String url)
    {
        try {

            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setAppCacheEnabled(false);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setSupportZoom(true);
            String erurl=Constants.BASE_URL + "no-img.png";
            String data = "<html><head><meta name=\"viewport\"\"content=\"width='100%', initial-scale=0.65 \" /></head>";
            data = data + "<body style=\"background-color:black;\"><center>" +
                    "<img width=\"auto\" height='100%' src=\"" + url + "\" " +
                    "onerror=\"this.onerror=null; this.src='" + erurl + "'\"/>" +
                    "" +
                    "</center></body></html>";
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(data, "text/html", "UTF-8");
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void one(View view) {
        try {
            String imageTitle = "";
            switch (view.getId()) {
                case R.id.photo_one:
                    imageTitle = "_One";
                    break;
                case R.id.photo_two:
                    imageTitle = "_Two";
                    break;
                case R.id.photo_three:
                    imageTitle = "_Three";
                    break;
                case R.id.photo_four:
                    imageTitle = "_Four";
                    break;
            }
            String ff = Preferences.get(context, Preferences.USER_MOBILE);
            String url = Constants.BASE_URL + "dp/uploadphoto.php?fname=" + ff + imageTitle;
            Preferences.save(context, Preferences.PHOTOURL, url);
            Intent intent = new Intent(context, WebViewFileUploadTest.class);
            startActivity(intent);

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {

        try{

            Dialog dialog=new Dialog(context,R.style.AppBaseTheme);
            dialog.setContentView(R.layout.popup_img_show);
            WebView web_img=dialog.findViewById(R.id.web_img);

            switch (v.getId())
            {
                case R.id.txt_1:
                    loadMyUrl(web_img,url1);
                    break;
                case R.id.txt_2:
                    loadMyUrl(web_img,url2);
                    break;
                case R.id.txt_3:
                    loadMyUrl(web_img,url3);
                    break;
                case R.id.txt_4:
                    loadMyUrl(web_img,url4);
                    break;
            }


            dialog.show();


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
            Toast.makeText(context, "Error Occur", Toast.LENGTH_SHORT).show();

            view.loadData("No Image", "text/html", "UTF-8");
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
    protected void onResume() {
        super.onResume();
        showImages();
    }
}