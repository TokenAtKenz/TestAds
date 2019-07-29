/*
 * Copyright (c) 2019 by Ken Burrows<KenzWares@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package kenz.testads;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {
    private String TAG = "KENZ MainActivity";
    private AdRequest myAdRequest;
    private RewardedVideoAd myRewardedVideoAd;
    private InterstitialAd myInterstitialAd;
    private AdViewFragment adViewFragment;
    private boolean isBannerDisplayed = false;

    private void constructAdRequest()   {
        if(BuildConfig.DEBUG)
            myAdRequest = new AdRequest.Builder()
                    .addTestDevice(getApplicationContext().getString(R.string.SM_T280_TestDevice))
                    .addTestDevice(getApplicationContext().getString(R.string.SGH_1547C_TestDevice))
                    .setRequestAgent(getApplicationContext().getString(R.string.app_name))
                    .build();
        else
            myAdRequest = new AdRequest.Builder()
                    .addTestDevice(getApplicationContext().getString(R.string.SM_T280_TestDevice))
                    .addTestDevice(getApplicationContext().getString(R.string.SGH_1547C_TestDevice))
                    .setRequestAgent(getApplicationContext().getString(R.string.app_name))
                    .build();
    }
    private void makeAllAdMobAds()      {
        myInterstitialAd = new InterstitialAd(getBaseContext());
        myInterstitialAd.setAdUnitId(getResources().getString(R.string.AdmobTestInterstitial));
        myInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.d(TAG, "Interstatial Ad Closed");
                myInterstitialAd.loadAd(myAdRequest);
            }

            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Interstatial Ad Loaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.d(TAG, "Interstatial Ad FAILED Loaded ");
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG, "Next Interstatial Ad Opened");
            }
        });
        myInterstitialAd.loadAd(myAdRequest);

        myRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getBaseContext());myRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.d(TAG, "Reward Ad Loaded");
            }
            @Override
            public void onRewardedVideoAdOpened() {
                Log.d(TAG, "Reward Video Ad Opened");
            }
            @Override
            public void onRewardedVideoStarted() {
                Log.d(TAG, "Reward Video Ad Started");
            }
            @Override
            public void onRewardedVideoAdClosed() {
                Log.d(TAG, "Reward Ad Closed");
                myRewardedVideoAd.loadAd(getResources().getString(R.string.AdmobTestReward),
                        myAdRequest);
            }
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Log.d(TAG,"Rewarded! currency type: [" + rewardItem.getType() + "]  amount [" +rewardItem.getAmount()+"]");
            }
            @Override
            public void onRewardedVideoAdLeftApplication() {
                Log.d(TAG,"Reward Video Ad Left Application");
            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.d(TAG,"Reward Video Ad Failed to Load");
            }
            @Override
            public void onRewardedVideoCompleted() {
                Log.d(TAG,"Reward Video Completed");
            }
        });
        myRewardedVideoAd.loadAd(getResources().getString(R.string.AdmobTestReward),myAdRequest);

    }
    private void displayAdViewFragment(){
        adViewFragment = AdViewFragment.newInstance(myAdRequest);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.adFrame,adViewFragment,"adViewFragment")
                .addToBackStack("adViewFragment")
                .commit();
        isBannerDisplayed = true;
    }
    private void closeAdViewFragment()  {
        if(adViewFragment!=null)
            getSupportFragmentManager().beginTransaction()
                    .remove(adViewFragment).commit();
        isBannerDisplayed = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this,getString(R.string.AdmobTestAppID));
        setContentView(R.layout.activity_main);
        constructAdRequest();
        makeAllAdMobAds();
        if (savedInstanceState != null) {
            isBannerDisplayed = savedInstanceState.getBoolean("bannerState");
        }
        //manual reset  ads orientation correct
        if(isBannerDisplayed)displayAdViewFragment();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exit(0);}
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "OK then... back to the game.");    }
                })
                .setMessage("\n\tThis will close the program.\n\n\t\tAre you Sure ?")
                .setTitle("Ad Tester")
                .show();
    }  //use back button for exit
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        if (savedInstanceState != null)
            isBannerDisplayed = savedInstanceState.getBoolean("bannerState");
        //manual reset these to get ads orientation correct
        if(isBannerDisplayed)displayAdViewFragment();
        makeAllAdMobAds();

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)       {
        savedInstanceState.putBoolean("bannerState", isBannerDisplayed);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)      {
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
        //manual reset these to get ads orientation correct
        if(isBannerDisplayed)displayAdViewFragment();
        makeAllAdMobAds();
    }
    public void bannerToggle(View view) {
        if(isBannerDisplayed)closeAdViewFragment();else displayAdViewFragment();
    } //toggle the bottom banner ad on/off
    public void doAds(View view)        {
        new AlertDialog.Builder(this)
                .setTitle("Interstitial OR Reward ?")
                .setMessage("Both are full page ads, but the \"Reward Ad\" will send a reward if you watch the whole thing. The \"Interstitial Ad\" does not care if you watch it or not.")
                .setPositiveButton("Reward Ad", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (myRewardedVideoAd != null&& myRewardedVideoAd.isLoaded()) myRewardedVideoAd.show();
                        else Log.d(TAG,"Reward Ads not Ready");
                    }
                })
                .setNegativeButton("Interstitial Ad", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (myInterstitialAd != null&& myInterstitialAd.isLoaded()) myInterstitialAd.show();
                        else Log.d(TAG,"Interstitial Ads not Ready");
                    }
                })
                .setIcon(android.R.drawable.ic_media_play)
                .show();

    } //select and then watch a full page ad

}
