package com.be.solo.qr.code.scanner.SoloAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloActivities.SoloContactActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloEmailActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloEventActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloLocationActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloMessageActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloTelActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloTextActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloURLActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloWifiActivity;
import com.be.solo.qr.code.scanner.SoloModels.SoloCreatingQRModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SoloCreateQRAdapter extends RecyclerView.Adapter<SoloCreateQRAdapter.CreateViewHolder> {

    private List<SoloCreatingQRModel> creatingQRLists;
    private Context context;

    public SoloCreateQRAdapter(List<SoloCreatingQRModel> creatingQRLists, Context context) {
        this.creatingQRLists = creatingQRLists;
        this.context = context;
    }

    public void filterList(List<SoloCreatingQRModel> filteredList) {
        creatingQRLists = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreateViewHolder(LayoutInflater.from(context).inflate(R.layout.create_qr_item_layout,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull final CreateViewHolder holder, int position) {

        final SoloCreatingQRModel lists = creatingQRLists.get(position);

        holder.item_name.setText(lists.getItem_name());
        Glide.with(context).load(lists.getIcon()).into(holder.item_image);
        holder.item_image.setBackgroundResource(lists.getColor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemActivity(holder.item_name.getText().toString());

            }
        });

    }
     private void startItemActivity(String item_name) {

        if (item_name.equals("Email")){
            context.startActivity(new Intent(context, SoloEmailActivity.class));
        }
        if (item_name.equals("Message")){
            context.startActivity(new Intent(context, SoloMessageActivity.class));
        }
        if (item_name.equals("Location")){
            context.startActivity(new Intent(context, SoloLocationActivity.class));
        }
        if (item_name.equals("Event")){
            context.startActivity(new Intent(context, SoloEventActivity.class));
        }
        if (item_name.equals("Contact")){
            context.startActivity(new Intent(context, SoloContactActivity.class));
        }
        if (item_name.equals("Phone")){
            context.startActivity(new Intent(context, SoloTelActivity.class));
        }
        if (item_name.equals("Text")){
            context.startActivity(new Intent(context, SoloTextActivity.class));
        }
        if (item_name.equals("WiFi")){
            context.startActivity(new Intent(context, SoloWifiActivity.class));
        }
        if (item_name.equals("Url")){
            context.startActivity(new Intent(context, SoloURLActivity.class));
        }

    }

    @Override
    public int getItemCount() {
        return creatingQRLists.size();
    }

    public class CreateViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item_container;
        CircleImageView item_image;
        TextView item_name;

        public CreateViewHolder(@NonNull View itemView) {
            super(itemView);

//        item_container = itemView.findViewById(R.id.item_container);
        item_image = itemView.findViewById(R.id.image);
        item_name = itemView.findViewById(R.id.item_name);

        }
    }
}
