package com.oshnisoft.erp.btl.listener;

import com.oshnisoft.erp.btl.model.Customer;

public interface SetCustomerListener {
    void onSetCustomer(Customer customer);
    void onNoCustomerSet();
}
