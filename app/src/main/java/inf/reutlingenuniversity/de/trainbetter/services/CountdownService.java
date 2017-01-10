package inf.reutlingenuniversity.de.trainbetter.services;

/**
 * Created by EL on 08.01.2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

public class CountdownService extends Service {

    public static final String CTS_TICK = "TICK";
    public static final String CTS_FINISH = "FINISH";
    public static final int SECONDS_TO_MILLISECONDS_FACTOR = 1000;

    private CountDownTimer countDownTimer;
    private LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
    private final IBinder BINDER = new LocalBinder();

    public class LocalBinder extends Binder {
        public CountdownService getService() {
            return CountdownService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return BINDER;
    }

    public void countdownTimer(long time){
        countDownTimer = new CountDownTimer(time, CountdownService.SECONDS_TO_MILLISECONDS_FACTOR){

            @Override
            public void onTick(long millisUntilFinished){
                Intent intent = new Intent(CTS_TICK);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFinish(){
                Intent intent = new Intent(CTS_FINISH);
                broadcastManager.sendBroadcast(intent);

            }

        }.start();

    }

    public void cancel(){
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

}