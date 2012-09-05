package com.flashproductions.silenttoggle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

/**
 * Created by Flash Productions.
 * Date: 3/24/12
 * Time: 1:59 PM
 */
public class OnBootReceiver extends BroadcastReceiver
{

    public void onReceive ( Context context, Intent intent )
    {
        RemoteViews updateViews = new RemoteViews ( context.getPackageName (), R.layout.widget );
        AudioManager audioManager = ( AudioManager ) context.getSystemService ( Activity.AUDIO_SERVICE );

        if ( audioManager.getRingerMode () == AudioManager.RINGER_MODE_VIBRATE )
        {

            updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_vibrate );


        }

        // Intent serviceIntent = new Intent();
        //serviceIntent.setAction("com.flashproductions.silenttoggle.checkRingerMode");
        //context.startService(serviceIntent);

    }
}
