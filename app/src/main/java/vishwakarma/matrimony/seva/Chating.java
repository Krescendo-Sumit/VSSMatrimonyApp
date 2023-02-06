package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vishwakarma.matrimony.seva.adapter.ChatAdapter;
import vishwakarma.matrimony.seva.model.ChatModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;

public class Chating extends AppCompatActivity {
    String interestedid;
    Context context;
    ProgressDialog progressDialog;
    EditText et_message;
    ChatAdapter adapter1;
    RecyclerView rc_chat;
    LinearLayoutManager mManager;
    Handler handler;
    Runnable m_Runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        context = Chating.this;
        et_message = findViewById(R.id.et_message);
        progressDialog = new ProgressDialog(context);
        interestedid = getIntent().getStringExtra("oid").toString().trim();
        rc_chat = (RecyclerView) findViewById(R.id.rc_chat);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);
     //   mManager.setReverseLayout(true);
        rc_chat.setLayoutManager(mManager);
        getChat();
     /*   handler = new Handler();
        m_Runnable=new Runnable() {
            public void run() {
                Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show();
                getChat();
                //doTheAutoRefresh();
                handler.postDelayed(m_Runnable, 5000);
            }
        };
        handler.postDelayed(m_Runnable, 5000);*/
    }

    private void getChat() {
        try {
            getChating();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // handler.removeCallbacks(m_Runnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //handler.removeCallbacks(m_Runnable);
    }

    public void send(View view) {
        try {
            if(et_message.getText().toString().trim().equals(""))
            {
             et_message.setError("Enter message");
            }
            {
                sendChat();
            }
            //     Toast.makeText(this, "Hello "+interestedid, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }


    private void sendChat() {
        //  Toast.makeText(context, "Calling", Toast.LENGTH_SHORT).show();
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();
            String userid = Preferences.get(context, Preferences.USER_ID).toString().trim();
            //   String interestid=Preferences.get(context,Preferences.USER_INTERESTED_ID).toString().trim();
            String interestid = interestedid.trim();
            String message = et_message.getText().toString().trim();

            Call<String> call = RetrofitClient.getInstance().getMyApi().addChat(
                    userid, interestid, message);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.body() != null) {
                        String saravMenuModels = response.body();
                        try {
                           // Toast.makeText(context, "" + saravMenuModels, Toast.LENGTH_SHORT).show();
                            et_message.setText("");
                            getChating();

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


    private void getChating() {
        try {
         /*   if (!progressDialog.isShowing())
                progressDialog.show();*/

            String sender = Preferences.get(context, Preferences.USER_ID).toString().trim();
            //   String interestid=Preferences.get(context,Preferences.USER_INTERESTED_ID).toString().trim();
            String receiver = interestedid.trim();


            Call<List<ChatModel>> call = RetrofitClient.getInstance().getMyApi().getChat(sender, receiver);
            call.enqueue(new Callback<List<ChatModel>>() {
                @Override
                public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                    if (response != null) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        //   Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                        List<ChatModel> videos = response.body();
                        try {
                            adapter1 = new ChatAdapter((ArrayList) videos, context);
                            rc_chat.setAdapter(adapter1);
                            rc_chat.smoothScrollToPosition(videos.size());
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Record will be available soon.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Record will be available soon.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "No data available yet.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }
    }

    public void refresh(View view) {
        try{
            if (!progressDialog.isShowing())
                progressDialog.show();
            getChat();
        }catch(Exception e)
        {

        }
    }
}