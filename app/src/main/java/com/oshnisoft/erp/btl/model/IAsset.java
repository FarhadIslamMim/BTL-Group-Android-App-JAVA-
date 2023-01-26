package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;

import java.util.Collections;
import java.util.List;

import io.realm.annotations.Ignore;


public class IAsset implements IItem<IAsset, IAsset.ViewHolder> {

    private long id;
    private long employee_id;
    private String expected_date;
    private String item;
    private String note;
    private int quantity;
    private String status;
    private String feedback;
    private String created_at;
    private String updated_at;


    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
    private boolean isEnabled = true;
    @Ignore
    private boolean isSelected = false; // defines if the item is selected


    public void setId(long id) {
        this.id = id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IAsset withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IAsset withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IAsset withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IAsset withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvAsset;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_asset_history;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    private ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        Context ctx = holder.itemView.getContext();


        holder.date.setText(expected_date);
        holder.tvItem.setText("Item : " +item);
        holder.tvQuantity.setText("Quantity : " +quantity);
        holder.tvNote.setText("Note :" +note);


    }

    @Override
    public void unbindView(ViewHolder holder) {

        holder.date.setText(null);
        holder.tvItem.setText(null);
        holder.tvQuantity.setText(null);
        holder.tvNote.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IAsset withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView tvItem;
        TextView tvQuantity;
        TextView tvNote;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            tvItem = itemView.findViewById(R.id.tvItem);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvNote = itemView.findViewById(R.id.tvNote);


        }
    }
}
