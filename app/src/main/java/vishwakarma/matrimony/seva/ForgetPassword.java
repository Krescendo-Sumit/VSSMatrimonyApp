package vishwakarma.matrimony.seva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.RetrofitClient;
import vishwakarma.matrimony.seva.util.RetrofitClient_MailSending;

public class ForgetPassword extends AppCompatActivity {
    Context context;
    EditText editText;
    String txt_email;
    TextView txt_label;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        context = ForgetPassword.this;
        editText = findViewById(R.id.et_verifytext);
        txt_label=findViewById(R.id.txt_label);
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please Wait..");

    }

    public void verify(View w) {
        try {
            txt_email = editText.getText().toString().trim();
            if (txt_email.trim().equals("")) {
                editText.setError("Please Enter something...");
            } else {
                getUserProfile(txt_email);
                Toast.makeText(context, "Verify..", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {

        }
    }
    public void getUserProfile(String txt_email) {
        try {
            progressDialog.show();
            Call<List<UserModel>> call = RetrofitClient.getInstance().getMyApi().getUserDetails_ForgetPassword(txt_email);
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //  Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                   try {
                       List<UserModel> MyProfiles = response.body();
                       for (UserModel v : MyProfiles) {
                           if (v.getEmail() != null && !(v.getEmail().trim().equals(""))) {
                               SendEmail(v.getEmail(),v.getPassword());
                           }
                       }
                   }catch(Exception e)
                   {
                       txt_label.setText("User details not found..");
                   }
                }

                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
txt_label.setText("User details not found...");
                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (
                Exception e) {


        }

    }
    public void SendEmail(String txt_email,String password) {
        try {
            progressDialog.show();
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("FromEmail","Hello");
            jsonObject.addProperty("CCEmail","vishwakarmamatrimony.help@gmail.com");
            jsonObject.addProperty("Port",587);
            jsonObject.addProperty("Password","Hello");
            jsonObject.addProperty("ToEmail",txt_email);
            jsonObject.addProperty("Subject","Vishwakarma Matrimony Forget Password.");
            jsonObject.addProperty("Body","Hi User Your Pasword is : <h3>"+password+"</h3>");
            Call<String> call = RetrofitClient_MailSending.getInstance().getMyApi().sendMail(jsonObject);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    //  Toast.makeText(context, "Size is " + response.body().size(), Toast.LENGTH_SHORT).show();
                    String result = response.body();
                    if(result.trim().equals("true")) {
                        txt_label.setText("Password Sent to User Registered Email Address.");
                        Toast.makeText(ForgetPassword.this, "Password Sent to User Registered Email Address.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Error is", t.getMessage());
                }
            });
        } catch (
                Exception e) {


        }

    }
}