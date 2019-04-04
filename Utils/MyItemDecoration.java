package com.aktivo.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by agc-linux on 14/9/17.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration{

    private Paint paintBlue, paintRed;
    private int offset;

    public MyItemDecoration(){
        offset = 5;
        paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBlue.setColor(Color.TRANSPARENT);
        paintBlue.setStyle(Paint.Style.STROKE);
        paintBlue.setStrokeWidth(3);

        paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRed.setColor(Color.TRANSPARENT);
        paintRed.setStyle(Paint.Style.STROKE);
        paintRed.setStrokeWidth(1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(offset, offset, offset, offset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for(int i=0; i<parent.getChildCount(); i++){
            final View child = parent.getChildAt(i);
            c.drawRect(
                    layoutManager.getDecoratedLeft(child),
                    layoutManager.getDecoratedTop(child),
                    layoutManager.getDecoratedRight(child),
                    layoutManager.getDecoratedBottom(child),
                    paintBlue);
            c.drawRect(
                    layoutManager.getDecoratedLeft(child) + offset,
                    layoutManager.getDecoratedTop(child) + offset,
                    layoutManager.getDecoratedRight(child) - offset,
                    layoutManager.getDecoratedBottom(child) - offset,paintRed);

        }
    }
}


