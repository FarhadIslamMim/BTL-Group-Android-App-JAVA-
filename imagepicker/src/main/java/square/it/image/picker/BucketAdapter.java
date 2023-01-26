package square.it.image.picker;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.MyViewHolder> {
    List<String> bucketList;

    private OnItemClickListener listener;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView txtBucketName;
        public MyViewHolder(View v) {
            super(v);
            view = v;
            txtBucketName = view.findViewById(R.id.txtBucketName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BucketAdapter(List<String> bucketList, OnItemClickListener listener) {
        this.bucketList = bucketList;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BucketAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bucketpicker, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtBucketName.setText(bucketList.get(position));
        final int pos = position;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(bucketList.get(pos));
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bucketList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }
}
