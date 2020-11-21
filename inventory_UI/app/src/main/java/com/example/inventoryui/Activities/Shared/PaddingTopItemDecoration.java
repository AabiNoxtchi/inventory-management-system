package com.example.inventoryui.Activities.Shared;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class PaddingTopItemDecoration extends RecyclerView.ItemDecoration {
    private final int size;

    public PaddingTopItemDecoration(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top += size;
        }
    }
}
