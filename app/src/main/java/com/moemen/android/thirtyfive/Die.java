package com.moemen.android.thirtyfive;

import java.util.Random;

/**
 * Represents a Die that can roll in to the values of a D6.
 * A die can can also have two states, isSelected and toBeScore.
 * isSelected means that the die is selected to not be shuffled/thrown
 * toBeScore means that value of the die will add towards the score of the round.
 *
 */

public class Die {

    private int diceValue;
    private Random rand = new Random();
    private boolean selected;
    private boolean beScore;


    public Die(){
        /**
         * Creates a new Die with the value 1
         * normal state is unselected and not counted towards the score.
         *
         */
        diceValue = 0;
        selected = false;
        beScore = false;

    }

    /**
     * Gets the current value of this die.
     * @return die value.
     */
    public int getDiceValue() {
        return diceValue;
    }

    /**
     * Change the value of this die to a specific value.
     * This method is only used when initializing a new game
     * @param value new value of die
     */
    public void setDiceValue(int value) {
        this.diceValue = value;
    }

    /**
     * Change the value of this die to a random value.
     * This method is used when throwing die.
     * @return new value of die
     */
    public int setDiceValueRandom() {
        return this.diceValue = rand.nextInt(6);
    }

    /**
     * Gets the selected-state of this die
     * @return selected-state of die
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Change the selected-state of this die
     * @param selected new selected-state of die
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Gets the score-state of the die.
     * @return score-state of the die
     */
    public boolean toBeScore() {
        return beScore;
    }

    /**
     * Change the score-state of this die
     * @param beScore new score-state of die
     */
    public void setToBeScore(boolean beScore) {
        this.beScore = beScore;
    }

}
