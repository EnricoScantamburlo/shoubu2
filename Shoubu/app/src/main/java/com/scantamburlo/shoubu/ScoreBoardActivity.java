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
            final String propName = event.getPropertyName();
            time.post(new Runnable() {
                @Override
                public void run() {
                    switch (propName) {
                        case ShoubuModel.PROP_ELAPSED_TIME:
                            updateTime();
                            break;
                        case ShoubuModel.PROP_STATUS:
                            updateScores(); // When it is reset

                            updateTime(); // When it is reset

                            updateStatus((ShoubuModel.Status) event.getNewValue());
                            break;
                        case ShoubuModel.PROP_POINTS:
                            updateScores();
                            break;
                    }
                }
            });
        }
    };

    // Widgets
    private TextView time;
    private Button hajime;
    private Button reset;

    // Left
    private Button leftIppon;
    private Button leftWazaAri;
    private Button leftYuko;
    private Button leftMistake;
    private TextView leftScore;

    //Right
    private Button rightIppon;
    private Button rightWazaAri;
    private Button rightYuko;
    private Button rightMistake;
    private TextView rightScore;

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


        this.rightIppon = (Button)findViewById(R.id.rightIppon);
        this.rightIppon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightIppon();
            }
        });

        this.leftWazaAri = (Button)findViewById(R.id.leftWazaAri);
        this.leftWazaAri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftWazaAri();
            }
        });


        this.rightWazaAri = (Button)findViewById(R.id.rightWazaAri);
        this.rightWazaAri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightWazaAri();
            }
        });

        this.leftYuko = (Button)findViewById(R.id.leftYuko);
        this.leftYuko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftYuko();
            }
        });


        this.rightYuko = (Button)findViewById(R.id.rightYuko);
        this.rightYuko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightYuko();
            }
        });

        this.leftMistake = (Button)findViewById(R.id.leftMistake);
        this.leftMistake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftMistake();
            }
        });


        this.rightMistake = (Button)findViewById(R.id.rightMistake);
        this.rightMistake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightMistake();
            }
        });


        this.rightScore = (TextView)findViewById(R.id.rightScore);
        this.leftScore = (TextView)findViewById(R.id.leftScore);

        updateScores();
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
        leftScore.setText(String.valueOf(model.getLeftKarateka().getPoints()));
        rightScore.setText(String.valueOf(model.getRightKarateka().getPoints()));
    }
}
