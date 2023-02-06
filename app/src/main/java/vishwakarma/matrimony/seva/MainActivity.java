package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.hugomatilla.audioplayerview.AudioPlayerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;

//import com.hugomatilla.audioplayerview.AudioPlayerView;

public class MainActivity extends AppCompatActivity {
    String url = "";
    TextView txt_count;
    SeekBar seekBar;
    Handler seekHandler;
    // private AudioPlayerView audioPlayerView, audioPlayerViewText, audioPlayerViewCustomFont;
    MediaPlayer player;
    Dialog dialog;
    ImageView btn_play, btn_pause;
    ProgressDialog progressDialog;
    int pause_cnt = 0;
    int pause_position = 0;
    private View spinner;
    Context context;
    PDFView pdfView;
    RetrivePDFfromUrl UrlRender;
    String pdfurl = "";
    WebView web;


    private AudioPlayerView audioPlayerView, audioPlayerViewText, audioPlayerViewCustomFont;
    private Runnable updateSeekBar = new Runnable() {
        public void run() {
            if (player != null) {
                long totalDuration = player.getDuration();
                long currentDuration = player.getCurrentPosition();

                // Displaying Total Duration time
                txt_count.setText("" + milliSecondsToTimer(totalDuration - currentDuration));
                // Displaying time completed playing
                txt_count.setText("" + milliSecondsToTimer(currentDuration));
                //  txt_count.setText("" + currentDuration/1024 + "MB / " + totalDuration/1024 + " MB");
                // Updating progress bar
                seekBar.setProgress((int) currentDuration);

                // Call this thread again after 15 milliseconds => ~ 1000/60fps
                seekHandler.postDelayed(this, 15);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        context=MainActivity.this;
        spinner = findViewById(R.id.loading_spinner);
        seekBar = findViewById(R.id.seekBar2);
        txt_count = findViewById(R.id.textViewCount);
        String ff=Preferences.get(MainActivity.this, Preferences.SELECTEDFILE).replace(" ","%20");
        url = Constants.BASE_URL + "audio/index.php?fname=" + ff;
        Log.i("Audio URL",url);
      //   Toast.makeText(MainActivity.this, "File Path " + url, Toast.LENGTH_LONG).show();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait...");
        seekHandler = new Handler();
        spinner = findViewById(R.id.loading_spinner);
        audioPlayerView = (AudioPlayerView) findViewById(R.id.play);
        pdfView = findViewById(R.id.idPDFView);
        try {
            audioPlayerView.withUrl(url);
            audioPlayerView.toggleAudio();
            audioPlayerView.setOnAudioPlayerViewListener(new AudioPlayerView.OnAudioPlayerViewListener() {
                @Override
                public void onAudioPreparing() {
                  spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAudioReady() {
                    // Toast.makeText(getBaseContext(), "Audio is ready callback", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAudioFinished() {
                    //  Toast.makeText(getBaseContext(), "Audio finished callback", Toast.LENGTH_SHORT).show();
                    audioPlayerView.destroy();
                    audioPlayerView.withUrl(url);
                    try {
                        audioPlayerView.toggleAudio();
                    } catch (Exception e) {

                    }
                }
            });
        } catch(Exception e) {

        }

        String contentname= Preferences.get(context,Preferences.CONTENTNAME).toString().trim();
        TextView textView=findViewById(R.id.txt_title);
        textView.setText(contentname);

        String fname= Preferences.get(context,Preferences.SELECTEDFILEPDF).toString().trim();
        pdfurl= Constants.BASE_URL+"files/"+fname;
        Log.i("Urls is",pdfurl);
        if(fname.trim().equals("")) {
            Toast.makeText(context, "Notation not available.You have to write your own.", Toast.LENGTH_SHORT).show();
        }else
        {
            UrlRender = new RetrivePDFfromUrl();
            UrlRender.execute(pdfurl);
        }


        web=findViewById(R.id.web);
        url=url.replace(" ","%20");

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl(url);
        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
             //   Toast.makeText(context, "Getting Error Please", Toast.LENGTH_SHORT).show();
                //error.getErrorCode()
                if(error.getErrorCode()==-2)
                  web.loadData("<center>Something went wrong .Please Reload it again!!! .</center>", "text/html", "UTF-8");
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });

        web.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });



    }

    @Override
    protected void onDestroy() {
      /*  audioPlayerView.destroy();
        audioPlayerViewText.destroy();
        audioPlayerViewCustomFont.destroy();*/
        super.onDestroy();
        web.destroy();
        audioPlayerView.destroy();
        try{
            UrlRender.cancel(true);
        }catch (Exception e)
        {

        }
    }

    private boolean isHeadphonesPlugged() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return audioManager.isWiredHeadsetOn();
    }

    public void play(View v) {
        try {
            if (player.isPlaying())
                player.pause();
            else {
                // player.reset();
                player.prepare();
                player.start();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void restart(View view) {
        try {
            player.start();
            seekBar.setProgress(0);
            Toast.makeText(MainActivity.this, " Test " + player.getDuration(), Toast.LENGTH_SHORT).show();
            seekBar.setMax(player.getDuration());
            seekHandler.postDelayed(updateSeekBar, 15);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public void looping(View v) {
    /*    if (player.isLooping()) {
            player.setLooping(false);
            v.setBackgroundColor(Color.GRAY);
            Button btn = (Button) v;
            btn.setText("Disabled");
        } else {
            player.setLooping(true);
            v.setBackgroundColor(Color.GREEN);
            Button btn = (Button) v;
            btn.setText("Enabled");
        }*/


     /*   try {
            if (pause_cnt == 0) {
                pause_cnt = 1;
                player.pause();
                pause_position = player.getCurrentPosition();
            } else if (pause_cnt == 1) {
                pause_cnt = 0;
                player.seekTo(pause_position);

            }

        } catch (Exception e) {


        }*/


    }

    public void myprofile(View view)
    {
        try{

            Intent intent=new Intent(context,MyProfile.class);
            startActivity(intent);

        }catch (Exception e)
        {

        }
    }

    public void feesdetails(View view)
    {
        try{

            Intent intent=new Intent(context,FeesDetails.class);
            startActivity(intent);

        }catch (Exception e)
        {

        }
    }

    public void files(View view)
    {
        try{
            Intent intent=new Intent(context,MyFIles.class);
            startActivity(intent);
        }catch (Exception e)
        {

        }
    }

    public void goback(View view) {
        finish();
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
            progressDialog.dismiss();
        }
    }


}