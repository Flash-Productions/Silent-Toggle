package com.flashproductions.silenttoggle;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


public class AppWidget extends AppWidgetProvider
{

    OnBootReceiver bootReceiver = new OnBootReceiver ();


    @Override
    public void onReceive ( Context ctxt, Intent intent )
    {


        if ( intent.getAction () == null )
        {
            Intent serviceIntent = new Intent ();
            serviceIntent.setAction ( "com.flashproductions.silenttoggle.checkRingerMode" );
            ctxt.startService ( serviceIntent );

            ctxt.startService ( new Intent ( ctxt, ToggleService.class ) );
        }
        else
        {
            super.onReceive ( ctxt, intent );
        }

    }

    @Override
    public void onUpdate ( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds )
    {


        context.startService ( new Intent ( context, ToggleService.class ) );

    }

    public static class ToggleService extends IntentService
    {


        public ToggleService ()
        {
            super ( "AppWidget$ToggleService" );
        }


        @Override
        protected void onHandleIntent ( Intent intent )
        {
            ComponentName me = new ComponentName ( this, AppWidget.class );
            AppWidgetManager mgr = AppWidgetManager.getInstance ( this );
            mgr.updateAppWidget ( me, buildUpdate ( this ) );

        }


        Message msg = new Message ();

        View layout;


        private RemoteViews buildUpdate ( Context context )
        {


            LayoutInflater inflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            layout = inflater.inflate ( R.layout.toast_layout, null );

            ImageView image = ( ImageView ) layout.findViewById ( R.id.image );
            TextView text = ( TextView ) layout.findViewById ( R.id.text );


            RemoteViews updateViews = new RemoteViews ( context.getPackageName (), R.layout.widget );
            AudioManager audioManager = ( AudioManager ) context.getSystemService ( Activity.AUDIO_SERVICE );

            if ( audioManager.getRingerMode () == AudioManager.RINGER_MODE_SILENT )
            {

                updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_normal );


                audioManager.setRingerMode ( AudioManager.RINGER_MODE_NORMAL );
                audioManager.setStreamVolume ( AudioManager.STREAM_RING,
                                               audioManager.getStreamVolume(AudioManager.STREAM_RING),
                                               AudioManager.FLAG_SHOW_UI );


                image.setImageResource ( R.drawable.phone_state_normal );
                text.setText ( R.string.Normal );


                handler.sendMessage ( msg );


            }
            else if ( audioManager.getRingerMode () == AudioManager.RINGER_MODE_VIBRATE )
            {
                updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_silent );


                audioManager.setRingerMode ( AudioManager.RINGER_MODE_SILENT );

                image.setImageResource ( R.drawable.phone_state_silent );
                text.setText ( R.string.Silent );


                handler.sendMessage ( msg );


            }
            else
            {
                updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_vibrate );


                audioManager.setRingerMode ( AudioManager.RINGER_MODE_VIBRATE );

                image.setImageResource ( R.drawable.phone_state_vibrate );
                text.setText ( R.string.Vibrate );


                handler.sendMessage ( msg );


            }

            Intent i = new Intent ( this, AppWidget.class );

            PendingIntent pi = PendingIntent.getBroadcast ( context, 0, i, 0 );

            updateViews.setOnClickPendingIntent ( R.id.phoneState, pi );

            return updateViews;
        }

        private Handler handler = new Handler ()
        {
            @Override
            public void handleMessage ( Message msg )
            {

                Toast toast = new Toast ( AppWidget.ToggleService.this );
                toast.setGravity ( Gravity.CENTER_VERTICAL, 0, 0 );
                toast.setDuration ( Toast.LENGTH_SHORT );
                toast.setView ( layout );
                toast.show ();


            }
        };

    }


}