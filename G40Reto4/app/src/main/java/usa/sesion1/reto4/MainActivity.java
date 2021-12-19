package usa.sesion1.reto4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import usa.sesion1.reto4.vista.CatalogoActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imagenPresentacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }catch (Exception e){
            Log.e("TAG_ERROR", "Error EX: " + e.getMessage());
            e.printStackTrace();
        }


    }



}