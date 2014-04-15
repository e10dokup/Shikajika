package com.e10dokup.shikajika;

public interface ImagePostListener {
    abstract public void postCompletion(byte[] response);
    abstract public void postFialure();
}
