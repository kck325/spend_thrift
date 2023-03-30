package com.panda_cookie.spend_thrift.fragments;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeaderItemDecoration extends RecyclerView.ItemDecoration {
    private final View headerView;

    public HeaderItemDecoration(View headerView) {
        this.headerView = headerView;

        // Measure and layout the header view
        int widthSpec = View.MeasureSpec.makeMeasureSpec(headerView.getContext().getResources().getDisplayMetrics().widthPixels, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(headerView.getContext().getResources().getDisplayMetrics().heightPixels, View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec, headerView.getPaddingLeft() + headerView.getPaddingRight(), headerView.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec, headerView.getPaddingTop() + headerView.getPaddingBottom(), headerView.getLayoutParams().height);

        headerView.measure(childWidth, childHeight);
        headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        c.save();
        c.translate(0, 0);
        headerView.draw(c);
        c.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(0, headerView.getMeasuredHeight(), 0, 0);
        } else {
            outRect.setEmpty();
        }
    }
}
