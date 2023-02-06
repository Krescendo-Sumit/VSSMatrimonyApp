package vishwakarma.matrimony.seva.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vishwakarma.matrimony.seva.ContentList;
import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.model.SubCourseModel;
import vishwakarma.matrimony.seva.util.Preferences;

public class SubCourseAdapter extends  RecyclerView.Adapter<SubCourseAdapter.DataObjectHolder>{
    Context context;

    private static final int UNSELECTED = -1;

    ArrayList<SubCourseModel> bhartiModelArrayList=null;

    public interface EventListener {
        void onDelete(int trid, int position);
    }
    public SubCourseAdapter(ArrayList<SubCourseModel> productModels, Context context) {

        this.bhartiModelArrayList = productModels;
        Log.i("Seller produ:",">>"+productModels.size());
        this.context = context;

    }

    @NonNull
    @Override
    public SubCourseAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_listofexams, parent, false);

        return new SubCourseAdapter.DataObjectHolder(view);
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
    public void onBindViewHolder(final SubCourseAdapter.DataObjectHolder holder, final int position) {
        try {
            SubCourseModel bhartiModel=bhartiModelArrayList.get(position);
            holder.price.setText(bhartiModel.getTitle());

            Glide.with(context)
                    .load(bhartiModel.getImagepath())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(holder.imageView);

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Preferences.save(context,Preferences.SELECTEDSUBCOURSE,bhartiModel.getId());
                    Intent intent=new Intent(context, ContentList.class);
                    context.startActivity(intent);

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
        public DataObjectHolder(View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.txt_title);
            imageView=(ImageView)itemView.findViewById(R.id.img_bhartilogo);
            ll=(LinearLayout)itemView.findViewById(R.id.ll);

        }
    }

}
