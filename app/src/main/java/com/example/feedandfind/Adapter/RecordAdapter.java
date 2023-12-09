package com.example.feedandfind.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.Model.RecordModel;
import com.example.feedandfind.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {

   Context context;
   private final List<PetInformation> recordsList;

    public RecordAdapter(Context context, List<PetInformation> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.features_item_row, parent, false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordHolder holder, int position) {

        PetInformation model = recordsList.get(position);

        //Glide.with(context).load(recordsList.get(position).getImage()).into(holder.petPic);
        holder.petName.setText(recordsList.get(position).getName());
        holder.petAge.setText(recordsList.get(position).getAge());

        holder.petPic.setImageResource(R.drawable.pet_profile_pic);
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    class RecordHolder extends RecyclerView.ViewHolder {

        ImageView petPic;
        TextView petName, petAge;
        String id;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);

            petPic = itemView.findViewById(R.id.petProfile);
            petName = itemView.findViewById(R.id.namePet);
            petAge = itemView.findViewById(R.id.petAge);

        }
    }



}
