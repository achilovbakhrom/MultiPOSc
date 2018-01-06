package com.jim.multipos.ui.mainpospage.model;

import android.os.Parcel;

import com.jim.mpviews.suggestions.model.SearchSuggestion;
import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class CustomerSuggestion implements SearchSuggestion {

    private Customer customer;

    @Override
    public String getBody() {
        return customer.getName();
    }

        @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Creator<CustomerSuggestion> CREATOR = new Creator<CustomerSuggestion>() {
        @Override
        public CustomerSuggestion createFromParcel(Parcel parcel) {
            return new CustomerSuggestion();
        }

        @Override
        public CustomerSuggestion[] newArray(int i) {
            return new CustomerSuggestion[i];
        }
    };

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
