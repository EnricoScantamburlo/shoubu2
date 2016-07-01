package com.scantamburlo.shoubu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.scantamburlo.shoubu.model.ShoubuModel;
import com.scantamburlo.shoubu.presenter.ShoubuPresenter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ScoreBoardActivity extends AppCompatActivity {

    // TODO Change colors
    // TODO Select the time


    private ShoubuModel model;
    private ShoubuPresenter presenter;

    private final PropertyChangeListener modelListener = new PropertyChangeListener(){
        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            time.post(new Runnable() {
                @Override
                public void run() {
                    String propName = event.getPropertyName();
                    if(ShoubuModel.PROP_ELAPSED_TIME.equals(propName)){
                        updateTime();
                    } else if(ShoubuModel.PROP_STATUS.equals(propName)){
                        updateTime(); // When it is reset
                        updateStatus((ShoubuModel.Status)event.getNewValue());
                    } if(ShoubuModel.PROP_POINTS.equals(propName)){
                        updateScores();
                    }

                }
            });
        }
    };

    // Widgets
    private TextView time;
    private Button hajime;
    private Button reset;

    private Button leftIppon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_score_board);

        model = new ShoubuModel();
        presenter = new ShoubuPresenter(model);
        // Add Listener
        model.addProperyChangeListener(modelListener);


        this.time = (TextView) findViewById(R.id.time);

        this.hajime = (Button) findViewById(R.id.hajime);
        this.hajime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startResumeOrPause();
            }
        });
        this.reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reset();
            }
        });


        this.reset = (Button) findViewById(R.id.reset);
        this.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reset();
            }
        });

        this.leftIppon = (Button)findViewById(R.id.leftIppon);
        this.leftIppon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftIppon();
            }
        });

        updateTime();
        updateStatus(model.getStatus());
    }

    private void updateTime(){
        this.time.setText(presenter.getRemainingTime());
    }

    private void updateStatus(ShoubuModel.Status newStatus){
        switch (newStatus){
            case READY:
                this.hajime.setText("Hajime"); // TODO I18N
                break;
            case FINISHED:
                this.hajime.setText("Restart");// TODO I18N
                break;
            case RUNNING:
                this.hajime.setText("Pause");// TODO I18N
                break;
            case PAUSED:
                this.hajime.setText("Resume");// TODO I18N
                break;
        }
    }

    private void updateScores(){
        // TODO
    }
}
