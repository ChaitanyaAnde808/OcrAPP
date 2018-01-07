package com.example.lenovo.ocrapp;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SurfaceView cameraview;
    TextView textView;
    CameraSource cameraSource;
    ScrollView scrollView;
    Button button,button2,button3,button4;
    String save="No Text";
    final int RequestCameraPermissionId = 1001;
    private static final String FILENAME = "myFile.txt";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionId: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraview = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);
        button=(Button)findViewById(R.id.button);
        button3=(Button)findViewById(R.id.button3);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent sendIntent = new Intent();
               sendIntent.setAction(Intent.ACTION_SEND);
               sendIntent.putExtra(Intent.EXTRA_TEXT,save);
               sendIntent.setType("text/plain");
               startActivity(Intent.createChooser(sendIntent,"Share using"));
           }
                  });
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), Main2Activity.class);
                save=textView.getText().toString();
                in.putExtra("txt",save);
                in.setType("text/plain");
                startActivity(in);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.google.com/#q=";
                String query = textView.getText().toString().trim();
                String final_url = url + query;
                Uri uri = Uri.parse(final_url.substring(0,32));
                Intent ins=new Intent(Intent.ACTION_VIEW, uri);
                startActivity(ins);
            }
        });
        button4=(Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents=new Intent(getApplicationContext(),Main3Activity.class);
                startActivity(intents);
            }
        });
        textView.setMovementMethod(new ScrollingMovementMethod());
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {

        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraview.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionId);
                            return;
                        }
                        cameraSource.start(cameraview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();


                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections){
                   final SparseArray<TextBlock> items=detections.getDetectedItems();
                    if(items.size()!=0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
            StringBuilder stringBuilder=new StringBuilder();
                                for (int i=0;i<items.size();++i)
                                {
                                    TextBlock item=items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }

                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }

                }
            });
        }
    }
}