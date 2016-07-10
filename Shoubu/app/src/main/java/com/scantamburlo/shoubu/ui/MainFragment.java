package com.scantamburlo.shoubu.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scantamburlo.shoubu.R;
import com.scantamburlo.shoubu.model.ShoubuModel;
import com.scantamburlo.shoubu.presenter.ShoubuPresenter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * Created by scanti.rulla at gmail.com on 10/07/16.
 */
public class MainFragment extends Fragment {

    // TODO Change colors
    // TODO Select the time


    private ShoubuModel model;
    private ShoubuPresenter presenter;

    // TODO move in Options
    private int leftColor;
    private int rightColor;
    private int greyColor;
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
    // cat 1
    private Button leftC1W;
    private Button leftC1K;
    private Button leftC1H;
    private Button leftC1HC;
    // cat 2
    private Button leftC2W;
    private Button leftC2K;
    private Button leftC2H;
    private Button leftC2HC;
    //Right
    private Button rightIppon;
    private Button rightWazaAri;
    private Button rightYuko;
    private Button rightMistake;
    private TextView rightScore;
    // cat 1
    private Button rightC1W;
    private Button rightC1K;
    private Button rightC1H;
    private Button rightC1HC;
    // cat 2
    private Button rightC2W;
    private Button rightC2K;
    private Button rightC2H;
    private Button rightC2HC;


    private TextView result;


