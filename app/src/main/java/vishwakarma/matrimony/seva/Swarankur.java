package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vishwakarma.matrimony.seva.adapter.SwarankurAdapter;
import vishwakarma.matrimony.seva.model.SwarankurModel;
import vishwakarma.matrimony.seva.util.Preferences;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Swarankur extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;

    SwarankurAdapter adapter;
    RecyclerView rc_feesdetaills;
    LinearLayoutManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swarankur);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_200));
        }
        context = Swarankur.this;
        progressDialog = new ProgressDialog(context);

        rc_feesdetaills = (RecyclerView) findViewById(R.id.rc_listoffees);
        mManager = new LinearLayoutManager(context);
        rc_feesdetaills.setLayoutManager(mManager);
        getMenuList();

    }
    private void getMenuList() {

        try {
            if (!progressDialog.isShowing())
                progressDialog.show();
            String mobile = Preferences.get(context, Preferences.USER_MOBILE);
            String userid = Preferences.get(context, Preferences.USER_ID);

            Call<List<SwarankurModel>> call = RetrofitClient.getInstance().getMyApi().getSwarankur(mobile, userid);
            call.enqueue(new Callback<List<SwarankurModel>>() {
                @Override
                public void onResponse(Call<List<SwarankurModel>> call, Response<List<SwarankurModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //    Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                    if (response.body() != null) {
                        List<SwarankurModel> saravMenuModels = response.body();
                        try {
                            adapter = new SwarankurAdapter((ArrayList) saravMenuModels, context);
                            rc_feesdetaills.setAdapter(adapter);
                        } catch (NullPointerException e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Not Found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<SwarankurModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (Exception e) {

        }

    }

}