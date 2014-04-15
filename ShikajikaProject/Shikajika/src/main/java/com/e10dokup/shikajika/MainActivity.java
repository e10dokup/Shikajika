package com.e10dokup.shikajika;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter = null;
    private Face face = new Face(0,0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {

        }

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetoothくらい有効にしとけ馬鹿", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        BluetoothDevice btDevice = null;
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice dev : bondedDevices) {
            if (dev.getName().equals("EasyBT")) {
                btDevice = dev;
            }
        }

        if (btDevice == null) {
            Toast.makeText(this,"Target Bluetooth device is not found.",Toast.LENGTH_LONG).show();
            return;
        }
        Utils.device = btDevice;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CameraFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings1) {
            face.sendMove("1");
            return true;
        }
        if (id == R.id.action_settings2) {
            face.sendMove("2");
            return true;
        }
        if (id == R.id.action_settings3) {
            face.sendMove("3");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
