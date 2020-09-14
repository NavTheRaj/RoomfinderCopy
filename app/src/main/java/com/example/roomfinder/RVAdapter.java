package com.example.roomfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> implements Filterable {

    private ArrayList<Room> roomArrayList = new ArrayList<>();
    private ArrayList<Room> roomArrayListFull; // this is the clone object of original arraylist of room data so that any changes doen in this list wont affect the real ome.
    private Context context; //Context is created here so that this RVAdapter class recognizes from where the actions are being dealt with


    //Constructor class
    public RVAdapter(ArrayList<Room> roomArrayList, Context context) {
        this.roomArrayList = roomArrayList;
        this.context = context;
        this.roomArrayListFull = new ArrayList<>(roomArrayList);
    }


    //Inflates single_row.xml with the data that are later on binded with onBindViewHolder function
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    //Sets the image and text in iterated single_row.xml file for each rows in our database
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        int id;

        final Room room = roomArrayList.get(position);

        id = room.get_id();


        //sets image
        Glide.with(context)
                .asBitmap()
                .load(room.getImage())
                .into(holder.listRoomImage);


        //sets text in the image
        holder.listRoomText.setText(room.toString());

        //layout set and clickable
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"Clicked id is "+room.get_id(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,RoomDetailActivity.class);
                intent.putExtra(RoomDetailActivity.EXTRA_ROOMID,roomArrayList.get(position).get_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }




    //this class is responsible for getting the layour paramters from single_row.xml file
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView listRoomImage;
        TextView listRoomText;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listRoomImage = itemView.findViewById(R.id.sr_imageViewRV);
            listRoomText = itemView.findViewById(R.id.sr_textRV);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }

    //Filter Action Starts here

    @Override
    public Filter getFilter() {
        return roomFilter;
    }

    private Filter roomFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Room> filteredList = new ArrayList<>();

            //if in case the search query is empty dont change anything in the layout
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(roomArrayListFull); //thats why we are adding the original copy of lists in the filteredlist
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                //for each room rows ietrates the all rows and compares string pattern with location
                for(Room item:roomArrayListFull){
                    if(item.getLocation().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results; //Results that contain the search query if not empty


        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            roomArrayList.clear(); //clearing the previously showed list so that new filterelist will be shown..
            roomArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged(); //notifies the activity that our list has been changed!!

        }
    };






}