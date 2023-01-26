package com.oshnisoft.erp.btl.ui.order;

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


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class ICustomer implements IItem<ICustomer, ICustomer.ViewHolder> {


    private long id;
    private String name;
    private String contact_person;
    private String primary_mobile_number;
    private String site_district;
    private String site_thana;
    private String site_address_line;
    private long dealer_id;
    private long zone_id;


    private Object tag;// defines if this item is isSelectable

    private boolean isSelectable = true;

    private boolean isEnabled = true;

    private boolean isSelected = false; // defines if the item is selected



    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public ICustomer withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ICustomer withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ICustomer withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ICustomer withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getId() {
        return id;
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

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }



    public String getPrimary_mobile_number() {
        return primary_mobile_number;
    }

    public void setPrimary_mobile_number(String primary_mobile_number) {
        this.primary_mobile_number = primary_mobile_number;
    }


    public String getSite_district() {
        return site_district;
    }

    public void setSite_district(String site_district) {
        this.site_district = site_district;
    }

    public String getSite_thana() {
        return site_thana;
    }

    public void setSite_thana(String site_thana) {
        this.site_thana = site_thana;
    }

    public String getSite_address_line() {
        return site_address_line;
    }

    public void setSite_address_line(String site_address_line) {
        this.site_address_line = site_address_line;
    }

    public long getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(long dealer_id) {
        this.dealer_id = dealer_id;
    }

    public long getZone_id() {
        return zone_id;
    }

    public void setZone_id(long zone_id) {
        this.zone_id = zone_id;
    }


    @Override
    public int getType() {
        return R.id.rvList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_set_customer;
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
        String addrerss = site_address_line +", "+site_thana + ", " + site_district;
        holder.txtName.setText(contact_person);
        holder.txtTradeName.setText(name);
        holder.txtAddress.setText(addrerss);
        holder.txtPhoneNo.setText(primary_mobile_number);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtTradeName.setText(null);
        holder.txtAddress.setText(null);
        holder.txtPhoneNo.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public ICustomer withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtTradeName;
        TextView txtAddress;
        TextView txtPhoneNo;
        public ViewHolder(View itemView) {
            super(itemView);

            txtName                                             = itemView.findViewById(R.id.txtName);
            txtTradeName                                        = itemView.findViewById(R.id.txtTradeName);
            txtAddress                                          = itemView.findViewById(R.id.txtAddress);
            txtPhoneNo                                          = itemView.findViewById(R.id.txtPhoneNo);

        }
    }
}
