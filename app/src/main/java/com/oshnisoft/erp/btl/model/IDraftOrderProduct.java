package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;

import java.util.Collections;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class IDraftOrderProduct extends RealmObject implements IItem<IDraftOrderProduct, IDraftOrderProduct.ViewHolder> {

    //"order_id": 1,
    //                    "product_id": 1,
    //                    "order_detail_id": null,
    //                    "trade_offer_id": null,
    //                    "trade_offer_item_id": null,
    //                    "unit_price": "2650.00",
    //                    "quantity": 4,
    //                    "price": "10600.00",
    //                    "final_quantity": 4,
    //                    "final_price": "10600.00",
    //                    "name": "2.0 rm-Copper"
    private long product_id;
    @Ignore
    private long order_detail_id;
    @Ignore
    private long trade_offer_id;
    @Ignore
    private long trade_offer_item_id;
    private int quantity;
    @SerializedName("final_quantity")
    @Expose
    @Ignore
    private int invoice_quantity;
    private double unit_price;
    private long localOrderId;
    @Ignore
    private long order_id;
    private String name;
    @Ignore
    private String serial;

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
    public IDraftOrderProduct withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IDraftOrderProduct withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDraftOrderProduct withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDraftOrderProduct withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public long getTrade_offer_id() {
        return trade_offer_id;
    }

    public void setTrade_offer_id(long trade_offer_id) {
        this.trade_offer_id = trade_offer_id;
    }

    public long getLocalOrderId() {
        return localOrderId;
    }

    public void setLocalOrderId(long localOrderId) {
        this.localOrderId = localOrderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(long order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public long getTrade_offer_item_id() {
        return trade_offer_item_id;
    }

    public void setTrade_offer_item_id(long trade_offer_item_id) {
        this.trade_offer_item_id = trade_offer_item_id;
    }

    public int getInvoice_quantity() {
        return invoice_quantity;
    }

    public void setInvoice_quantity(int invoice_quantity) {
        this.invoice_quantity = invoice_quantity;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
        return R.layout.item_draft_order_product;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder                           = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
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


        holder.txtSerial.setText(serial);
        holder.txtName.setText(name);
        holder.txtQuantity.setText(""+quantity);
        holder.txtUnitPrice.setText(""+unit_price);


    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtSerial.setText(null);
        holder.txtName.setText(null);
        holder.txtQuantity.setText(null);
        holder.txtUnitPrice.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IDraftOrderProduct withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtSerial;
        TextView txtName;
        TextView txtQuantity;
        TextView txtUnitPrice;
        public ViewHolder(View itemView) {
            super(itemView);

            txtSerial                                        = itemView.findViewById(R.id.txtSerial);
            txtName                                        = itemView.findViewById(R.id.txtName);
            txtQuantity                                    = itemView.findViewById(R.id.txtQuantity);
            txtUnitPrice                                    = itemView.findViewById(R.id.txtUnitPrice);



        }
    }
}
