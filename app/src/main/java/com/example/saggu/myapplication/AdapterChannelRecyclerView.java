package com.example.saggu.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterChannelRecyclerView extends RecyclerView.Adapter<AdapterChannelRecyclerView.MyViewHolder> {
    private List<ChannelObject> channelList;

    private Context context;
    private String TAG = "AdapterChannelRecyclerView";
    private ArrayList<String> added_ch;
    private ArrayList<Integer> new_ch_id = new ArrayList<>();
    private ArrayList<String> new_ch_name= new ArrayList<>();
    private float diffirence =0;

    MyInterface myInterface; // to communicate with parent activity from adapter class






    public AdapterChannelRecyclerView(Context context, List<ChannelObject> channelList, ArrayList<String> added_ch,MyInterface myInterface) {
        this.channelList = channelList;
        this.context = context;
        this.added_ch = added_ch;
        this.myInterface = myInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ChannelObject chnames = channelList.get(position);
        holder.name.setText(chnames.getName());
        holder.price.setText(Float.toString(chnames.getPrice()));
        holder.type.setText(chnames.getType());
        //   holder.chkbox_ch_selecton.setChecked(chnames.is_selected());
        //    Log.d(TAG, "onBindViewHolder: "+position);

        if (added_ch.contains(holder.name.getText())){
            Log.d(TAG, "onBindViewHolder: match found");
            holder.chkbox_ch_selecton.setVisibility(View.INVISIBLE);
        }else {holder.chkbox_ch_selecton.setVisibility(View.VISIBLE);}

        holder.chkbox_ch_selecton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(holder.chkbox_ch_selecton.isChecked()==true){
          //          Toast.makeText(context, "added Id "+ chnames.getId(), Toast.LENGTH_SHORT).show();

                    new_ch_id.add(chnames.getId());
                    new_ch_name.add(chnames.getName()+" "+chnames.getPrice());

                    diffirence+= chnames.getPrice();
                    myInterface.respond(new_ch_id,new_ch_name,diffirence);



                }else {
                    new_ch_id.remove(Integer.valueOf(chnames.getId()));
                    new_ch_name.remove(chnames.getName()+" "+chnames.getPrice());

                    diffirence-= chnames.getPrice();
                    myInterface.respond(new_ch_id,new_ch_name,diffirence);
                }


                try {
                    for (int i = 0; i< new_ch_id.size(); i++){
                    //    Toast.makeText(context, " new ch "+new_ch.get(i), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //       Toast.makeText(context, "clicked "+holder.name.getText() +" Id is "+  chnames.getId(), Toast.LENGTH_SHORT).show();
            }
        });


        if(new_ch_id.contains(chnames.getId())){
            holder.chkbox_ch_selecton.setChecked(true);
        }else {holder.chkbox_ch_selecton.setChecked(false);}




    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, type;
        ConstraintLayout list_layout;
        CheckBox chkbox_ch_selecton;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.channelname);
            price = itemView.findViewById(R.id.price);
            type = itemView.findViewById(R.id.pkgtype);
            chkbox_ch_selecton = itemView.findViewById(R.id.chk_box_selection);
            list_layout = itemView.findViewById(R.id.channellistlayout);





        }
    }
}
