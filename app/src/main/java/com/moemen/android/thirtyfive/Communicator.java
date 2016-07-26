package com.moemen.android.thirtyfive;

/**
 * Interface to allow fragments communicate with each other.
 */
public interface Communicator {
    void respond(String[] data);
    void needRestart();
}
