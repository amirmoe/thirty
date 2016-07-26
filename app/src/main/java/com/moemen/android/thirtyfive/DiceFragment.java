package com.moemen.android.thirtyfive;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * First view which main purpose is to show the game board
 */
public class DiceFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";

    private static int[] sumsum;

    private Communicator comm;
    private TabSelector mTabSelector;

    private Spinner scoreSpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private String[] scoreTypes;

    private Button mThrowButton;

    private ImageView[]diceImageList;
    private Die[] diceList;
    private int[] whiteDice = {R.drawable.white1, R.drawable.white2, R.drawable.white3, R.drawable.white4, R.drawable.white5, R.drawable.white6};
    private int[] redDice = {R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6};
    private int[] diceID = {R.id.diceImageOne, R.id.diceImageTwo, R.id.diceImageThree, R.id.diceImageFour, R.id.diceImageFive, R.id.diceImageSix};

    public String[] score = {"-", "-", "-", "-", "-", "-", "-", "-", "-", "-"};

    private int countThrow = 0;
    private int countRound = 0;

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
     * When called upon from ScoreFragment, resets all the score in score array.
     */
    public void resetValues(){
        for(int i=0; i<score.length; i++){
            score[i]="-";
        }
        comm.respond(score);

        for (int i=0; i<scoreTypes.length;i++){
            spinnerAdapter.add(scoreTypes[i]);
            spinnerAdapter.notifyDataSetChanged();
        }
        for (int i=0; i<diceList.length; i++){
            diceList[i].setDiceValue(0);
        }

        countRound = 0;

        resetSelected();
    }

    /**
     * Changes the selected state of all die to unselected.
     * Called upon after every round.
     */
    private void resetSelected(){
        for (int i=0; i<diceImageList.length; i++){
            diceImageList[i].setImageResource(whiteDice[diceList[i].getDiceValue()]);
            diceList[i].setSelected(false);
        }
    }

    /**
     * Keeps listening if the dices have been pressed, if they have, we change their background
     * and set their pressed attribute to false/true.
     * Also, throw listens and check if the score type has any value, if it has, it gives a toast
     * telling the user to change score type, else initiate throwDie().
     */
    private void checkPressed(){
        for (int i=0; i<diceList.length;i++){
            final int j = i;
            diceImageList[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (countThrow!=0) {
                        if (diceList[j].isSelected()) {
                            diceImageList[j].setImageResource(whiteDice[diceList[j].getDiceValue()]);
                            diceList[j].setSelected(false);
                        } else {
                            diceImageList[j].setImageResource(redDice[diceList[j].getDiceValue()]);
                            diceList[j].setSelected(true);
                        }
                    }
                }
            });
        }

        mThrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countRound < 10) {
                    try {
                        if (score[Integer.parseInt(scoreSpinner.getSelectedItem().toString()) - 3].equals("-")) {
                            throwDie();
                        } else {
                            Toast.makeText(getActivity(), R.string.full_toast, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        if (scoreSpinner.getSelectedItem().equals("Low") && score[0].equals("-")) {
                            throwDie();
                        } else {
                            Toast.makeText(getActivity(), R.string.full_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.newgame_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Always shuffles the unpressed dices. If there is only one shuffle left. The throw button
     * changes text to "calculate?" but is also supposed to shuffle onClick. If i'ts the last throw,
     * we go to scoreCalculatorLow() or scoreCalculator, depending on the scoring type.
     * After that the score is calculated through totalScore() and put in the score array which is
     * sent to scoreFragment.
     */
    private void throwDie(){
        if (countThrow < 3) {
            countThrow++;
            for (int i = 0; i < diceList.length; i++) {
                if (!diceList[i].isSelected()) {
                    diceList[i].setDiceValueRandom();
                    diceImageList[i].setImageResource(whiteDice[diceList[i].getDiceValue()]);
                    diceList[i].setSelected(false);
                }
            }
        }
        if (countThrow == 3){
            countThrow++;
            mThrowButton.setText("Calculate?"); // R.string.calculate_button crashes?

        }
        else if (countThrow == 4){
            countThrow = 0;
            resetSelected();
            if (scoreSpinner.getSelectedItem().toString().equals("Low")){
                scoreCalculatorLow();
                score[0] = Integer.toString(totalScore());
            } else {
                scoreCalculator();;
                score[Integer.parseInt(scoreSpinner.getSelectedItem().toString())-3] = Integer.toString(totalScore());
            }

            mThrowButton.setText(R.string.throw_button);

            comm.respond(score);
            countRound++;

            spinnerAdapter.remove(scoreSpinner.getSelectedItem().toString());
            spinnerAdapter.notifyDataSetChanged();

            if (countRound==10){
                mTabSelector.onTabSwitch(1);
            }
        }
    }

    /**
     * Checks which dices will give points for this round and sums them up
     *
     * @return The total score of all die that are being counted towards the total score
     */
    private int totalScore (){
        int totPoint = 0;
        for (int i = 0; i<diceList.length; i++){
            if (diceList[i].toBeScore()){
                totPoint = totPoint + diceList[i].getDiceValue()+1;
                diceList[i].setToBeScore(false);
            }
        }
        return totPoint;
    }

    /**
     * When scoring type "Low" is chosen, all dices with value three or under will give points-
     */
    private void scoreCalculatorLow(){
        for (int i = 0; i<diceList.length; i++){
            if (diceList[i].getDiceValue()+1 <=3){
                diceList[i].setToBeScore(true);
            }
        }
    }

    /**
     *  Method to figure out all combinations that gives a given sum. Code taken from user JosAH
     (https://bytes.com/topic/software-development/answers/781089-check-all-combinations-numbers-given-sum)
     uses recursive function solve()
     */
    private void scoreCalculator(){

        int[] numbers = new int[diceList.length];
        for (int i = 0; i<diceList.length; i++){
            numbers[i]=diceList[i].getDiceValue()+1;
        }

        sumsum= new int[numbers.length];

        int sum= Integer.parseInt(scoreSpinner.getSelectedItem().toString());

        for (int s= 0, i= numbers.length; --i >= 0; ) {
            sumsum[i]= numbers[i]+s;
            s+= numbers[i];
        }
        solve(numbers, 0, sum, new ArrayList<Integer>());
    }

    /**
     * Part of scoreCalculator
     *
     * @param numbers Array with values of all die
     * @param i Recursive counter
     * @param sum The sum you want to achive.
     * @param solution List of all possible solutions.
     */
    private void solve(int[] numbers, int i, int sum, List<Integer> solution) {

        if (sum == 0)
            solution(solution);
        else
            for (; i < numbers.length && sumsum[i] >= sum; i++) {
                if (numbers[i] <= sum) {
                    solution.add(0, numbers[i]);
                    solve(numbers, i+1, sum-numbers[i], solution);
                    solution.remove(0);
                }
            }
    }

    /**
     * My function to figure out which dices to use when calculating score.
     * The function scoreCalculator() calculates all possible combinations and since one die cannot
     * be used more than once, we have to use this function to make sure that all die are only used
     * once.
     *
     * @param solution filtered list of possible solutions.
     */
    private void solution(List<Integer> solution) {

        boolean possibleFalsePair = false;
        boolean falsePair = false;
        boolean haveChangedValue = false;

        List<Integer> falsePairIndex = new ArrayList<Integer>();


        for (int i = 0; i < solution.size(); i++) {
            haveChangedValue = false;
            for (int j = 0; j < diceList.length; j++) {
                if (solution.get(i) == (diceList[j].getDiceValue() + 1)) {
                    if (!diceList[j].toBeScore()) {
                        falsePairIndex.add(j);
                        diceList[j].setToBeScore(true);
                        haveChangedValue = true;
                        break;

                    } else {
                        possibleFalsePair = true;
                    }
                }

                if (j == (diceList.length-1) && !haveChangedValue && possibleFalsePair) {
                    falsePair = true;
                }
            }
        }
        if (falsePair) {
            for (int i= 0; i < falsePairIndex.size(); i++) {
                diceList[falsePairIndex.get(i)].setToBeScore(false);
            }
        }
    }

    /**
     * Initates all the dice, throwButton, scoreSpinner and spinnerAdapter.
     * Sets all dice backgrounds according to value and creates lists for
     * dices objects and dice ImageViews.
     *
     * @param inflater inflates xml to give a view
     * @param parent Fragment’s parent ViewGroup
     * @param savedInstanceState Prior state bundle
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, parent, false);

        diceImageList =  new ImageView[6];
        diceList = new Die[6];
        for (int i = 0; i < diceList.length; i++) {
            diceList[i] = new Die();
        }

        for (int i = 0; i < diceImageList.length; i++){
            diceImageList[i] = (ImageView) v.findViewById(diceID[i]);
            diceImageList[i].setImageResource(whiteDice[diceList[i].getDiceValue()]);
        }

        mThrowButton = (Button) v.findViewById(R.id.throwButton);

        scoreSpinner = (Spinner) v.findViewById(R.id.spinnerScore);

        Resources res = getResources();
        scoreTypes = res.getStringArray(R.array.score_array);
        spinnerAdapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new ArrayList(Arrays.asList(scoreTypes)));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreSpinner.setAdapter(spinnerAdapter);

        checkPressed();
        return v;
    }

}
