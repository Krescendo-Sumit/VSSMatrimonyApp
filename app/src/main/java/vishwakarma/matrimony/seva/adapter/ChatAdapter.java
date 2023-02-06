package vishwakarma.matrimony.seva.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vishwakarma.matrimony.seva.R;
import vishwakarma.matrimony.seva.model.ChatModel;
import vishwakarma.matrimony.seva.util.Preferences;

public class ChatAdapter extends  RecyclerView.Adapter<ChatAdapter.DataObjectHolder>{


    Context context;

    private static final int UNSELECTED = -1;

    ArrayList<ChatModel> bhartiModelArrayList=null;

    public interface EventListener {
        void onDelete(int trid, int position);
    }
    public ChatAdapter(ArrayList<ChatModel> productModels, Context context) {

        this.bhartiModelArrayList = productModels;
        Log.i("Seller produ:",">>"+productModels.size());
        this.context = context;

    }

    @NonNull
    @Override
    public ChatAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chat, parent, false);

        return new ChatAdapter.DataObjectHolder(view);
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
    public void onBindViewHolder(final ChatAdapter.DataObjectHolder holder, final int position) {
        try {
            ChatModel bhartiModel=bhartiModelArrayList.get(position);
            String sender=Preferences.get(context,Preferences.USER_ID).toString().trim();
            if(sender.equals(bhartiModel.getSender().trim()))
            {
                holder.txt_message.setText(bhartiModel.getMessage());
                holder.txt_date.setText(bhartiModel.getCdate());
                holder.txt_message.setGravity(Gravity.RIGHT);
                holder.txt_date.setGravity(Gravity.RIGHT);
                holder.linearLayout.setBackgroundResource(R.drawable.rounded_corner_chat_sender);
            }else
            {
                holder.txt_message.setText(bhartiModel.getMessage());
                holder.txt_date.setText(bhartiModel.getCdate());
                holder.txt_message.setGravity(Gravity.LEFT);
                holder.txt_date.setGravity(Gravity.LEFT);
                holder.linearLayout.setBackgroundResource(R.drawable.text_field);
            }
        }catch(Exception e)
        {
            Log.i("Error ",e.getMessage());
        }
    }





    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView txt_message, txt_date;
LinearLayout linearLayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_message =itemView.findViewById(R.id.txt_message);
            txt_date=itemView.findViewById(R.id.txt_date);
            linearLayout=itemView.findViewById(R.id.cc);


        }
    }


}
