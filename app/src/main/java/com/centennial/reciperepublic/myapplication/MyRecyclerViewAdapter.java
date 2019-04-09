package com.centennial.reciperepublic.myapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<EdamamModel.Recipe> recipeArrayList;
    private ArrayList<EdamamModel.Recipe> recipeArrayListFull;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView recipeImage;
        TextView recipeLabel;
        TextView recipeSource;


        public DataObjectHolder(View itemView, final MyClickListener listener) {
            super(itemView);

            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
            recipeLabel = (TextView) itemView.findViewById(R.id.recipeLabel);
            recipeSource = (TextView) itemView.findViewById(R.id.recipeSource);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, v);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<EdamamModel.Recipe> myDataset) {
        recipeArrayList = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view, myClickListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        new MyRecyclerViewAdapter.DownloadImageTask(holder.recipeImage).execute(recipeArrayList.get(position).getImage());
        holder.recipeLabel.setText(recipeArrayList.get(position).getLabel());
        holder.recipeSource.setText("By "+ recipeArrayList.get(position).getSource());
    }

    public void addItem(EdamamModel.Recipe dataObj, int index) {
        recipeArrayList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        recipeArrayList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}