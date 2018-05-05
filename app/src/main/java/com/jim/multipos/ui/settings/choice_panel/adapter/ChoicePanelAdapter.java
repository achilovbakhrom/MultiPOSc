package com.jim.multipos.ui.settings.choice_panel.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.ui.settings.choice_panel.data.PanelData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoicePanelAdapter extends RecyclerView.Adapter<ChoicePanelAdapter.ViewHolder> {
    private List<PanelData> panelData;
    private ChoicePanelListner choicePanelListner;
    int activePanel = 0;

    public interface ChoicePanelListner{
        void onPanelClicked(int clickedPosition);
    }

    public ChoicePanelAdapter(List<PanelData> panelData,ChoicePanelListner choicePanelListner){
        this.panelData = panelData;
        this.choicePanelListner = choicePanelListner;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_choice_panel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position!=activePanel){
            holder.llBackground.setBackgroundColor(Color.TRANSPARENT);
            holder.flActivator.setVisibility(View.INVISIBLE);
        }else {
            holder.llBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.flActivator.setVisibility(View.VISIBLE);
        }
        holder.tvTitleSetting.setText(panelData.get(position).getTitle());
        holder.tvSubTitleSetting.setText(panelData.get(position).getSubTitle());
    }

    @Override
    public int getItemCount() {
        return panelData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.flActivator)
        FrameLayout flActivator;
        @BindView(R.id.tvTitleSetting)
        TextView tvTitleSetting;
        @BindView(R.id.tvSubTitleSetting)
        TextView tvSubTitleSetting;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            llBackground.setOnClickListener(view -> {
                int oldPos = activePanel;
                activePanel = getAdapterPosition();
                choicePanelListner.onPanelClicked(activePanel);
                notifyItemChanged(oldPos);
                notifyItemChanged(activePanel);
            });
        }
    }
}
