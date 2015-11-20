package kz.growit.altynorda.adapters;

/**
 * Created by Талгат on 14.10.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.rey.material.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

import kz.growit.altynorda.AppController;
import kz.growit.altynorda.InsideListingActivity;
import kz.growit.altynorda.R;
import kz.growit.altynorda.ReserveActivity;
import kz.growit.altynorda.ResultsActivity;
import kz.growit.altynorda.models.Listings;

public class ListingRVAdapter extends RecyclerView.Adapter<ListingRVAdapter.ListingRVViewHolder> {
    private ArrayList<Listings> listings;
    private Activity activity;
    private Boolean isFavorite = false;
    private Button openReserve;
    private ArrayList<Integer> favoritesId = new ArrayList<>();

    private String userFullName = "Hello";

    public ListingRVAdapter(ArrayList<Listings> listings, Activity activity) {
        this.activity = activity;
        this.listings = listings;
    }

    @Override
    public ListingRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.listing_full_layout, parent, false);


        return new ListingRVViewHolder(itemView);
    }

    public void searchListingRequest(String holderId) {


        String url = "http://altynorda.kz/UserProfilesApi/GetUserProfile?userId=" + holderId;

//        titles = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (!response.getString("Firstname").isEmpty() || !response.getString("Lastname").isEmpty()) {
                                userFullName = response.getString("FirstName") + response.getString("Lastname");
                            }
                            else {
                                userFullName = "Пользователь";

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }



    @Override
    public void onBindViewHolder(final ListingRVViewHolder holder, int position) {
        Listings item = listings.get(position);
        holder.username.setText(item.getUsername());
        holder.username.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        holder.address.setText(item.getAddress());
        holder.totalArea.setText(item.getTotalArea());
        holder.description.setText(item.getDescription());
//        holder.RoomCount.setText(item.getRoomCount());
        holder.Price.setText(item.getPrice());



        searchListingRequest(item.getUserId());

        holder.Id = item.getId();
        holder.thumb.removeAllSliders();
        for (int i = 0; i < item.getAllPictures().size(); i++) {
            if (item.getAllPictures().get(i).getPictureSize() == 1) {
                DefaultSliderView dsv = new DefaultSliderView(activity.getApplicationContext());
                dsv.image("http://altynorda.kz" + item.getAllPictures().get(i).getImageUrl());
                holder.thumb.addSlider(dsv);
            }
        }


//        holder.commentsListingFull
        for (int i = 0; i < item.getAllComments().size(); i++) {
            //textViewAuthor
            holder.commentsListingFull.setTextSize(25);
            holder.commentsListingFull.setTextColor(Color.BLACK);
//            searchListingRequest(item.getUserId());
//            holder.commentsListingFull.append(userFullName);
            holder.commentsListingFull.append(item.getAllComments().get(i).getUserId().toString());
            holder.commentsListingFull.append(System.getProperty("line.separator"));
            holder.commentsListingFull.setTextSize(18);
//            holder.commentsListingFull.setTextColor(Color.GRAY);
            holder.commentsListingFull.append(item.getAllComments().get(i).getText().toString());
            holder.commentsListingFull.append(System.getProperty("line.separator"));
            holder.commentsListingFull.append(System.getProperty("line.separator"));


        }

        holder.linearLayoutCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(v.getContext(), InsideListingActivity.class);
//                intent.putExtra("IdToInside", holder.Id);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                v.getContext().getApplicationContext().startActivity(intent);
                Snackbar.make(v, "Cliked", Snackbar.LENGTH_SHORT).show();
            }
        });


//        favoritesId.add(26);
//        favoritesId.add(28);

        holder.openReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToReserveActivity = new Intent(v.getContext(), ReserveActivity.class);
                goToReserveActivity.putExtra("ListingIdFromListingRVAdapter", holder.Id);

                v.getContext().startActivity(goToReserveActivity);
            }
        });


        holder.favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


//                int id = holder.Id;

                if (isFavorite) {

//                    favoritesId.add(id);

//                    String favorites = favoritesId.toString();

                    SharedPreferences sp = activity.getSharedPreferences("FavoritesPrefs", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("favoriteId", holder.Id);
                    editor.commit();

                    holder.favorite.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    isFavorite = false;
                } else {


//                    Iterator<Integer> iterator = favoritesId.iterator();
//                    while(iterator.hasNext())
//                    {
//                        int value = iterator.next();
//                        if (id == value) {
//                            iterator.remove();
//                            break;
//                        }
//                    }

                    holder.favorite.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    isFavorite = true;
                }


            }
        });


//        holder.thumb.setImageUrl("http://altynorda.kz"+item.getAllPictures().get(0).getImageUrl(), AppController.getInstance().getImageLoader());
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class ListingRVViewHolder extends RecyclerView.ViewHolder {
        private TextView username, address, description, totalArea, RoomCount, commentsListingFull,
                Price;
        private SliderLayout thumb;
        private ImageButton favorite;
        //        EditText comment;
//        Button sendComment;
        private Button openReserve;
        private int Id;

        private LinearLayout linearLayoutCardLayout;

        public ListingRVViewHolder(View itemView) {
            super(itemView);

            commentsListingFull = (TextView) itemView.findViewById(R.id.commentsListingFull);
            openReserve = (Button) itemView.findViewById(R.id.openReserve);
            description = (TextView) itemView.findViewById(R.id.description);
            thumb = (SliderLayout) itemView.findViewById(R.id.thumbnailImageViewListingRow);
            username = (TextView) itemView.findViewById(R.id.usernameTVListingRow);
            address = (TextView) itemView.findViewById(R.id.addressTextViewListingRow);
            totalArea = (TextView) itemView.findViewById(R.id.areaTextViewListingRow);
            //  RoomCount = (TextView) itemView.findViewById(R.id.roomsTextViewListingRow);
            Price = (TextView) itemView.findViewById(R.id.priceTextViewListingRow);
            favorite = (ImageButton) itemView.findViewById(R.id.favorite);

//            comment = (EditText) itemView.findViewById(R.id.comment);
//            sendComment = (Button) itemView.findViewById(R.id.sendComment);
//            sendComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(v.getContext(), intentData + "", Toast.LENGTH_SHORT).show();
////                    postComment();
//                    Toast.makeText(v.getContext(), "sdf", Toast.LENGTH_SHORT).show();
//                }
//            });

            linearLayoutCardLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutCardLayout);
        }
    }
}
