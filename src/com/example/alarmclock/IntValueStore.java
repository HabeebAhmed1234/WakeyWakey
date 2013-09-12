package com.example.alarmclock;

import android.util.Log;

/**
 * A store of an int value. You can register a listener that will be notified
 * when the value changes.
 */
public class IntValueStore {

    /**
     * The current value.
     */
    int mValue;

    /**
     * The listener (you might want turn this into an array to support many
     * listeners)
     */
    private IntValueStoreListener mListener;

    /**
     * Construct a the int store.
     *
     * @param initialValue The initial value.
     */
    public IntValueStore(int initialValue) {
        mValue = initialValue;
    }

    /**
     * Sets a listener on the store. The listener will be modified when the
     * value changes.
     *
     * @param listener The {@link IntValueStoreListener}.
     */
    public void setListener(IntValueStoreListener listener) {
        mListener = listener;
    }

    /**
     * Set a new int value.
     *
     * @param newValue The new value.
     */
    public void setValue(int newValue) {
        mValue = newValue;
    	Log.d("accel", "changed value 1");
        if (mListener != null && mValue > 100) {
        	Log.d("accel", "changed value 2");
            mListener.onValueChanged(mValue);
        }
    }

    /**
     * Get the current value.
     *
     * @return The current int value.
     */
    public int getValue() {
        return mValue;
    }

    /**
     * Callbacks by {@link IntValueModel}.
     */
    public static interface IntValueStoreListener {
        /**
         * Called when the value of the int changes.
         *
         * @param newValue The new value.
         */
        void onValueChanged(int newValue);
    }
}