package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class IDraftOrder extends RealmObject implements IItem<IDraftOrder, IDraftOrder.ViewHolder>, Serializable {

    private long id;
    private long user_id;
    private double total_amount;
    private String ordered_at;
    @SerializedName("user_role")
    private String type;
    @SerializedName("user_name")
    private String name;
    @Ignore
    private String ordered_by;
    @Ignore
    private String approved_by;
    @Ignore
    private String processed_by;
    @Ignore
    private String invoiced_by;
    @Ignore
    private String status;

    @PrimaryKey
    private long localId;

    private String delivery_date;

    private String note;

    @Ignore
    List<IDraftOrderProduct> items;

    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
    private boolean isEnabled = true;
    @Ignore
    private boolean isSelected = false; // defines if the item is selected


    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDraftOrder withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDraftOrder withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDraftOrder withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDraftOrder withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdered_at() {
        return ordered_at;
    }

    public void setOrdered_at(String ordered_at) {
        this.ordered_at = ordered_at;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public List<IDraftOrderProduct> getItems() {
        return items;
    }

    public void setItems(List<IDraftOrderProduct> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrdered_by() {
        return ordered_by;
    }

    public void setOrdered_by(String ordered_by) {
        this.ordered_by = ordered_by;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getProcessed_by() {
        return processed_by;
    }

    public void setProcessed_by(String processed_by) {
        this.processed_by = processed_by;
    }

    public String getInvoiced_by() {
        return invoiced_by;
    }

    public void setInvoiced_by(String invoiced_by) {
        this.invoiced_by = invoiced_by;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        return R.id.rvDetailList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_draft_order;
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

        if (type.equalsIgnoreCase("Customer")) {
            holder.txtName.setText("Customer: " + name);
        } else {
            holder.txtName.setText("Dealer: " + name);
        }

        holder.txtDate.setText(DateTimeUtils.changeDateTimeFormat(ordered_at, DateTimeUtils.FORMAT6, DateTimeUtils.FORMAT7));
        holder.txtStatus.setText(status);
        holder.txtInvoiceAmount.setText("Amount: "+ String.valueOf(total_amount));
        List<IDraftOrderProduct> iDraftOrderProductList = new ArrayList<>();
        int i =0;
        for(IDraftOrderProduct iDraftOrderProduct:items){
            i++;
            iDraftOrderProduct.setSerial(""+i);
            iDraftOrderProductList.add(iDraftOrderProduct);
        }
        final FastItemAdapter<IDraftOrderProduct> fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.add(iDraftOrderProductList);
        fastItemAdapter.setHasStableIds(false);
        holder.rvProducts.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rvProducts.setAdapter(fastItemAdapter);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtDate.setText(null);
        holder.txtStatus.setText(null);
        holder.txtInvoiceAmount.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IDraftOrder withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtDate;
        TextView txtStatus;
        TextView txtInvoiceAmount;
        RecyclerView rvProducts;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtInvoiceAmount = itemView.findViewById(R.id.txtInvoiceAmount);
            rvProducts = itemView.findViewById(R.id.rvProducts);


        }
    }
}
