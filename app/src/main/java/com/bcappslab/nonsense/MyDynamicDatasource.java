package com.bcappslab.nonsense;

import java.util.Observable;

/**
 * Created by Barry on 2014-07-13.
 */
public class MyDynamicDatasource implements Runnable {

    // following the androidplot tutorial

    // encapsulates management of the observers watching the datasource for update events:
    class MyObservable extends Observable {
        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    }

    private static final int MAX_AMP_SEED = 100;
    private static final int MIN_AMP_SEED = 10;
    private static final int AMP_STEP = 5;
    public static final int SINE1 = 0;
    public static final int SINE2 = 1;
    private static final int SAMPLE_SIZE = 30;
    private int phase = 0;
    private int sinAmp = 20;
    private MyObservable notifier;

    {
        notifier = new MyObservable();
    }

    @Override
    public void run() {

    }
}
