package com.falahrafi.latihan.unisbankstopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import java.time.Clock;

public class MainActivity extends AppCompatActivity {

    private Button btnMulai, btnJeda, btnReset;
    private Chronometer cmtrWaktu;
    private ImageView ivClockHandShadow;
    private ObjectAnimator clockHandRounding;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMulai = findViewById(R.id.btn_mulai);
        btnJeda = findViewById(R.id.btn_jeda);
        btnReset = findViewById(R.id.btn_reset);
        ivClockHandShadow = findViewById(R.id.iv_clock_hand_shadow);
        cmtrWaktu = findViewById(R.id.cmtr_waktu);

        // HIDE tombol "Jeda" & "Atur Ulang"
        btnJeda.setAlpha(0);
        btnReset.setAlpha(0);

        // BUTTON MULAI
        btnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Membuat ObjectAnimator
                clockHandRounding = ObjectAnimator.ofFloat(ivClockHandShadow, "rotation", 0, 360);
                clockHandRounding.setInterpolator(new LinearInterpolator()); // Menghilangkan delay saat animasi mulai
                clockHandRounding.setDuration(120000);
                clockHandRounding.setRepeatCount(ObjectAnimator.INFINITE);
                clockHandRounding.setRepeatMode(ObjectAnimator.RESTART);

                // Menjalankan animasi
                clockHandRounding.start();

                // HIDE tombol "Mulai"
                btnMulai.animate().alpha(0).setDuration(300).start();

                // SHOW tombol "Jeda" & "Atur Ulang"
                btnJeda.animate().alpha(1).translationY(-100).setDuration(800).start();
                btnReset.animate().alpha(1).translationY(-100).setDuration(800).start();

                setToJeda();
                mulaiChronometer();
            }
        });

        // BUTTON JEDA
        btnJeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnJeda.getText().toString().equals("Jeda")) {
                    clockHandRounding.pause(); // Jeda animasi
                    jedaChronometer();
                    setToLanjutkan();

                } else if (btnJeda.getText().toString().equals("Lanjutkan")) {
                    clockHandRounding.resume(); // Lanjutkan animasi
                    lanjutkanChronometer();
                    setToJeda();
                }
            }
        });

        // BUTTON RESET
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // SHOW tombol "Mulai"
                btnMulai.animate().alpha(1).setDuration(300).start();

                // HIDE tombol "Jeda" & "Atur Ulang"
                btnJeda.animate().alpha(0).translationY(100).setDuration(300).start();
                btnReset.animate().alpha(0).translationY(100).setDuration(300).start();

                clockHandRounding.end(); // Hentikan animasi
                resetChronometer();

            }
        });

    }



    public void mulaiChronometer(){
        // Reset waktu chronometer pada background
        cmtrWaktu.setBase(SystemClock.elapsedRealtime());
        // Mulai waktu chronometer pada tampilan
        cmtrWaktu.start();

        running = true;
    }

    public void jedaChronometer(){

        if(running){
            // Hentikan waktu chronometer pada tampilan
            cmtrWaktu.stop();
            // Menyimpan waktu terakhir pada chronometer
            pauseOffset = SystemClock.elapsedRealtime() - cmtrWaktu.getBase(); // Waktu realtime - Waktu terakhir chronometer

            running = false;
        }

    }

    public void lanjutkanChronometer(){

        if(!running){
            // Set waktu chronometer dengan waktu yang tersimpan
            cmtrWaktu.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            // Mulai waktu chronometer pada tampilan
            cmtrWaktu.start();

            running = true;
        }

    }

    public void resetChronometer(){
        // Hentikan waktu chronometer pada tampilan
        cmtrWaktu.stop();
        // Reset waktu chronometer pada background
        cmtrWaktu.setBase(SystemClock.elapsedRealtime());
    }



    public void setToJeda(){
        btnJeda = findViewById(R.id.btn_jeda);
        btnJeda.setText("Jeda");
        btnJeda.setTextColor(getResources().getColor(R.color.unisbank_black));
        btnJeda.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.custom_button_yellow));
    }

    public void setToLanjutkan(){
        btnJeda = findViewById(R.id.btn_jeda);
        btnJeda.setText("Lanjutkan");
        btnJeda.setTextColor(getResources().getColor(R.color.white));
        btnJeda.setBackground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.custom_button_blue));
    }

}