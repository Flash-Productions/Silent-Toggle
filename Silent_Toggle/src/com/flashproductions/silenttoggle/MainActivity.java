package com.flashproductions.silenttoggle;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import com.google.ads.*;


public class MainActivity extends Activity implements AdListener
{
    private AdView adView;

    private AudioManager mAudioManager;
    private int          mPhoneState;

    private static final int PHONE_STATE_SILENT  = 0;
    private static final int PHONE_STATE_VIBRATE = 1;
    private static final int PHONE_STATE_NORMAL  = 2;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate ( Bundle savedInstanceState )
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.main );

        // Create the adView
        adView = new AdView ( this, AdSize.BANNER, "a14f55281e01752" );


        // Lookup your LinearLayout assuming it’s been given
        // the attribute android:id="@+id/main"
        LinearLayout layout = ( LinearLayout ) findViewById ( R.id.main );

        //Set the Ad Listener
        adView.setAdListener ( this );


        // Add the adView to it
        layout.addView ( adView );

        //Set new request
        AdRequest request = new AdRequest ();


        // Initiate a generic request to load it with an ad
        adView.loadAd ( request );


        mAudioManager = ( AudioManager ) getSystemService ( AUDIO_SERVICE );

        checkRingerMode ();

        setButtonClickListener ();


    }


    private void setButtonClickListener ()
    {
        Button toggleButton = ( Button ) findViewById ( R.id.toggleButton );

        toggleButton.setOnClickListener ( new View.OnClickListener ()
        {

            public void onClick ( View v )
            {
                switch ( mPhoneState )
                {
                    case PHONE_STATE_SILENT:
                        mAudioManager.setRingerMode ( AudioManager.RINGER_MODE_NORMAL );
                        mAudioManager.setStreamVolume ( AudioManager.STREAM_RING,
                                                        mAudioManager.getStreamVolume(AudioManager.STREAM_RING),
                                                        AudioManager.FLAG_SHOW_UI );
                        mPhoneState = PHONE_STATE_NORMAL;
                        break;

                    case PHONE_STATE_NORMAL:
                        mAudioManager.setRingerMode ( AudioManager.RINGER_MODE_VIBRATE );
                        mPhoneState = PHONE_STATE_VIBRATE;
                        break;

                    case PHONE_STATE_VIBRATE:
                        mAudioManager.setRingerMode ( AudioManager.RINGER_MODE_SILENT );
                        mPhoneState = PHONE_STATE_SILENT;
                        break;

                }


                //Now toggle the UI again
                toggleUi ();


            }
        } );
    }

    /**
     * Checks to see what the state of the ringer mode is
     */

    private void checkRingerMode ()
    {
        int ringerMode = mAudioManager.getRingerMode ();
        if ( ringerMode == AudioManager.RINGER_MODE_SILENT )
        {
            mPhoneState = PHONE_STATE_SILENT;
        }
        else if ( ringerMode == AudioManager.RINGER_MODE_VIBRATE )
        {
            mPhoneState = PHONE_STATE_VIBRATE;
        }
        else
        {
            mPhoneState = PHONE_STATE_NORMAL;
        }


    }

    /**
     * Toggles the UI images from silent to vibrate to normal
     */
    private void toggleUi ()
    {
        ImageView imageView = ( ImageView ) findViewById ( R.id.phone_icon );
        Drawable newPhoneImage = getResources ().getDrawable ( R.drawable.ic_launcher );
        RemoteViews updateViews = new RemoteViews ( this.getPackageName (), R.layout.widget );


        switch ( mPhoneState )
        {
            case PHONE_STATE_SILENT:
                newPhoneImage = getResources ().getDrawable ( R.drawable.phone_silent );
                updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_silent );
                break;

            case PHONE_STATE_NORMAL:
                newPhoneImage = getResources ().getDrawable ( R.drawable.phone_normal );
                updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_normal );
                break;

            case PHONE_STATE_VIBRATE:
                newPhoneImage = getResources ().getDrawable ( R.drawable.phone_vibrate );
                updateViews.setImageViewResource ( R.id.phoneState, R.drawable.phone_state_vibrate );
                break;

        }

        imageView.setImageDrawable ( newPhoneImage );
    }

    @Override
    protected void onResume ()
    {
        super.onResume ();
        checkRingerMode ();
        toggleUi ();
    }


    @Override
    public void onReceiveAd ( Ad ad )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFailedToReceiveAd ( Ad ad, AdRequest.ErrorCode errorCode )
    {
        AdRequest request = new AdRequest ();
        adView.loadAd ( request );
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onPresentScreen ( Ad ad )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDismissScreen ( Ad ad )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLeaveApplication ( Ad ad )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
