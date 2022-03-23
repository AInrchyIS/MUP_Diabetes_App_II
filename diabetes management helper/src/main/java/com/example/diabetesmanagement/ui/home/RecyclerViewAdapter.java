package com.example.diabetesmanagement.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetesmanagement.AdapterLevelInterface;
import com.example.diabetesmanagement.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.LevelViewHolder> {

    ArrayList<RecyclerViewModel> dataholder;
    Context context;
    AdapterLevelInterface mAdapterLevelInterface;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public RecyclerViewAdapter(ArrayList<RecyclerViewModel> dataholder, Context context,AdapterLevelInterface adapterLevelInterface) {
        this.dataholder = dataholder;
        this.context = context;
        mAdapterLevelInterface=adapterLevelInterface;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelViewHolder holder, final int position) {

        final RecyclerViewModel temp = dataholder.get(position);

        holder.levelImage.setImageResource(dataholder.get(position).getImg());
        holder.club_text.setText(dataholder.get(position).getHeader());
        holder.level_text.setText(dataholder.get(position).getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(context, ClubNarrative.class);
//                intent.putExtra("imagename", temp.getImg());
//                intent.putExtra("clubname", temp.getHeader());
//                intent.putExtra("levelname", temp.getDesc());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);

                mAdapterLevelInterface.levelAdapterMethod(holder.level_text.getText().toString().trim());

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class LevelViewHolder extends RecyclerView.ViewHolder{

        ImageView levelImage;
        TextView club_text, level_text;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);

            levelImage = itemView.findViewById(R.id.level_imageview_id);
            club_text = itemView.findViewById(R.id.club_textView_id);
            level_text = itemView.findViewById(R.id.level_textView_id);

        }
    }
}
