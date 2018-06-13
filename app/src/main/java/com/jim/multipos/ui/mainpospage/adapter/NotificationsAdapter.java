package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.mpviews.utils.Utils;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.NotificationData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 21.12.2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<NotificationData> items;
    private NotificationViewHolder holder;
    private Context context;
    int lastPosition = -1;

    public NotificationsAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    public void setData(NotificationData item) {
        this.items.add(0, item);
        notifyDataSetChanged();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_dialog, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.tvCustomerName.setText(items.get(position).getCustomer().getName());
        holder.tvTotalDiscounts.setText(String.valueOf(items.get(position).getTotalDiscounts()) + " " + items.get(position).getAbbr());
        holder.tvTotalPayments.setText(String.valueOf(items.get(position).getTotalPayments()) + " " + items.get(position).getAbbr());
        runEnterAnimation(holder.itemView, position);
        this.holder = holder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position) {
        runExitAnimation(holder.itemView, position);
    }

    private void runEnterAnimation(View view, int position) {
        if (position > lastPosition) {
            lastPosition = position;

//            ObjectAnimator objectAnimator
//                    = ObjectAnimator.ofFloat(view, "translationX", 1000f, 0f);
//            objectAnimator.setDuration(1000);
//            objectAnimator.setInterpolator(new DecelerateInterpolator(3f));
//            objectAnimator.start();
            view.setTranslationX(Utils.getScreenWidth(context));
            view.animate()
                    .translationX(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(1000)
                    .start();
        }

    }
    private void runExitAnimation(View view, int position) {
        view.animate()
                .alpha(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(500)
                .withEndAction(() -> {
                    items.remove(position);
                    notifyItemRemoved(0);
                    Toast.makeText(context, items.size() + "", Toast.LENGTH_SHORT).show();
                })
                .start();


    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCustomerName)
        TextView tvCustomerName;
        @BindView(R.id.tvCustomerVisits)
        TextView tvCustomerVisits;
        @BindView(R.id.tvTotalDiscounts)
        TextView tvTotalDiscounts;
        @BindView(R.id.tvTotalPayments)
        TextView tvTotalPayments;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
