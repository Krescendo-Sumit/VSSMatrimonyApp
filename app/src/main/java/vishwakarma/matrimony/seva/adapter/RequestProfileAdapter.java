package vishwakarma.matrimony.seva.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vishwakarma.matrimony.seva.OtherUserProfile;
import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.model.UserModel;
import vishwakarma.matrimony.seva.util.Constants;
import vishwakarma.matrimony.seva.util.Preferences;

public class RequestProfileAdapter extends  RecyclerView.Adapter<RequestProfileAdapter.DataObjectHolder> {
    Context context;
    int days;
    private static final int UNSELECTED = -1;
    EventListener eventListener;
    ArrayList<UserModel> bhartiModelArrayList=null;


    public interface EventListener {
        void showCourseName(String name);
        void addRequestStatus(String otheruserid,int req_type);
    }
    public RequestProfileAdapter(ArrayList<UserModel> productModels, Context context,EventListener eventListener ) {

        this.bhartiModelArrayList = productModels;
        Log.i("Seller produ:",">>"+productModels.size());
        this.context = context;
        this.eventListener=eventListener;


    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_rquestedprofile, parent, false);

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
        return bhartiModelArrayList.size();
        //} else {
        //  return 0;
        // }
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        try {
            UserModel bhartiModel=bhartiModelArrayList.get(position);
            holder.price.setText(bhartiModel.getFullname());
            holder.txt_chapternamme.setText(Html.fromHtml("<b> User Code: <b>VLSS"+bhartiModel.getId()));
            holder.txt_days.setText(Html.fromHtml("<b>DOB : </b>"+bhartiModel.getDob()+"<b> Age:</b>"+bhartiModel.getAge()));

         //   listener.showCourseName(bhartiModel.getId());
Log.i("Url",Constants.BASE_URL+"dp/"+bhartiModel.getPhotopath());
            Glide.with(context)
                    .load(Constants.BASE_URL+"dp/"+bhartiModel.getPhotopath())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(holder.imageView);

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Preferences.save(context,Preferences.OTHER_USER_ID,bhartiModel.getId().toString().trim());
                    Preferences.save(context,Preferences.OTHER_USER_Mobile,bhartiModel.getMobile1().toString().trim());
                    Intent intent = new Intent(context, OtherUserProfile.class);
                    context.startActivity(intent);
                  //  Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
                }
            });


            holder.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Preferences.save(context,Preferences.OTHER_USER_ID,bhartiModel.getId().toString().trim());
                    Preferences.save(context,Preferences.OTHER_USER_Mobile,bhartiModel.getMobile1().toString().trim());
                    eventListener.addRequestStatus(bhartiModel.getId(),0);
                }
            });
            holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Preferences.save(context,Preferences.OTHER_USER_ID,bhartiModel.getId().toString().trim());
                    Preferences.save(context,Preferences.OTHER_USER_Mobile,bhartiModel.getMobile1().toString().trim());
                    eventListener.addRequestStatus(bhartiModel.getId(),1);
                }
            });

            if(bhartiModel.getRequestType().toString().trim().equals("1"))
                holder.btn_accept.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
            else if(bhartiModel.getRequestType().toString().trim().equals("0"))
                holder.btn_reject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_cancel_24, 0);


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
        Button btn_accept,btn_reject;
        public DataObjectHolder(View itemView)
        {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.txt_title);
            imageView=(ImageView)itemView.findViewById(R.id.img_bhartilogo);
            ll=(LinearLayout)itemView.findViewById(R.id.ll);
            txt_chapternamme=(TextView)itemView.findViewById(R.id.chaptername);
            txt_days=(TextView)itemView.findViewById(R.id.txt_days);
            btn_accept=itemView.findViewById(R.id.btn_accept);
            btn_reject=itemView.findViewById(R.id.btn_reject);

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
