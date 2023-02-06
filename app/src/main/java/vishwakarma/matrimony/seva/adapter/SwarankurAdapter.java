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

import vishwakarma.matrimony.seva.model.SwarankurModel;


public class SwarankurAdapter extends  RecyclerView.Adapter<SwarankurAdapter.DataObjectHolder>{


    Context context;

    private static final int UNSELECTED = -1;

    ArrayList<SwarankurModel> SwarankurModelArrayList=null;

    public interface EventListener {
        void onDelete(int trid, int position);
    }
    public SwarankurAdapter(ArrayList<SwarankurModel> productModels, Context context) {

        this.SwarankurModelArrayList = productModels;
        Log.i("Seller produ:",">>"+productModels.size());
        this.context = context;

    }

    @NonNull
    @Override
    public SwarankurAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_listoffees, parent, false);

        return new SwarankurAdapter.DataObjectHolder(view);
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
        return SwarankurModelArrayList.size();
        //} else {
        //  return 0;
        // }
    }




    @Override
    public void onBindViewHolder(final SwarankurAdapter.DataObjectHolder holder, final int position) {
        try {
            SwarankurModel SwarankurModel=SwarankurModelArrayList.get(position);
            holder.txt_title.setText("Topic : "+SwarankurModel.getTitle());
         // holder.txt_amt.setText("Rs."+SwarankurModel.getAmt()+" By "+SwarankurModel.getPmode());
String sses="";
            if(SwarankurModel.getSession().trim().equals("1"))
            {
               sses="JAN";
            }else if(SwarankurModel.getSession().trim().equals("2"))
            {
                sses="FEB";
            }else if(SwarankurModel.getSession().trim().equals("3"))
            {
                sses="MAR";
            }else if(SwarankurModel.getSession().trim().equals("4"))
            {
                sses="APR";
            }else if(SwarankurModel.getSession().trim().equals("5"))
            {
                sses="MAY";
            }else if(SwarankurModel.getSession().trim().equals("6"))
            {
                sses="JUN";
            }else if(SwarankurModel.getSession().trim().equals("7"))
            {
                sses="JUL";
            }else if(SwarankurModel.getSession().trim().equals("8"))
            {
                sses="AUG";
            }else if(SwarankurModel.getSession().trim().equals("9"))
            {
                sses="SEPT";
            }else if(SwarankurModel.getSession().trim().equals("10"))
            {
                sses="OCT";
            }else if(SwarankurModel.getSession().trim().equals("11"))
            {
                sses="NOV";
            }else if(SwarankurModel.getSession().trim().equals("12"))
            {
                sses="DEC";
            }



            holder.txt_amt.setText(Html.fromHtml("Session<br><b>"+sses+"</b>"));
String d="";
             if(SwarankurModel.getAstatus().trim().equals("1"))
             {
               //  holder.txt_details.setTextColor(Color.GREEN);
                 holder.txt_details.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_outline_24, 0);
                 d="Attendance Status: <br><b style='color:GREEN;'> Present </b>";
             }else if(SwarankurModel.getAstatus().trim().equals("2"))
             {
               //  holder.txt_details.setTextColor(Color.RED);
                 holder.txt_details.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_cancel_24, 0);
                 d="Attendance Status: <br><b style='color:RED;'> Absent </b>";
             }
             else
             {
                 if(SwarankurModel.getAstatus().trim().equals("0"))
                 {
                     d="Attendance Status: <br><b style='color:Black;'> NA </b>";
                 }
             }
            holder.txt_details.setText(Html.fromHtml("Details:<br><b> "+SwarankurModel.getDetails()+"</b><br><br>"+d));


            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

           /*        // Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    Preferences.save(context,Preferences.SELECTEDEXAMID,SwarankurModel.getId());
                    Intent intent=new Intent(context, SubCourseList.class);
                    context.startActivity(intent);

                    */

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
