
package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;

public class ProductImageViwer extends AppCompatActivity {
    WebView webView;
    LinearLayout ll;
    String data="";
    String baseUrl,url,serverpath;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_viwer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        context=ProductImageViwer.this;
        ll=findViewById(R.id.ll);
        String ff= Preferences.get(context, Preferences.OTHER_USER_Mobile).trim();
        String url1 =  ff + "_One.jpg";
        String url2 =  ff + "_Two.jpg";
        String url3 =  ff + "_Three.jpg";
        String url4 =  ff + "_Four.jpg";
        data=url1+","+url2+","+url3+","+url4;
        Log.i("URls",data);
        //  Toast.makeText(this, "Data is "+data, Toast.LENGTH_SHORT).show();
        baseUrl= Constants.BASE_URL;
        serverpath=baseUrl+"no-img.png";
        String kk[]=data.split(",");
        String value;
        webView = (WebView) findViewById(R.id.web);
        webView.setWebViewClient(new Browser_Home());
        webView.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        for(int i=0;i<kk.length;i++)
        {
            try {
                url = baseUrl + "dp/"+kk[i].trim();
                if(i==0)
                {
                    loadWebSite(url);
                }
                ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(R.drawable.bkblacktnew);
                imageView.setId(i);
                imageView.setPadding(5,5,5,5);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // imageView.setMaxWidth(20);
                imageView.setLayoutParams(layoutParams);

                String data = "<html><head><meta name=\"viewport\"\"content=\"width='100%', initial-scale=0.65 \" /></head>";
                data = data + "<body><center>" +
                        "<img style='border: 5px solid #555;' width=\"100%\" height='110px' src=\"" + url + "\" " +
                        "onerror=\"this.onerror=null; this.src='" + serverpath + "'\"/>" +
                        "" +
                        "</center></body></html>";
                Glide.with(this)
                        .load(url)
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(ProductImageViwer.this, kk[v.getId()]+" - "+v.getId(), Toast.LENGTH_SHORT).show();
                        url = baseUrl + "dp/"+kk[v.getId()].trim();
                        loadWebSite(url);
                    }
                });

                ll.addView(imageView);




            }catch(Exception e)
            {
                Toast.makeText(context, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }
    private void loadWebSite(String u) {

        //  webView.loadUrl(u);

        String data = "<html><head><meta name=\"viewport\"\"content=\"width='100%', initial-scale=0.65 \" /></head>";
        data = data + "<body><center>" +
                "<img width=\"100%\" height='auto' src=\"" + u + "\" " +
                "onerror=\"this.onerror=null; this.src='" + serverpath + "'\"/>" +
                "" +
                "</center></body></html>";
        // webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(data, "text/html", "UTF-8");


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
//            Toast.makeText(Video.this, "Error Occur", Toast.LENGTH_SHORT).show();
//            finish();
//            Intent intent = new Intent(Video.this, ErroActivity.class);
//            startActivity(intent);
            // view.loadData(data, "text/html", "UTF-8");
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
//  9860088490

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

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onProgressChanged(WebView view, int progress) {
            //Make the bar disappear after URL is loaded, and changes string to Loading...
            //  setTitle("Loading...");
            setProgress(progress * 100); //Make the bar disappear after URL is loaded
//            pb.setProgress(progress);
//            // Return the app name after finish loading
//            if (progress == 100) {
//
//                pb.setVisibility(View.GONE);
//            }
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
}