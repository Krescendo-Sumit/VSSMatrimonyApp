package vishwakarma.matrimony.seva.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vishwakarma.matrimony.seva.OtherUserProfile;
import vishwakarma.matrimony.seva.PaymentActivity;
import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;

public class ContentAdapter extends  RecyclerView.Adapter<ContentAdapter.DataObjectHolder> {
    Context context;
    int days;
    private static final int UNSELECTED = -1;

    ArrayList<UserModel> userModelArrayList=null;
    EventListener listener;
    public interface EventListener {
        void showCourseName(String name);
    }
    public ContentAdapter(ArrayList<UserModel> productModels, Context context ) {

        this.userModelArrayList = productModels;
        Log.i("Seller produ:",">>"+productModels.size());
        this.context = context;


    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_listofexams, parent, false);

        return new DataObjectHolder(view);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //  if (mSellerProductlist.size() > 0) {
        return userModelArrayList.size();
        //} else {
        //  return 0;
        // }
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        try {
            UserModel userModel=userModelArrayList.get(position);
            holder.price.setText(userModel.getFullname());
            holder.txt_chapternamme.setText(Html.fromHtml("<b> User Code: <b>VLSS"+userModel.getId()));
            holder.txt_days.setText(Html.fromHtml("<b>DOB : </b>"+userModel.getDob()+"<b> Age:</b>"+userModel.getAge()));

         //   listener.showCourseName(userModel.getId());
Log.i("Url",Constants.BASE_URL+"dp/"+userModel.getPhotopath());
            Glide.with(context)
                    .load(Constants.BASE_URL+"dp/"+userModel.getPhotopath())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(holder.imageView);

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*

                    Preferences.save(context,Preferences.SELECTEDFILE,userModel.getId());
                    Preferences.save(context,Preferences.CONTENTNAME,userModel.getFullname());

                    if(userModel.getUsercategory().trim().equals("1")) {
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }else if(userModel.getType().equals("video"))
                    {
                        Intent intent = new Intent(context, Video.class);
                        context.startActivity(intent);
                    }

                    */
                    if (userModel.getPayment_status() != null && userModel.getPayment_status().trim().equals("0"))
                    {
                        Intent intent=new Intent(context, PaymentActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Please Pay Fees to Proceed.", Toast.LENGTH_SHORT).show();
                    }else {
                        Preferences.save(context, Preferences.OTHER_USER_ID, userModel.getId().toString().trim());
                        Preferences.save(context, Preferences.OTHER_USER_Mobile, userModel.getMobile1().toString().trim());
                        Intent intent = new Intent(context, OtherUserProfile.class);
                        context.startActivity(intent);
                    }
                  //  Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
                }
            });

        }catch(Exception e)
        {
            Log.i("Error ",e.getMessage());
        }
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView totalQuantity, price, txtQtyUnit;
        ImageView imageView;
        LinearLayout ll;
        TextView txt_chapternamme;
        TextView txt_days;
        public DataObjectHolder(View itemView)
        {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.txt_title);
            imageView=(ImageView)itemView.findViewById(R.id.img_bhartilogo);
            ll=(LinearLayout)itemView.findViewById(R.id.ll);
            txt_chapternamme=(TextView)itemView.findViewById(R.id.chaptername);
            txt_days=(TextView)itemView.findViewById(R.id.txt_days);

        }
    }

    public void openDialog()
    {
        try{


        }catch(Exception e)
        {

        }
    }

}
