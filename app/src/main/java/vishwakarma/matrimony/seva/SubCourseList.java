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



import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vishwakarma.matrimony.seva.adapter.SubCourseAdapter;
import vishwakarma.matrimony.seva.model.SubCourseModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCourseList extends AppCompatActivity {
    SubCourseAdapter adapter1;
    RecyclerView recyclerView_bhartilist;
    LinearLayoutManager mManager;
    Context context;
    ProgressDialog progressDialog;

    String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_course_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        context = SubCourseList.this;
        recyclerView_bhartilist = (RecyclerView) findViewById(R.id.rc_listofexams);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);

        // progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        recyclerView_bhartilist.setLayoutManager(mManager);
        id= Preferences.get(context,Preferences.SELECTEDEXAMID);
        getBhartiList(id);




    }

    private void getBhartiList(String id) {
        try {
            if (!progressDialog.isShowing())
                progressDialog.show();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", "9420329047");
             String mobile="";


            Call<List<SubCourseModel>> call = RetrofitClient.getInstance().getMyApi().getSubCourseList(mobile,id);
            call.enqueue(new Callback<List<SubCourseModel>>() {
                @Override
                public void onResponse(Call<List<SubCourseModel>> call, Response<List<SubCourseModel>> response) {
                  if(response !=null) {
                      if (progressDialog.isShowing())
                          progressDialog.dismiss();
                   //   Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                      List<SubCourseModel> videos = response.body();
                      try {
                          adapter1 = new SubCourseAdapter((ArrayList) videos, context);
                          recyclerView_bhartilist.setAdapter(adapter1);
                      } catch (NullPointerException e) {
                          Toast.makeText(context, "Record will be available soon.", Toast.LENGTH_SHORT).show();
                      } catch (Exception e) {
                          Toast.makeText(context, "Record will be available soon.", Toast.LENGTH_LONG).show();
                      }
                  }else
                  {
                      Toast.makeText(context,"No data available yet.",Toast.LENGTH_LONG).show();
                  }
                }

                @Override
                public void onFailure(Call<List<SubCourseModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }
    }
}