package com.moemen.android.thirtyfive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Second view which main purpose is to show the current and final score.
 */
public class ScoreFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";

    private Communicator comm;
    private TabSelector mTabSelector;

    private TextView scoreText;
    private Button restartButton;

    private TextView[] scores = new TextView[10];
    private int[] scoreID = {R.id.Low, R.id.scoreFour, R.id.scoreFive, R.id.scoreSix, R.id.scoreSeven,
            R.id.scoreEight, R.id.scoreNine, R.id.scoreTen, R.id.scoreEleven, R.id.scoreTwelve};

    /**
     * This method is called upon in the pagerAdapter to make it able for the fragments to communicate
     * with each other
     *
     * @param communicator Initialize the communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.comm = communicator;
    }

    /**
     * This method is called upon in the pagerAdapter to switch tab view after all 10 rounds
     * @param tabSelector Initialize the tabSelector
     */
    public void setTabSelector(TabSelector tabSelector) {
        mTabSelector = tabSelector;
    }

    /**
     * Initiate all the TextViews and an onClickListener for the restart button.
     * When pressed, tell dicesFragment to reset the scores and also remove the final score
     * TextView
     *
     * @param inflater inflates xml to give a view
     * @param parent Fragment’s parent ViewGroup
     * @param savedInstanceState Prior state bundle
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_score, parent, false);

        scores = new TextView[10];

        for (int i = 0; i < scores.length; i++){
            scores[i] = (TextView) v.findViewById(scoreID[i]);
        }
        scoreText = (TextView) v.findViewById(R.id.scoreTextView);
        restartButton = (Button) v.findViewById(R.id.restartButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartButton.setVisibility(View.INVISIBLE);
                scoreText.setText("");
                comm.needRestart();
                mTabSelector.onTabSwitch(0);


            }
        });

        return v;
    }

    /**
     * Called upon after every round from dicesFragment. Sets the score at appropriate TextView
     * and when all 10 scoring types have a score, counts the Total score. When this happens, the
     * formerly invisible restart button, gets visible.
     *
     * @param data new score that will update the score table
     */
    public void changeData(String[] data){
        int counter = 0;
        int totscore = 0;


        for(int i=0; i<data.length; i++){
            if (data[i].equals("-")){
                counter++;
            }
            try {
                totscore = totscore + Integer.parseInt(data[i]);
            } catch (NumberFormatException e){

            }

            scores[i].setText(data[i]);
        }
        if (counter==0){
            scoreText.setText("Your total score was: " + totscore);
            restartButton.setVisibility(View.VISIBLE);
        }

    }

}
