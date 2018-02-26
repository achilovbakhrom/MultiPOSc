package com.jim.mpviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.mpviews.utils.Utils;
import com.jim.mpviews.utils.VibrateManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by developer on 22.08.2017.
 */

public class MpMiniList extends FrameLayout {
    private Context context;
    private OnPaymentChangeListner onPaymentClickListner;

    public interface OnPaymentChangeListner {
        void paymentSelected(int position);
    }

    private MpPayments mpFirstPayment, mpSecondPayment, mpThirdPayment, mpFourPayment;
    private LinearLayout mpPrev, mpNext, mpIndicatorContainer, llNextPrev;
    private List<PaymentTypeWithService> paymentTypes;

    public MpMiniList(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MpMiniList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpMiniList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnPaymentClickListner(OnPaymentChangeListner onPaymentClickListner) {
        this.onPaymentClickListner = onPaymentClickListner;
        mpFirstPayment.setTouchCustom(() -> {
            mpSecondPayment.setChecked(false);
            mpThirdPayment.setChecked(false);
            mpFourPayment.setChecked(false);
        });
        mpSecondPayment.setTouchCustom(() -> {
            mpFirstPayment.setChecked(false);
            mpThirdPayment.setChecked(false);
            mpFourPayment.setChecked(false);
        });
        mpThirdPayment.setTouchCustom(() -> {
            mpFirstPayment.setChecked(false);
            mpSecondPayment.setChecked(false);
            mpFourPayment.setChecked(false);
        });
        mpFourPayment.setTouchCustom(() -> {
            mpFirstPayment.setChecked(false);
            mpSecondPayment.setChecked(false);
            mpThirdPayment.setChecked(false);
        });
        mpPrev.setOnClickListener(view -> {
            toPrevPageIndecatior();

        });

        mpNext.setOnClickListener(view -> {
            toNextPageIndecatior();

        });


        mpPrev.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrateManager.startVibrate(context, 50);
                    mpPrev.setBackgroundResource(R.drawable.left_oval_button_payment_next_pressed);
                    break;
                case MotionEvent.ACTION_UP:
                    mpPrev.setBackgroundResource(R.drawable.left_oval_button_payment_next);
                    break;
            }
            return false;
        });
        mpNext.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrateManager.startVibrate(context, 50);
                    mpNext.setBackgroundResource(R.drawable.right_oval_button_payment_next_pressed);
                    break;
                case MotionEvent.ACTION_UP:
                    mpNext.setBackgroundResource(R.drawable.right_oval_button_payment_next);
                    break;
            }
            return false;
        });

        mpFirstPayment.setOnClickListener(view -> {
            if (onPaymentClickListner != null)
                onPaymentClickListner.paymentSelected(currentIndicator * 3);
            currentButtonState = 0;
            currentPageState = currentIndicator;
        });
        mpSecondPayment.setOnClickListener(view -> {
            if (onPaymentClickListner != null)
                onPaymentClickListner.paymentSelected(currentIndicator * 3 + 1);
            currentButtonState = 1;
            currentPageState = currentIndicator;

        });
        mpThirdPayment.setOnClickListener(view -> {
            if (onPaymentClickListner != null)
                onPaymentClickListner.paymentSelected(currentIndicator * 3 + 2);
            currentButtonState = 2;
            currentPageState = currentIndicator;

        });
        mpFourPayment.setOnClickListener(view -> {
            if (onPaymentClickListner != null)
                onPaymentClickListner.paymentSelected(currentIndicator * 3 + 3);
            currentButtonState = 3;
            currentPageState = currentIndicator;

        });
        onPaymentClickListner.paymentSelected(0);

    }

    public void setPayments(List<PaymentTypeWithService> paymentTypes) {
        this.paymentTypes = paymentTypes;

        if (paymentTypes.size() <= 4)
            singleMode();
        else
            addIndicators();
        mpFirstPayment.setChecked(true);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_mini_list_layout, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        paymentTypes = new ArrayList<>();
        paymentTypes = new ArrayList<>();
        mpFirstPayment = (MpPayments) findViewById(R.id.mpFirstPayment);
        mpSecondPayment = (MpPayments) findViewById(R.id.mpSecondPayment);
        mpThirdPayment = (MpPayments) findViewById(R.id.mpThirdPayment);
        mpFourPayment = (MpPayments) findViewById(R.id.mpFourPayment);
        mpIndicatorContainer = (LinearLayout) findViewById(R.id.mpIndicatorContainer);
        llNextPrev = (LinearLayout) findViewById(R.id.llNextPrev);
        mpPrev = (LinearLayout) findViewById(R.id.mpPrev);
        mpNext = (LinearLayout) findViewById(R.id.mpNext);

    }

    int currentButtonState = 0;
    int currentPageState = 0;

    public void changePayments() {
        int pages = indecators.length;
        int position = currentIndicator * 3;
        if (currentIndicator == currentPageState) {
            switch (currentButtonState) {
                case 0:
                    mpFirstPayment.setChecked(true);
                    mpSecondPayment.setChecked(false);
                    mpThirdPayment.setChecked(false);
                    mpFourPayment.setChecked(false);
                    break;
                case 1:
                    mpSecondPayment.setChecked(true);
                    mpFirstPayment.setChecked(false);
                    mpThirdPayment.setChecked(false);
                    mpFourPayment.setChecked(false);
                    break;
                case 2:
                    mpThirdPayment.setChecked(true);
                    mpFirstPayment.setChecked(false);
                    mpSecondPayment.setChecked(false);
                    mpFourPayment.setChecked(false);
                    break;
                case 3:
                    mpFourPayment.setChecked(true);
                    mpThirdPayment.setChecked(false);
                    mpFirstPayment.setChecked(false);
                    mpSecondPayment.setChecked(false);
                    break;
            }
        } else {
            mpFirstPayment.setChecked(false);
            mpSecondPayment.setChecked(false);
            mpThirdPayment.setChecked(false);
            mpFourPayment.setChecked(false);
        }
        if (pages - 1 == currentIndicator) {
            switch (paymentTypes.size() % 3) {
                case 0:
                    setFirstText(paymentTypes.get(position).getPaymentType(), paymentTypes.get(position).getServiceAbr());
                    setSecondText(paymentTypes.get(position + 1).getPaymentType(), paymentTypes.get(position + 1).getServiceAbr());
                    setThirdText(paymentTypes.get(position + 2).getPaymentType(), paymentTypes.get(position + 2).getServiceAbr());
                    mpFirstPayment.setVisibility(VISIBLE);
                    mpSecondPayment.setVisibility(VISIBLE);
                    mpThirdPayment.setVisibility(VISIBLE);
                    break;
                case 1:
                    setFirstText(paymentTypes.get(position).getPaymentType(), paymentTypes.get(position).getServiceAbr());

                    mpFirstPayment.setVisibility(VISIBLE);
                    mpSecondPayment.setVisibility(INVISIBLE);
                    mpThirdPayment.setVisibility(INVISIBLE);

                    break;
                case 2:
                    setFirstText(paymentTypes.get(position).getPaymentType(), paymentTypes.get(position).getServiceAbr());
                    setSecondText(paymentTypes.get(position + 1).getPaymentType(), paymentTypes.get(position + 1).getServiceAbr());

                    mpFirstPayment.setVisibility(VISIBLE);
                    mpSecondPayment.setVisibility(VISIBLE);
                    mpThirdPayment.setVisibility(INVISIBLE);
                    break;
            }
        } else {
            setFirstText(paymentTypes.get(position).getPaymentType(), paymentTypes.get(position).getServiceAbr());
            setSecondText(paymentTypes.get(position + 1).getPaymentType(), paymentTypes.get(position + 1).getServiceAbr());
            setThirdText(paymentTypes.get(position + 2).getPaymentType(), paymentTypes.get(position + 2).getServiceAbr());
            mpFirstPayment.setVisibility(VISIBLE);
            mpSecondPayment.setVisibility(VISIBLE);
            mpThirdPayment.setVisibility(VISIBLE);
        }
    }


    public void setItems(List<PaymentTypeWithService> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public void setFirstText(String type, String percent) {
        mpFirstPayment.setType(type);
        mpFirstPayment.setPercent(percent);
    }

    public void setSecondText(String type, String percent) {
        mpSecondPayment.setType(type);
        mpSecondPayment.setPercent(percent);
    }

    public void setThirdText(String type, String percent) {
        mpThirdPayment.setType(type);
        mpThirdPayment.setPercent(percent);
    }

    public void setFourText(String type, String percent) {
        mpFourPayment.setType(type);
        mpFourPayment.setPercent(percent);
    }

    ImageView[] indecators;
    int currentIndicator = 0;

    private void singleMode() {
        if (paymentTypes.size() <= 3) {
            llNextPrev.setVisibility(GONE);
            mpFourPayment.setVisibility(GONE);

            switch (paymentTypes.size() % 3) {
                case 0:
                    setFirstText(paymentTypes.get(0).getPaymentType(), paymentTypes.get(0).getServiceAbr());
                    setSecondText(paymentTypes.get(1).getPaymentType(), paymentTypes.get(1).getServiceAbr());
                    setThirdText(paymentTypes.get(2).getPaymentType(), paymentTypes.get(2).getServiceAbr());
                    mpFirstPayment.setVisibility(VISIBLE);
                    mpSecondPayment.setVisibility(VISIBLE);
                    mpThirdPayment.setVisibility(VISIBLE);
                    break;
                case 1:
                    setFirstText(paymentTypes.get(0).getPaymentType(), paymentTypes.get(0).getServiceAbr());

                    mpFirstPayment.setVisibility(VISIBLE);
                    mpSecondPayment.setVisibility(INVISIBLE);
                    mpThirdPayment.setVisibility(INVISIBLE);

                    break;
                case 2:
                    setFirstText(paymentTypes.get(0).getPaymentType(), paymentTypes.get(0).getServiceAbr());
                    setSecondText(paymentTypes.get(1).getPaymentType(), paymentTypes.get(1).getServiceAbr());

                    mpFirstPayment.setVisibility(VISIBLE);
                    mpSecondPayment.setVisibility(VISIBLE);
                    mpThirdPayment.setVisibility(INVISIBLE);
                    break;
            }
        } else if (paymentTypes.size() == 4) {
            llNextPrev.setVisibility(GONE);
            mpFourPayment.setVisibility(VISIBLE);
            setFirstText(paymentTypes.get(0).getPaymentType(), paymentTypes.get(0).getServiceAbr());
            setSecondText(paymentTypes.get(1).getPaymentType(), paymentTypes.get(1).getServiceAbr());
            setThirdText(paymentTypes.get(2).getPaymentType(), paymentTypes.get(2).getServiceAbr());
            setFourText(paymentTypes.get(3).getPaymentType(), paymentTypes.get(3).getServiceAbr());
            mpFirstPayment.setVisibility(VISIBLE);
            mpSecondPayment.setVisibility(VISIBLE);
            mpThirdPayment.setVisibility(VISIBLE);
            mpFourPayment.setVisibility(VISIBLE);
        }
    }

    private void addIndicators() {
        int size = paymentTypes.size();
        int length = (int) Math.ceil(size / 3.0);
        indecators = new ImageView[length];
        for (int i = 0; i < length; i++) {
            indecators[i] = new ImageView(getContext());
        }
        currentIndicator = 0;
        for (int i = 0; i < length; i++) {
            if (i == 0)
                indecators[i].setImageResource(R.drawable.payment_ellipse);
            else indecators[i].setImageResource(R.drawable.payment_ellipse_white);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dpToPx(7), Utils.dpToPx(7));
            lp.setMargins(Utils.dpToPx(3), 0, Utils.dpToPx(3), 0);
            indecators[i].setLayoutParams(lp);

            mpIndicatorContainer.addView(indecators[i]);
        }
        for (int i = 0; i < length; i++) {
            if (i == 0)
                indecators[i].animate().setDuration(150).scaleX(1.2f).scaleY(1.2f).setInterpolator(new AccelerateInterpolator()).start();
            else {
                indecators[i].animate().setDuration(100).scaleX(0.83333f).scaleY(0.83333f).setInterpolator(new DecelerateInterpolator()).start();

            }
        }
        changePayments();

    }

    private void toNextPageIndecatior() {
        int buttosLength = indecators.length;
        int prevPosition = currentIndicator;
        currentIndicator++;
        currentIndicator %= buttosLength;
        indecators[currentIndicator].animate().setDuration(150).scaleX(1.2f).scaleY(1.2f).setInterpolator(new AccelerateInterpolator()).start();
        indecators[prevPosition].animate().setDuration(100).scaleX(0.83333f).scaleY(0.83333f).setInterpolator(new DecelerateInterpolator()).start();
        indecators[currentIndicator].setImageResource(R.drawable.payment_ellipse);
        indecators[prevPosition].setImageResource(R.drawable.payment_ellipse_white);
        changePayments();
    }

    private void toPrevPageIndecatior() {
        int buttosLength = indecators.length;
        int prevPosition = currentIndicator;
        currentIndicator--;
        currentIndicator = (currentIndicator < 0) ? buttosLength - 1 : currentIndicator;

        indecators[currentIndicator].animate().setDuration(150).scaleX(1.2f).scaleY(1.2f).setInterpolator(new AccelerateInterpolator()).start();
        indecators[prevPosition].animate().setDuration(100).scaleX(0.83333f).scaleY(0.83333f).setInterpolator(new DecelerateInterpolator()).start();

        indecators[currentIndicator].setImageResource(R.drawable.payment_ellipse);
        indecators[prevPosition].setImageResource(R.drawable.payment_ellipse_white);
        changePayments();
    }

    public void setPaymentViewWidth(int width) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mpFirstPayment.getLayoutParams();
        layoutParams.width = Utils.dpToPx(width);
        mpFirstPayment.setLayoutParams(layoutParams);
        mpSecondPayment.setLayoutParams(layoutParams);
        mpThirdPayment.setLayoutParams(layoutParams);
        mpFourPayment.setLayoutParams(layoutParams);
        llNextPrev.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mpNext.getLayoutParams();
        params.width = Utils.dpToPx(width/2);
        mpNext.setLayoutParams(params);
        mpPrev.setLayoutParams(params);

    }
}
