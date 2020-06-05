package tw.edu.pu.csie.s1063724.trafficlight;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import static tw.edu.pu.csie.s1063724.trafficlight.GameSurfaceView.canvas;

public class GameActivity extends AppCompatActivity {

    GameSurfaceView GameSV;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //設定全螢幕顯示
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //設定螢幕為橫式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_game);

        GameSV = (GameSurfaceView) findViewById(R.id.GameSV);
        //設定初始測試之燈號秒數
        GameSV.SetLightSec(10,5,10);
        handler= new Handler();
        Runnable count_down = new Runnable() {
            @Override
            public void run() {
                if(GameSV.GreenLightSec > 0){

                    GameSV.GreenLightSec--;

                }else if(GameSV.YellowLightSec>0){
                    GameSV.YellowLightSec--;
                }else if (GameSV.RedLightSec > 0) {
                    GameSV.RedLightSec--;
                }else {     //紅綠燈reset
                    GameSV.SetLightSec(10, 5, 10);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(rendering);
        handler.postDelayed(count_down, 1000);
    }

    //利用手指觸控，控制小男孩走路
    public boolean onTouchEvent (MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            GameSV.BoyMoving = true;

        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            GameSV.BoyMoving =  false;
//            handler.removeCallbacks(runnable);  //銷毀執行緒
        }
        return true;
    }

    //處理小男孩走路
    Runnable rendering = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = GameSV.getHolder().lockCanvas();
            if(canvas != null){
                GameSV.drawSomething(canvas);
                GameSV.getHolder().unlockCanvasAndPost(canvas);
            }
            handler.postDelayed(this, 50);
        }
    };
}
