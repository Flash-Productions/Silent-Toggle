package com.flashproductions.silenttoggle;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.RemoteViews;


public class checkRingerMode extends Service
{
    public IBinder onBind ( Intent intent )
    {
        return null;
    }

    @Override
    public void onStart ( Intent intent, int startId )
    {
        super.onStart ( intent, startId );

        BroadcastReceiver receiver = new BroadcastReceiver ()
        {
            @Override
            public void onReceive ( Context context, Intent intent )
            {
                RemoteViews updateViews = new RemoteViews ( context.getPackageName (), R.layout.widget );
                AudioManager audioManager = ( AudioManager ) context.getSystemService ( Activity.AUDIO_SERVICE );

                if ( audioManager.getRingerMode () == AudioManager.RINGER_MODE_VIBRATE )
                {

                    updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_vibrate );


                }

            }
        };
        IntentFilter filter = new IntentFilter ( AudioManager.RINGER_MODE_CHANGED_ACTION );
        registerReceiver ( receiver, filter );


    }


}
