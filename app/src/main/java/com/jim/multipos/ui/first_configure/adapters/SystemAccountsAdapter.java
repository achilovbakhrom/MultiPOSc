package com.jim.multipos.ui.first_configure.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 05.08.17.
 */

public class SystemAccountsAdapter extends RecyclerView.Adapter<SystemAccountsAdapter.ViewHolder> {
    private List<Account> accounts;
    private OnClickListener onClickListener;

    public SystemAccountsAdapter(List<Account> accounts, String[] accountTypes, String[] circulations, OnClickListener onClickListener) {
        this.accounts = accounts;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Account account = accounts.get(position);

        holder.tvAccountName.setText(account.getName());
    }

    public void addItem(Account account) {
        accounts.add(0, account);
        notifyDataSetChanged();
    }

    public void removeItem(Account account) {
        accounts.remove(account);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public interface OnClickListener {
        void removeAccount(Account account);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAccountName)
        TextView tvAccountName;
        @BindView(R.id.ivRemove)
        ImageView ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            RxView.clicks(ivRemove).subscribe(aVoid -> {
                onClickListener.removeAccount(accounts.get(getAdapterPosition()));
            });
        }
    }
}
