package com.example.feedandfind.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedandfind.Application.FeedAndFind;
import com.example.feedandfind.DataManager.FirebaseData;
import com.example.feedandfind.Features.Finder.FinderDataDisplay;
import com.example.feedandfind.Features.Pets.PetsEdit;
import com.example.feedandfind.Features.Pets.PetsInfo;
import com.example.feedandfind.Items.PetInformation;
import com.example.feedandfind.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {

   Context context;
   private final List<PetInformation> recordsList;
   FirebaseData firebaseData;
   FeedAndFind feedAndFind;
   RecordCallback recordCallback;

    public interface RecordCallback {
        void onDeleteCallback();
    }

    public RecordAdapter(Context context, List<PetInformation> recordsList, RecordCallback recordCallback) {
        this.context = context;
        this.recordsList = recordsList;
        this.recordCallback = recordCallback;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.features_item_row, parent, false);
        firebaseData = new FirebaseData();
        feedAndFind = FeedAndFind.getInstance();
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordHolder holder, int position) {

        holder.petPic.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                Integer.parseInt(String.valueOf(recordsList.get(position).getImage())),
                context.getTheme()));
        holder.petName.setText(recordsList.get(position).getName());
        holder.petAge.setText(recordsList.get(position).getAge());

        holder.showOptions.setOnClickListener(view ->
                showPopupMenu(view, recordsList.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PetsInfo.class);
                intent.putExtra("COLLAR_ID", recordsList.get(position).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    class RecordHolder extends RecyclerView.ViewHolder {

        ImageView petPic, showOptions;
        TextView petName, petAge;
        View itemView;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);

            petPic = itemView.findViewById(R.id.petProfile);
            petName = itemView.findViewById(R.id.namePet);
            petAge = itemView.findViewById(R.id.petAge);
            showOptions = itemView.findViewById(R.id.show_options);
            this.itemView = itemView;
        }
    }

    private void showPopupMenu(View view, PetInformation petInformation) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.pet_items_menu); // Replace with your menu resource

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.delete_pet):
                        firebaseData.removeData("Users/"+feedAndFind.APP_CODE+"/PetFeederQrCodes/"+petInformation.getKey());
                        feedAndFind.removePetInformationList(petInformation.getKey());
                        recordCallback.onDeleteCallback();
                        break;
                    case (R.id.edit_pet):
                        intent = new Intent(context, PetsEdit.class);
                        intent.putExtra("COLLAR_ID", petInformation.getKey());
                        context.startActivity(intent);
                        break;
                    case (R.id.show_device):
                        intent = new Intent(context, FinderDataDisplay.class);
                        intent.putExtra("COLLAR_ID", petInformation.getKey());
                        context.startActivity(intent);
                        break;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
