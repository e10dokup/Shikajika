package com.e10dokup.shikajika;

import android.bluetooth.BluetoothSocket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class Face {
    private static final String TAG = Face.class.getSimpleName();
    private final Face self = this;

    final UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private int mSmile;
    private int mDoya;
    private int mTrouble;

    private String mMode = "0";

    public Face(int smile, int doya, int trouble){
        mSmile = smile;
        mDoya = doya;
        mTrouble = trouble;
    }

    public void setType(){
        if(mSmile>mDoya&&mSmile>mTrouble) mMode="1";
        if(mDoya>mSmile&&mDoya>mTrouble) mMode="2";
        if(mTrouble>mDoya&&mTrouble>mSmile) mMode="3";
        if(mTrouble<20&&mSmile<20&&mDoya<20) mMode="0";
    }



    public void sendMove(String command){

        BluetoothSocket btSocket;
        try {
            btSocket = Utils.device.createRfcommSocketToServiceRecord(sppUuid);
        } catch (IOException ex) {
            return;
        }

        for (int i = 0; ; i++) {
            try {
                btSocket.connect();
            } catch (IOException ex) {
                if (i < 5) {
                    continue;
                }
                return;
            }
            break;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(btSocket.getOutputStream(), "ASCII"));
            writer.write(command);
            writer.flush( );
        } catch (IOException ex) {
            return;
        }

        try {
            btSocket.close();
        } catch (IOException ex) {

            return;
        }

    }

    public void sendMoveTrue(){

        BluetoothSocket btSocket;
        try {
            btSocket = Utils.device.createRfcommSocketToServiceRecord(sppUuid);
        } catch (IOException ex) {
            return;
        }

        for (int i = 0; ; i++) {
            try {
                btSocket.connect();
            } catch (IOException ex) {
                if (i < 5) {
                    continue;
                }
                return;
            }
            break;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(btSocket.getOutputStream(), "ASCII"));
            writer.write(mMode);
            writer.flush( );
        } catch (IOException ex) {
            return;
        }

        try {
            btSocket.close();
        } catch (IOException ex) {

            return;
        }

    }


}