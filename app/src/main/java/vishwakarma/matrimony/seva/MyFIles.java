package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vishwakarma.matrimony.seva.adapter.FileAdapter;
import vishwakarma.matrimony.seva.model.FileModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFIles extends AppCompatActivity {
    FileAdapter adapter;
    RecyclerView rc_feesdetaills;
    LinearLayoutManager mManager;
    Context context;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_f_iles);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = MyFIles.this;
        rc_feesdetaills = (RecyclerView) findViewById(R.id.rc_listoffees);
        mManager = new LinearLayoutManager(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        // progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER);
        rc_feesdetaills.setLayoutManager(mManager);

        getMenuList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getMenuList() {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();
            String mobile = Preferences.get(context,Preferences.USER_MOBILE);
            String userid = Preferences.get(context,Preferences.USER_ID);;
            Call<List<FileModel>> call = RetrofitClient.getInstance().getMyApi().getFileDetails(mobile,userid);
            call.enqueue(new Callback<List<FileModel>>() {
                @Override
                public void onResponse(Call<List<FileModel>> call, Response<List<FileModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //    Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                    if(response.body()!=null) {
                        List<FileModel> saravMenuModels = response.body();
                        try {
                            adapter = new FileAdapter((ArrayList) saravMenuModels, context);
                            rc_feesdetaills.setAdapter(adapter);
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(context, "माहिती उपलब्ध नाही.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<FileModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }

    }
}