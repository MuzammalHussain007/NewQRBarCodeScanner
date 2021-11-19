package com.be.solo.qr.code.scanner.SoloAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.be.solo.qr.code.scanner.Interface.CheckPosition;
import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloActivities.SoloTextDetailActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloURLsDetailActivity;
import com.be.solo.qr.code.scanner.SoloModels.SoloFavoriteModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloDeleteFavListener;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.bumptech.glide.Glide;

import java.util.List;

public class SoloFavouriteAdapter extends RecyclerView.Adapter<SoloFavouriteAdapter.FavViewHolder> {

    private Context context;
    private List<SoloFavoriteModel> favoriteEmailList;
    private SoloDeleteFavListener listener;

    public SoloFavouriteAdapter(Context context, List<SoloFavoriteModel> favoriteEmailList, SoloDeleteFavListener listener) {
        this.context = context;
        this.favoriteEmailList = favoriteEmailList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavViewHolder(LayoutInflater.from(context).inflate(R.layout.fav_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FavViewHolder holder, final int position) {

        holder.title.setText(favoriteEmailList.get(position).getContent());
        holder.time.setText(favoriteEmailList.get(position).getDate());
        Glide.with(context).load(favoriteEmailList.get(position).getIcon()).into(holder.icon);


        if (URLUtil.isValidUrl(favoriteEmailList.get(position).getContent())) {
            holder.icon.setBackgroundResource(favoriteEmailList.get(position).getIcon());
        } else {

            holder.icon.setBackgroundResource(favoriteEmailList.get(position).getIcon());
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startResultDetailActivity(favoriteEmailList.get(position).getType(), favoriteEmailList.get(position).getContent(),
                        favoriteEmailList.get(position).getDate());
            }
        });
        holder.icon.setBackgroundResource(favoriteEmailList.get(position).getColor());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFav(position);
                favoriteEmailList.remove(position);
                notifyDataSetChanged();
                if (favoriteEmailList.size() == 0) {
                    listener.deleted();
                }
            }
        });

    }

    private void removeFav(int position) {

        SoloSharedPreference sharedPref = new SoloSharedPreference();
        sharedPref.removeFavourite(context, position);
    }

    private void startResultDetailActivity(String type, String content, String date) {

        if (URLUtil.isValidUrl(content)) {
            Intent intent = new Intent(context, SoloURLsDetailActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("content", content);
            intent.putExtra("Url", "Url");
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, SoloTextDetailActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("content", content);
            intent.putExtra("Text", "Text");
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return favoriteEmailList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        TextView title, time;
        ConstraintLayout container;
        ImageView icon, delete , favourite;
        private CardView cardView;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            container = itemView.findViewById(R.id.container);
            icon = itemView.findViewById(R.id.icon);
            delete = itemView.findViewById(R.id.btn_remove);
            cardView = itemView.findViewById(R.id.card);

        }
    }
}
