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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GestureDetectorCompat;


public class AdOverView
        extends AppCompatImageView
        implements GestureDetector.OnGestureListener {

    String TAG="KENZ adOverView";
    private long dTime;
    private GestureDetectorCompat mDetector;

    public AdOverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetectorCompat(context,this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                return mDetector.onTouchEvent(event);
            }});
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown: ");
        return System.currentTimeMillis() - dTime >= 200;
    }
    @Override
    public void onShowPress(MotionEvent e) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(System.currentTimeMillis()-dTime>200){
            Log.d(TAG, "onSingleTapUp: > 200ms since last tap");
            Toast.makeText(getContext(),
                    "DoubleTap for Ads",Toast.LENGTH_SHORT)
                    .show();
            dTime=System.currentTimeMillis();}
        return true;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {return true;}
}
