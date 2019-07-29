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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdViewFragment extends Fragment {
    private AdRequest adRequest;
    private String TAG = "KENZ AdViewFrag";


    static AdViewFragment newInstance(AdRequest adRequest){
        AdViewFragment af = new AdViewFragment();
        af.adRequest=adRequest;
        return af;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container!=null)container.removeAllViews();
        Log.d(TAG, "Create view. ");
        return inflater.inflate(R.layout.adview,container,false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        final AdView adView = (AdView) ((ConstraintLayout)view).getChildAt(0);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLeftApplication() {
                Log.d(TAG, "Banner Ad Left Application");
            }
            @Override
            public void onAdLoaded() {
                Log.d(TAG,"Banner Ad Loaded");
            }
            @Override
            public void onAdClosed() {
                Log.d(TAG,"Banner Ad Closed");
            }
            @Override
            public void onAdOpened() {
                Log.d(TAG,"Banner Ad Opened");
            }
        });
        adView.loadAd(adRequest);
        Log.d(TAG, "View Created and AdRequest loaded ");
        super.onViewCreated(view, savedInstanceState);
    }

}
