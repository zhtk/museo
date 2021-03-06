package org.beaconmuseum.beaconmuseum.beacons;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.ToggleButton;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.beaconmuseum.beaconmuseum.AppManager;
import org.beaconmuseum.beaconmuseum.GUIManager;
import org.beaconmuseum.beaconmuseum.R;

import java.util.ArrayList;

/**
 * Created by filip on 30.05.16.
 */
@Singleton
public class BeaconSwitchSettings {
    public static Activity _activity;
    @Inject
    private AppManager appManager;
    @Inject
    private NearestBeacon nearestBeacon;

    public BeaconSwitchSettings() {
        manualModeOn = false;
        lastNearestBeacon = new NoBeacon();
    }

    BeaconInfo lastNearestBeacon;
    boolean manualModeOn;

    public void toggleMode() {
        manualModeOn = !manualModeOn;
    }

    public void updateLastNearestBeacon() {
        lastNearestBeacon = nearestBeacon.getInfo();
    }

    public boolean nearestBeaconHasChanged() {
        return nearestBeacon.getInfo().id != lastNearestBeacon.id;
    }

    public void updateSlideMenu(final Activity t) {
        final HorizontalScrollView slideMenu = (HorizontalScrollView) _activity.findViewById(R.id.scrollView);
        final ArrayList<String> list = new ArrayList<>();
        final LinearLayout ll = new LinearLayout(t);

        for (BeaconInfo beacon : appManager.refreshGUI()) {
            Log.d("Update beacon", beacon.id);
            list.add(beacon.id);
        }
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll.setOrientation(LinearLayout.HORIZONTAL);
                if (slideMenu == null) {
                    return;
                }
                slideMenu.removeAllViews();
                slideMenu.addView(ll);
                for (String str : list) {
                    Log.d("str", str);
                    Button b = new Button(t);
                    b.setText(str);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayAnotherPainting(v);
                        }
                    });
                    ll.addView(b);
                }

                slideMenu.refreshDrawableState();
            }
        });
    }

    private void displayAnotherPainting(View v) {
        Button b = (Button) v;
        String beaconName = b.getText().toString();
        Log.d("button", beaconName);
        String link = GUIManager.getBeaconLink(beaconName);

        ToggleButton tb =  (ToggleButton)_activity.findViewById(R.id.toggle_button);
        tb.setChecked(true);

        WebView closestPainting = (WebView) _activity.findViewById(R.id.webView);
        closestPainting.loadUrl(link);

    }

    public void displayNearestPainting() {
        WebView closestPainting = (WebView) _activity.findViewById(R.id.webView);
        String link = GUIManager.getBeaconLink(nearestBeacon.getInfo().id);

        Log.d("change", "zmieniam obraz!" + link);
        if(link != null)
            closestPainting.loadUrl(link);
    }

    public boolean isManualModeOn() {
        return manualModeOn;
    }

    public BeaconInfo getLastNearestBeacon() {return lastNearestBeacon; }
}
