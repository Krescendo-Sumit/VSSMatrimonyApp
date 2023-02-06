package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import vishwakarma.matrimony.seva.adapter.ContentAdapter;
import vishwakarma.matrimony.seva.model.ContentModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentList extends AppCompatActivity implements ContentAdapter.EventListener {
    ContentAdapter adapter1;
    RecyclerView recyclerView_bhartilist;
    LinearLayoutManager mManager;
    Context context;
    ProgressDialog progressDialog;

    String id = "";

    String subcourseid, username, coursename, courseid, days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        context = ContentList.this;
        recyclerView_bhartilist = (RecyclerView) findViewById(R.id.rc_listofexams);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);

        // progressDialog.setCanceledOnTouchOutside(false);
//      progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//      progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//      progressDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        recyclerView_bhartilist.setLayoutManager(mManager);
        id = Preferences.get(context, Preferences.SELECTEDSUBCOURSE);

        subcourseid = Preferences.get(context, Preferences.USERSUBCOURSE);
        username = Preferences.get(context, Preferences.USERNAME);
        coursename = Preferences.get(context, Preferences.USERCOURSENAME);
        courseid = Preferences.get(context, Preferences.USERCOURSE);
        days = Preferences.get(context, Preferences.USERDAYS);


        Log.i("subcourseid", subcourseid);

        Log.i("username", username);

        Log.i("coursename", coursename);

        Log.i("courseid", courseid);

        Log.i("days", days);


        getBhartiList(subcourseid);
    }

    private void getBhartiList(String id) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            String mobile = Preferences.get(context,Preferences.USER_MOBILE);

            Call<List<ContentModel>> call = RetrofitClient.getInstance().getMyApi().getContentList(mobile, subcourseid);
            call.enqueue(new Callback<List<ContentModel>>() {
                @Override
                public void onResponse(Call<List<ContentModel>> call, Response<List<ContentModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    List<ContentModel> videos = response.body();
                    try {
                     //   adapter1 = new ContentAdapter((ArrayList) videos, context,days,ContentList.this::showCourseName);
                        adapter1 = new ContentAdapter((ArrayList) videos, context);
                        recyclerView_bhartilist.setAdapter(adapter1);
                    } catch (NullPointerException e) {
                        Toast.makeText(context, " No Lesson found. " , Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<ContentModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void showCourseName(String name) {

    }
}