package inf.reutlingenuniversity.de.trainbetter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PushHandler extends ParsePushBroadcastReceiver {

    private static final String TAG = "NOTIFICATION";
    private Class activity;

    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    public void onPushReceive(Context con, Intent intent){
    try{
        if(intent != null){
            String action = intent.getAction();
            if(action.equals("com.parse.push.intent.RECEIVE")){
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Iterator itr = json.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    if (key.equals("alert")) {
                        if(currentUser != null){
                            activity = WorkoutDetailsActivity.class;
                        }else{
                            activity = StartActivity.class;
                        }
                        Intent showWorkout = new Intent(con, activity);
                        showWorkout.putExtra("id", json.getString(key));
                        PendingIntent pIntent = PendingIntent.getActivity(con, (int) System.currentTimeMillis(), showWorkout, 0);

                        Notification notification = new Notification.Builder(con)
                                .setContentTitle(json.getString("title"))
                                .setContentText("Check it out!")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pIntent).build();

                        NotificationManager notificationManager = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(0, notification);
                    }

                    Log.d(TAG, key + " | " + json.getString(key));
                }
            }
        }

        }catch (JSONException e){

        }
    }
}
