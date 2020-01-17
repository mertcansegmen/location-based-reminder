package com.mertcansegmen.locationbasedreminder.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * ItemDecoration class for proper spacing between items in recycler view.
 */

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int columnCount;
    private int spacingInPx;

    public SpacingItemDecoration(int columnCount, int spacingInDp, Context context) {
        this.columnCount = columnCount;
        this.spacingInPx = ConfigUtils.convertDpToPixels(context, spacingInDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % columnCount; // item column

        outRect.left = spacingInPx - column * spacingInPx / columnCount;
        outRect.right = (column + 1) * spacingInPx / columnCount;

        if (position < columnCount) { // top edge
            outRect.top = spacingInPx;
        }
        outRect.bottom = spacingInPx; // item bottom
    }
}