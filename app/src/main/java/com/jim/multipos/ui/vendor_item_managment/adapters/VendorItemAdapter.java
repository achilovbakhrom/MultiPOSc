package com.jim.multipos.ui.vendor_item_managment.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.EllipsizingTextView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;

/**
 * Created by developer on 20.11.2017.
 */

public class VendorItemAdapter extends ClickableBaseAdapter<VendorWithDebt, VendorItemAdapter.VendorItemViewHolder> {


    private OnVendorAdapterCallback onVendorAdapterCallback;
    private Context context;
    private DecimalFormat decimalFormat;
    boolean searchMode = false;
    private String searchText;

    public VendorItemAdapter(List<VendorWithDebt> items, OnVendorAdapterCallback onVendorAdapterCallback, Context context, DecimalFormat decimalFormat) {
        super(items);
        this.onVendorAdapterCallback = onVendorAdapterCallback;
        this.context = context;
        this.decimalFormat = decimalFormat;
    }
    public void setSearchResult(List<VendorWithDebt> searchResult,String searchText){
        this.searchText = searchText;
        searchMode = true;
        items = searchResult;
    }
    public void setData(List<VendorWithDebt> items){
        this.items = items;
        searchMode = false;
    }


    @Override
    protected void onItemClicked(VendorItemViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(VendorItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(position%2==0) holder.llBackground.setBackgroundColor(Color.parseColor("#f9f9f9"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#f0f0f0"));
        VendorWithDebt vendorWithDebt = items.get(position);
        if(!searchMode) {
            setUnderlineText(holder.tvVendorName, vendorWithDebt.getVendor().getName());

            holder.tvContactName.setText(context.getString(R.string.contact_name_two_dots) + " " + vendorWithDebt.getVendor().getContactName());
            if (vendorWithDebt.getVendor().getContacts().size() == 0) {
                holder.tvTel.setVisibility(View.GONE);
            } else {
                holder.tvTel.setVisibility(View.VISIBLE);

                StringBuilder builder = new StringBuilder();
                for (Contact contact : vendorWithDebt.getVendor().getContacts()) {
                    if (contact.getType() == Contact.E_MAIL) {
                        builder.append(context.getString(R.string.email_two_dots));
                        builder.append(" ");
                        builder.append(contact.getName());
                        builder.append("\n");
                    } else if (contact.getType() == Contact.PHONE) {
                        builder.append(context.getString(R.string.phone_two_dots));
                        builder.append(" ");
                        builder.append(contact.getName());
                        builder.append("\n");
                    }
                }
                builder.deleteCharAt(builder.length() - 1);
                holder.tvTel.setText(builder.toString());
            }


            StringBuilder builder = new StringBuilder();
            for (Product product:vendorWithDebt.getVendor().getProducts()) {
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | "); builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
            }
            if(builder.toString().length()>2) {
                builder.deleteCharAt(builder.length() - 1);
                builder.deleteCharAt(builder.length() - 1);
            }
            holder.tvProductNames.setMaxLines(5);
            holder.tvProductNames.setText(builder.toString());
        }else {
            colorSubSeqUnderLine(vendorWithDebt.getVendor().getName(),searchText,Color.parseColor("#95ccee"),holder.tvVendorName);
            colorSubSeq(context.getString(R.string.contact_name_two_dots) + " " + vendorWithDebt.getVendor().getContactName(),searchText,Color.parseColor("#95ccee"),holder.tvContactName);

            if (vendorWithDebt.getVendor().getContacts().size() == 0) {
                holder.tvTel.setVisibility(View.GONE);
            } else {
                StringBuilder builder = new StringBuilder();
                for (Contact contact : vendorWithDebt.getVendor().getContacts()) {
                    if (contact.getType() == Contact.E_MAIL) {
                        builder.append(context.getString(R.string.email_two_dots));
                        builder.append(" ");
                        builder.append(contact.getName());
                        builder.append("\n");
                    } else if (contact.getType() == Contact.PHONE) {
                        builder.append(context.getString(R.string.phone_two_dots));
                        builder.append(" ");
                        builder.append(contact.getName());
                        builder.append("\n");
                    }
                }
                builder.deleteCharAt(builder.length() - 1);
                colorSubSeq(builder.toString(),searchText,Color.parseColor("#95ccee"),holder.tvTel);

            }

            boolean haveContains = false;
            for (Product product:vendorWithDebt.getVendor().getProducts()){
                haveContains = product.getName().toUpperCase().contains(searchText.toUpperCase());
                if(haveContains) break;
            }

            StringBuilder builder = new StringBuilder();
            for (Product product:vendorWithDebt.getVendor().getProducts()) {
                if(haveContains  && (!product.getName().toUpperCase().contains(searchText.toUpperCase()))) continue;
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
                builder.append(product.getName());
                builder.append(" | ");
            }
            if(builder.toString().length()>2) {
                builder.deleteCharAt(builder.length() - 1);
                builder.deleteCharAt(builder.length() - 1);
            }
            colorSubSeq(builder.toString(),searchText,Color.parseColor("#95ccee"),holder.tvProductNames);
        }

        holder.tvProductCount.setText(context.getString(R.string.product_count_two_dots) + " " + decimalFormat.format(vendorWithDebt.getVendor().getProducts().size()));
        holder.tvDebtAmmount.setText(decimalFormat.format(vendorWithDebt.getDebt()));
    }

    @Override
    public VendorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_managment_item, parent, false);
        return new VendorItemViewHolder(view);
    }

    public interface OnVendorAdapterCallback{
        void onIncomeProduct(VendorWithDebt vendorWithDebt);
        void onWriteOff(VendorWithDebt vendorWithDebt);
        void onConsigmentStory(VendorWithDebt vendorWithDebt);
        void onPay(VendorWithDebt vendorWithDebt);
        void onPayStory(VendorWithDebt vendorWithDebt);
        void onMore(VendorWithDebt vendorWithDebt);
    }

    public class VendorItemViewHolder extends BaseViewHolder {
        @BindView(R.id.tvVendorName)
        TextView tvVendorName;
        @BindView(R.id.tvContactName)
        TextView tvContactName;
        @BindView(R.id.tvTel)
        TextView tvTel;
        @BindView(R.id.tvProductCount)
        TextView tvProductCount;
        @BindView(R.id.tvProductNames)
        TextView tvProductNames;
        @BindView(R.id.tvDebtAmmount)
        TextView tvDebtAmmount;
        @BindView(R.id.tvMore)
        TextView tvMore;

        @BindView(R.id.ivPayment)
        ImageView ivPayment;
        @BindView(R.id.ivPaymentsStory)
        ImageView ivPaymentsStory;
        @BindView(R.id.ivStoryConsigment)
        ImageView ivStoryConsigment;
        @BindView(R.id.ivBackReturn)
        ImageView ivBackReturn;
        @BindView(R.id.ivIncome)
        ImageView ivIncome;


        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        public VendorItemViewHolder(View itemView) {
            super(itemView);
            ivPayment.setOnClickListener(view1 -> onVendorAdapterCallback.onPay(items.get(getAdapterPosition())));
            ivPaymentsStory.setOnClickListener(view1 -> onVendorAdapterCallback.onPayStory(items.get(getAdapterPosition())));
            ivBackReturn.setOnClickListener(view1 -> onVendorAdapterCallback.onWriteOff(items.get(getAdapterPosition())));
            ivIncome.setOnClickListener(view1 -> onVendorAdapterCallback.onIncomeProduct(items.get(getAdapterPosition())));
            tvMore.setOnClickListener(view1 -> onVendorAdapterCallback.onMore(items.get(getAdapterPosition())));
            ivStoryConsigment.setOnClickListener(view1 -> onVendorAdapterCallback.onConsigmentStory(items.get(getAdapterPosition())));
        }
    }
    public void setUnderlineText(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }
    public  void  colorSubSeq(String text, String whichWordColor, int colorCode, TextView textView) {
        String textUpper = text.toUpperCase();
        String whichWordColorUpper = whichWordColor.toUpperCase();
        SpannableString ss = new SpannableString(text);
        int strar = 0;

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.length() != 0) {
            ss.setSpan(new BackgroundColorSpan(colorCode), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length();
        }
        textView.setText(ss);
    }

    public void colorSubSeqUnderLine(String text, String whichWordColor, int colorCode, TextView textView) {
        String textUpper = text.toUpperCase();
        String whichWordColorUpper = whichWordColor.toUpperCase();
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
        int strar = 0;

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.length() != 0) {
            ss.setSpan(new BackgroundColorSpan(colorCode), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length();
        }
        textView.setText(ss);
    }
}
