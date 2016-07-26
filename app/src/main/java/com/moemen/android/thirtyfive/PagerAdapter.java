package com.moemen.android.thirtyfive;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Adapter to populate pages inside our ViewPager
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private ScoreFragment mScoreFragment;
    private DiceFragment mDiceFragment;

    /**
     * Initialize the PagerAdapter and also create new fragment-objects.
     *
     * @param fm fragmentManager to keep track of fragments
     * @param NumOfTabs number of tabs/views.
     */
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.mScoreFragment = new ScoreFragment();
        this.mDiceFragment = new DiceFragment();
    }

    /**
     * Takes the position of the view we want to change to and returns corresponding fragment
     *
     * @param position of the view
     * @return fragment of the new view.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DiceFragment tab1 = this.mDiceFragment  ;
                tab1.setCommunicator(new Communicator() {
                    /**
                     * Callback used for fragment dicesFragment to communicate with
                     * fragment scoreFragment. Keeps updating the score
                     * @param data update of the score in the score table.
                     */
                    @Override
                    public void respond(String[] data) {
                        ScoreFragment frag = (ScoreFragment) getItem(1);
                        frag.changeData(data);
                    }
                    @Override
                    public void needRestart() {}
                });
                return tab1;
            case 1:
                ScoreFragment tab2 = this.mScoreFragment;
                tab2.setCommunicator(new Communicator() {
                    @Override
                    public void respond(String[] data) {}
                    /**
                     * Callback used for fragment scoreFragment to communicate with
                     * fragment dicesFragment. Resets the score
                     */
                    @Override
                    public void needRestart() {
                        DiceFragment frag = (DiceFragment) getItem(0);
                        frag.resetValues();
                    }
                });
                return tab2;
            default:
                return null;
        }
    }

    /**
     * Method that returns the number of tabs
     * @return number of tabs
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}