package cn.jzvd;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.s_tools.R;

public class JzvdLockScreen extends JzvdStd {
    float starX, startY;
    private boolean isLockScreen, isRotate;
    private ImageView lockIv, rotate, ratio;
    private boolean ratioClicked;

    public JzvdLockScreen(Context context) {
        super(context);
    }

    public JzvdLockScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //这里应该还没有判断完  目前还没有测试出什么问题  这里是拦截父亲得一些事件比如滑动快进 改变亮度
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                starX=event.getX();
                startY=event.getY();
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    //&& Math.abs(Math.abs(event.getX() - starX)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()  && Math.abs(Math.abs(event.getY() - startY)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()
                    if (event.getX() == starX || event.getY() == startY) {
                        startDismissControlViewTimer();
                        onClickUiToggle();
                        bottomProgressBar.setVisibility(VISIBLE);
                    }
                    return true;
                }
                break;
        }
        return super.onTouch(v, event);
    }


    @Override
    public void init(Context context) {
        super.init(context);
        lockIv=findViewById(R.id.lock);
        lockIv.setOnClickListener(this);
        rotate=findViewById(R.id.rotate);
        rotate.setOnClickListener(this);
        ratio=findViewById(R.id.ratio);
        ratio.setOnClickListener(this);
    }


    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        if (screen == SCREEN_FULLSCREEN) {
            ratio.setVisibility(GONE);
            if (!isLockScreen) {
                if (bottomContainer.getVisibility() == View.VISIBLE) {
                    lockIv.setVisibility(View.VISIBLE);
                    rotate.setVisibility(VISIBLE);
                } else {
                    rotate.setVisibility(GONE);
                    lockIv.setVisibility(View.GONE);
                }
            } else {
                if ((int) lockIv.getTag() == 1) {
                    bottomProgressBar.setVisibility(GONE);
                    if (lockIv.getVisibility() == View.GONE) {
                        lockIv.setVisibility(View.VISIBLE);
                    } else {
                        lockIv.setVisibility(View.GONE);
                    }

                }
            }

        }else if (screen==SCREEN_NORMAL){
            Log.e("//", "onClickUiToggle: " );
            if (bottomContainer.getVisibility()==VISIBLE){
                ratio.setVisibility(VISIBLE);
            }else {
                ratio.setVisibility(GONE);
            }
        }
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        if (isLockScreen) {
            rotate.setVisibility(GONE);
            bottomContainer.setVisibility(GONE);
            topContainer.setVisibility(GONE);
            startButton.setVisibility(GONE);
            ratio.setVisibility(GONE);
        }
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        lockIv.setImageResource(R.drawable.unlock_btn);
        lockIv.setVisibility(View.VISIBLE);
        rotate.setVisibility(VISIBLE);
        ratio.setVisibility(GONE);
    }

    @Override
    public void dissmissControlView() {
        super.dissmissControlView();
        post(() -> {
            if (screen == SCREEN_FULLSCREEN) {
                rotate.setVisibility(GONE);
                lockIv.setVisibility(View.GONE);
                ratio.setVisibility(GONE);
//                bottomProgressBar.setVisibility(View.GONE);
            }else if (screen==SCREEN_NORMAL){
                ratio.setVisibility(GONE);
            }
        });
    }


    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
//        if (JZUtils.getWindow(jzvdContext).getDecorView().getSystemUiVisibility()==VISIBLE && screen==SCREEN_FULLSCREEN){
//            JZUtils.hideSystemUI(jzvdContext);
//        }
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            if (isLockScreen) {
                rotate.setVisibility(GONE);
                topContainer.setVisibility(GONE);
                bottomContainer.setVisibility(GONE);
                startButton.setVisibility(GONE);
                ratio.setVisibility(GONE);
            } else {
                rotate.setVisibility(VISIBLE);
                topContainer.setVisibility(VISIBLE);
                bottomContainer.setVisibility(VISIBLE);
                startButton.setVisibility(VISIBLE);
                ratio.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            lockIv.setVisibility(View.GONE);
            rotate.setVisibility(GONE);
        }
    }



    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        lockIv.setVisibility(View.GONE);
        rotate.setVisibility(GONE);
        ratio.setVisibility(GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lock:
                if (screen == SCREEN_FULLSCREEN) {
                    lockIv.setTag(1);
                    if (!isLockScreen) {
                        isLockScreen=true;
//                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        lockIv.setImageResource(R.drawable.lock_icon);
                        dissmissControlView();
                        rotate.setVisibility(GONE);
                        ratio.setVisibility(GONE);
                    } else {
//                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        isLockScreen=false;
                        lockIv.setImageResource(R.drawable.unlock_btn);
                        bottomContainer.setVisibility(VISIBLE);
                        bottomProgressBar.setVisibility(GONE);
                        topContainer.setVisibility(VISIBLE);
                        startButton.setVisibility(VISIBLE);
                        rotate.setVisibility(VISIBLE);
                        ratio.setVisibility(GONE);
                    }
                }
                break;
            case R.id.rotate:
                if (isRotate) {
                    isRotate=false;
                    JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    isRotate=true;
                    JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case R.id.ratio:
                if (ratioClicked) {
                    Jzvd.CURRENT_JZVD.widthRatio=16;
                    Jzvd.CURRENT_JZVD.heightRatio=9;
                    ratioClicked=false;
                } else {
                    Jzvd.CURRENT_JZVD.widthRatio=1;
                    Jzvd.CURRENT_JZVD.heightRatio=1;
                    ratioClicked=true;
                }
                break;
        }

    }
}