    private final PropertyChangeListener modelListener = new PropertyChangeListener() {
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

                            updateScores();

                            updateStatus((ShoubuModel.Status) event.getNewValue());
                            updatePenalties();
                            break;
                        case ShoubuModel.PROP_POINTS:
                            updateScores();
                            break;
                        case ShoubuModel.PROP_PENALITY:
                            updatePenalties();
                            break;
                    }
                }
            });
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        model = new ShoubuModel();
        presenter = new ShoubuPresenter(model);
        // Add Listener
        model.addProperyChangeListener(modelListener);


        this.time = (TextView) view.findViewById(R.id.time);

        this.hajime = (Button) view.findViewById(R.id.hajime);
        this.hajime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startResumeOrPause();
            }
        });
        this.reset = (Button) view.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reset();
            }
        });


        this.reset = (Button) view.findViewById(R.id.reset);
        this.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reset();
            }
        });

        this.leftIppon = (Button) view.findViewById(R.id.leftIppon);
        this.leftIppon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftIppon();
            }
        });


        this.rightIppon = (Button) view.findViewById(R.id.rightIppon);
        this.rightIppon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightIppon();
            }
        });

        this.leftWazaAri = (Button) view.findViewById(R.id.leftWazaAri);
        this.leftWazaAri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftWazaAri();
            }
        });


        this.rightWazaAri = (Button) view.findViewById(R.id.rightWazaAri);
        this.rightWazaAri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightWazaAri();
            }
        });

        this.leftYuko = (Button) view.findViewById(R.id.leftYuko);
        this.leftYuko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftYuko();
            }
        });


        this.rightYuko = (Button) view.findViewById(R.id.rightYuko);
        this.rightYuko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightYuko();
            }
        });

        this.leftMistake = (Button) view.findViewById(R.id.leftMistake);
        this.leftMistake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.leftMistake();
            }
        });


        this.rightMistake = (Button) view.findViewById(R.id.rightMistake);
        this.rightMistake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rightMistake();
            }
        });


        this.rightScore = (TextView) view.findViewById(R.id.rightScore);
        this.leftScore = (TextView) view.findViewById(R.id.leftScore);


        this.leftC1W = initPenalty(view, R.id.leftC1C, model.getLeftKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.CHUKOKU);
        this.leftC1K = initPenalty(view, R.id.leftC1K, model.getLeftKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.KEIKOKU);
        this.leftC1HC = initPenalty(view, R.id.leftC1HC, model.getLeftKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.HANSOKU_CHUI);
        this.leftC1H = initPenalty(view, R.id.leftC1H, model.getLeftKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.HANSOKU);

        this.leftC2W = initPenalty(view, R.id.leftC2C, model.getLeftKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.CHUKOKU);
        this.leftC2K = initPenalty(view, R.id.leftC2K, model.getLeftKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.KEIKOKU);
        this.leftC2HC = initPenalty(view, R.id.leftC2HC, model.getLeftKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.HANSOKU_CHUI);
        this.leftC2H = initPenalty(view, R.id.leftC2H, model.getLeftKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.HANSOKU);

        this.rightC1W = initPenalty(view, R.id.rightC1C, model.getRightKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.CHUKOKU);
        this.rightC1K = initPenalty(view, R.id.rightC1K, model.getRightKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.KEIKOKU);
        this.rightC1HC = initPenalty(view, R.id.rightC1HC, model.getRightKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.HANSOKU_CHUI);
        this.rightC1H = initPenalty(view, R.id.rightC1H, model.getRightKarateka(), ShoubuModel.Category.ONE, ShoubuModel.Penalty.HANSOKU);

        this.rightC2W = initPenalty(view, R.id.rightC2C, model.getRightKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.CHUKOKU);
        this.rightC2K = initPenalty(view, R.id.rightC2K, model.getRightKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.KEIKOKU);
        this.rightC2HC = initPenalty(view, R.id.rightC2HC, model.getRightKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.HANSOKU_CHUI);
        this.rightC2H = initPenalty(view, R.id.rightC2H, model.getRightKarateka(), ShoubuModel.Category.TWO, ShoubuModel.Penalty.HANSOKU);


        this.result = (TextView) view.findViewById(R.id.result);

        // Default "@android:color/holo_red_dark"
        this.leftColor = model.getLeftColor();
        this.rightColor = model.getRightColor();
        this.greyColor = Color.GRAY; // TODO USE R.colors ....


        updateColors(); // also calls updatePenalties
        updateScores();
        updateTime();
        updateStatus(model.getStatus());

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void updateColors() {
        int color = leftColor;
        setColor(this.leftIppon, color);

        setColor(leftWazaAri, color);
        setColor(leftYuko, color);
        setColor(leftMistake, color);

        color = rightColor;
        setColor(rightIppon, color);
        setColor(rightWazaAri, color);
        setColor(rightYuko, color);
        setColor(rightMistake, color);

        updatePenalties();
        updateWidths();
    }

    private void updateWidths() {
        int maxWidth = leftIppon.getWidth();
        if (leftWazaAri.getWidth() > maxWidth) {
            maxWidth = leftWazaAri.getWidth();
        }

        if (leftYuko.getWidth() > maxWidth) {
            maxWidth = leftYuko.getWidth();
        }

        final int max = maxWidth;

        leftIppon.post(new Runnable() {

            public void run() {
                leftIppon.setWidth(max);
                leftWazaAri.setWidth(max);
                leftYuko.setWidth(max);
            }

        });
    }


    private void setColor(Button btn, int color) {
        Drawable tmpDrawable = DrawableCompat.wrap(btn.getBackground());
        DrawableCompat.setTint(tmpDrawable, color);
        btn.setBackground(tmpDrawable);
    }

    private Button initPenalty(View view, int id, final ShoubuModel.Karateka karateka, final ShoubuModel.Category cat, final ShoubuModel.Penalty pen) {
        Button btn = (Button) view.findViewById(id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setPenalty(karateka, cat, pen);
            }
        });

        return btn;
    }

    private void updateTime() {
        this.time.setText(presenter.getRemainingTime());
    }

    private void updateStatus(ShoubuModel.Status newStatus) {
        switch (newStatus) {
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

        updateResult(newStatus);
    }

    private void updateScores() {
        leftScore.setText(String.valueOf(model.getLeftKarateka().getPoints()));
        rightScore.setText(String.valueOf(model.getRightKarateka().getPoints()));
    }

    private void updatePenalties() {
        // RIGHT
        Set<ShoubuModel.Penalty> rightCat1 = model.getRightKarateka().getCat1Penalties();

        updatePenaltyButton(rightC1W, rightCat1, ShoubuModel.Penalty.CHUKOKU, rightColor);
        updatePenaltyButton(rightC1K, rightCat1, ShoubuModel.Penalty.KEIKOKU, rightColor);
        updatePenaltyButton(rightC1HC, rightCat1, ShoubuModel.Penalty.HANSOKU_CHUI, rightColor);
        updatePenaltyButton(rightC1H, rightCat1, ShoubuModel.Penalty.HANSOKU, rightColor);

        Set<ShoubuModel.Penalty> rightCat2 = model.getRightKarateka().getCat2Penalties();
        updatePenaltyButton(rightC2W, rightCat2, ShoubuModel.Penalty.CHUKOKU, rightColor);
        updatePenaltyButton(rightC2K, rightCat2, ShoubuModel.Penalty.KEIKOKU, rightColor);
        updatePenaltyButton(rightC2HC, rightCat2, ShoubuModel.Penalty.HANSOKU_CHUI, rightColor);
        updatePenaltyButton(rightC2H, rightCat2, ShoubuModel.Penalty.HANSOKU, rightColor);

        // LEFT
        Set<ShoubuModel.Penalty> leftCat1 = model.getLeftKarateka().getCat1Penalties();

        updatePenaltyButton(leftC1W, leftCat1, ShoubuModel.Penalty.CHUKOKU, leftColor);
        updatePenaltyButton(leftC1K, leftCat1, ShoubuModel.Penalty.KEIKOKU, leftColor);
        updatePenaltyButton(leftC1HC, leftCat1, ShoubuModel.Penalty.HANSOKU_CHUI, leftColor);
        updatePenaltyButton(leftC1H, leftCat1, ShoubuModel.Penalty.HANSOKU, leftColor);

        Set<ShoubuModel.Penalty> leftCat2 = model.getLeftKarateka().getCat2Penalties();
        updatePenaltyButton(leftC2W, leftCat2, ShoubuModel.Penalty.CHUKOKU, leftColor);
        updatePenaltyButton(leftC2K, leftCat2, ShoubuModel.Penalty.KEIKOKU, leftColor);
        updatePenaltyButton(leftC2HC, leftCat2, ShoubuModel.Penalty.HANSOKU_CHUI, leftColor);
        updatePenaltyButton(leftC2H, leftCat2, ShoubuModel.Penalty.HANSOKU, leftColor);
    }

    private void updatePenaltyButton(Button btn, Set<ShoubuModel.Penalty> cat, ShoubuModel.Penalty pen, int color) {
        if (cat.contains(pen)) {
            setColor(btn, color);
            btn.setBackgroundColor(color);
        } else {
            setColor(btn, greyColor);
        }
    }

    private void updateResult(ShoubuModel.Status status) {
        if (status == ShoubuModel.Status.FINISHED) {
            result.setVisibility(View.VISIBLE);

            ShoubuModel.Karateka winner = model.getWinner();
            String msg;
            if (winner != null) {
                msg = winner.getName() + " no kachi"; // TODO I18N
            } else {
                msg = "Hikiwake"; // TODO I18N
            }
            result.setText(msg);

        } else {
            result.setVisibility(View.INVISIBLE);
        }

    }
}
