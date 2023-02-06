package vishwakarma.matrimony.seva.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.model.FeesModel;

public class FeesAdapter extends  RecyclerView.Adapter<FeesAdapter.DataObjectHolder>{


    Context context;

    private static final int UNSELECTED = -1;

    ArrayList<FeesModel> FeesModelArrayList=null;

    public interface EventListener {
        void onDelete(int trid, int position);
    }
    public FeesAdapter(ArrayList<FeesModel> productModels, Context context) {

        this.FeesModelArrayList = productModels;
        Log.i("Seller produ:",">>"+productModels.size());
        this.context = context;

    }

    @NonNull
    @Override
    public FeesAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_listoffees, parent, false);

        return new FeesAdapter.DataObjectHolder(view);
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
        return FeesModelArrayList.size();
        //} else {
        //  return 0;
        // }
    }




    @Override
    public void onBindViewHolder(final FeesAdapter.DataObjectHolder holder, final int position) {
        try {
            FeesModel FeesModel=FeesModelArrayList.get(position);
            holder.txt_title.setText("Installment "+FeesModel.getInstallmentno());
         //   holder.txt_amt.setText("Rs."+FeesModel.getAmt()+" By "+FeesModel.getPmode());
            holder.txt_amt.setText(Html.fromHtml("Amount<br> <b>Rs."+FeesModel.getAmt()+"</b>"));

            holder.txt_details.setText(Html.fromHtml("Date Paid<br><b> "+FeesModel.getCdate()+"</b><br><br>Mode of Payment <br><b> "+FeesModel.getPmode()+"</b>"));


            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

           /*        // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Preferences.save(context,Preferences.SELECTEDEXAMID,FeesModel.getId());
                    Intent intent=new Intent(context, SubCourseList.class);
                    context.startActivity(intent);*/

                }
            });

        }catch(Exception e)
        {
            Log.i("Error ",e.getMessage());
        }
    }





    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_amt, txt_details;

        LinearLayout ll;
        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_amt = (TextView) itemView.findViewById(R.id.txt_days);
            txt_details = (TextView) itemView.findViewById(R.id.txt_paiddate);

            ll=(LinearLayout)itemView.findViewById(R.id.ll);

        }
    }


}
