package com.aktivo.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by techiestown on 25/12/17.
 */

public class EndStyledProgressBar extends ProgressBar {

    private static final int DEFAULT_START_OFFSET_PERCENT = 5;

    private int mStartOffset;
    private int mRealMax;

    public EndStyledProgressBar(Context context) {
        super(context);
        commonConstructor();
    }

    public EndStyledProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonConstructor();
    }

    public EndStyledProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        commonConstructor();
    }

    private void commonConstructor() {
        mRealMax = super.getMax();
        mStartOffset = 0;

        setMaxWithPercentOffset(DEFAULT_START_OFFSET_PERCENT, mRealMax);

        super.setProgress(super.getProgress() + mStartOffset);
        super.setSecondaryProgress(super.getSecondaryProgress() + mStartOffset);
    }

    public void setProgress(int progress) {
        super.setProgress(progress + mStartOffset);
    }

    public void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress + mStartOffset);
    }

    public int getProgress() {
        int realProgress = super.getProgress();
        return isIndeterminate() ? 0 : (realProgress - mStartOffset);
    }


    public  int getSecondaryProgress() {
        int realSecondaryProgress = super.getSecondaryProgress();
        return isIndeterminate() ? 0 : (realSecondaryProgress - mStartOffset);
    }

    public int getMax() {
        return mRealMax;
    }


    /**
     * Don't call this, instead call setMaxWithPercentOffset() or setStartOffsetInPercent()
     *
     * @param max
     */
    public void setMax(int max) {
        super.setMax(max);
    }

    /**
     * Sets a new max with a start offset (in percent) included.
     *
     * @param startOffsetInPercent start offset for the progress bar to avoid graphic errors.
     */
    public void setMaxWithPercentOffset(int startOffsetInPercent, int max) {
        mRealMax = max;
        int startOffset = (mRealMax * startOffsetInPercent) / 100;

        setMaxWithOffset(startOffset, max);
    }

    /**
     * Sets a new max with a start offset included.
     *
     * @param startOffset start offset for the progress bar to avoid graphic errors.
     */
    public void setMaxWithOffset(int startOffset, int max) {
        mRealMax = max;

        super.setMax(startOffset + max);

        setStartOffset(startOffset);

        super.setMax(startOffset + max);
    }


    /**
     * Sets a new start offset different from the default of 5%
     *
     * @param startOffset start offset for the progress bar to avoid graphic errors.
     */
    private void setStartOffset(int startOffset) {
        int newStartOffset = startOffset;

        // Ensure the start offset is not outside the range of the progress bar
        if (newStartOffset < 0) newStartOffset = 0;
        if (newStartOffset >= super.getMax()) newStartOffset = super.getMax();

        // Apply the start offset difference
        if (mStartOffset != newStartOffset) {
            int diff = newStartOffset - mStartOffset;
            super.setMax(super.getMax() + diff);
            super.setProgress(super.getProgress() + diff);
            super.setSecondaryProgress(super.getSecondaryProgress() + diff);
            mStartOffset = newStartOffset;
        }
    }

}
