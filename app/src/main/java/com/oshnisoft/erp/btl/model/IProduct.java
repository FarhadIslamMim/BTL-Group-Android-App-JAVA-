package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class IProduct implements IItem<IProduct, IProduct.ViewHolder>, Serializable {


    private long id;
    private String name;
    private String category;
    private String unit;
    private double price;
    private int quantity;
    private String tradeOffer;

    public boolean isDelete = false;


    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    private boolean isSelected = false; // defines if the item is selected


    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IProduct withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IProduct withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IProduct withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IProduct withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getId() {
        return id;
    }


    public double getPrice() {
        return price;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTradeOffer() {
        return tradeOffer;
    }

    public void setTradeOffer(String tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int getType() {
        return R.id.rvList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_add_customer_order;
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


        holder.txtName.setText(name);
        holder.txtQuantity.setText("" + quantity);
        if (tradeOffer == null) {
            holder.txtTradeOffer.setVisibility(View.INVISIBLE);
        } else {
            holder.txtTradeOffer.setVisibility(View.VISIBLE);
            holder.txtTradeOffer.setText("Trade Offer: " + tradeOffer);
        }
        holder.txtUnit.setText(unit);
        holder.txtCategory.setText(category);
        holder.txtMarketPrice.setText("Price: " + price);


        if (isDelete) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }


    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtQuantity.setText(null);
        holder.txtTradeOffer.setText(null);
        holder.txtUnit.setText(null);
        holder.txtCategory.setText(null);
        holder.txtMarketPrice.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IProduct withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtMarketPrice;
        public TextView txtCategory;
        public TextView txtUnit;
        public TextView txtTradeOffer;
        public TextView txtQuantity;
        public ImageView ivMinus, ivPlus, delete;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtMarketPrice = itemView.findViewById(R.id.txtMarketPrice);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            txtTradeOffer = itemView.findViewById(R.id.txtTradeOffer);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}
