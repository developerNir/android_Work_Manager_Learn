package com.example.workmanagerlearn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    Button button, stopButton;
    TextView textViewPoints;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.workButton);
        textViewPoints = findViewById(R.id.view_points);
        stopButton = findViewById(R.id.stop_button);


//        Create constraints ====================================

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build();


//         One Time work Request=====================================
//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();

//        PeriodicWorkRequest =======================================
        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
//               Set constraints ======================================
                .setConstraints(constraints)
                .build();

        button.setOnClickListener(view ->{
            WorkManager.getInstance(this).enqueue(periodicWorkRequest);
            // Show a notification

        });

        stopButton.setOnClickListener(view -> {
            WorkManager.getInstance(this).cancelWorkById(periodicWorkRequest.getId());
            // Show a notification

        });

        // how to status update in realtime text update
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String status = workInfo.getState().name();
                        textViewPoints.setText(status);
//                        if (workInfo!= null && workInfo.getState().isFinished()) {
//                            textViewPoints.setText(String.valueOf(workInfo.getOutputData().getInt("KEY_POINTS", 0)));
//                        }
                    }
                }
                );



    }



}