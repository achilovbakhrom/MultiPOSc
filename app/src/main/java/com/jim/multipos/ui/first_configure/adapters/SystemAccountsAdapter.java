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
import com.jim.multipos.ui.first_configure.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 05.08.17.
 */

public class SystemAccountsAdapter extends RecyclerView.Adapter<SystemAccountsAdapter.ViewHolder> {
    private List<Account> accounts;
    private String[] accountTypes;
    private String[] circulations;
    private OnClick onClickCallback;

    public SystemAccountsAdapter(List<Account> accounts, String[] accountTypes, String[] circulations, OnClick onClickCallback) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        this.circulations = circulations;
        this.onClickCallback = onClickCallback;
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
        holder.tvType.setText(getAccountType(account));
        holder.tvCirculation.setText(getCirculation(account));
    }

    private String getAccountType(Account account) {
        String type;

        if (account.getType().equals(Constants.TYPE_STANDART)) {
            type = accountTypes[0];
        } else {
            type = accountTypes[1];
        }

        return type;
    }

    private String getCirculation(Account account) {
        String circulation;

        if (account.getCirculation().equals(Constants.CIRCULATION_TO_TILL)) {
            circulation = circulations[0];
        } else {
            circulation = circulations[1];
        }

        return circulation;
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public interface OnClick {
        void removeAccount(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAccountName)
        TextView tvAccountName;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvCirculation)
        TextView tvCirculation;
        @BindView(R.id.ivRemove)
        ImageView ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            RxView.clicks(ivRemove).subscribe(aVoid -> {
                onClickCallback.removeAccount(getAdapterPosition());
            });
        }
    }
}
