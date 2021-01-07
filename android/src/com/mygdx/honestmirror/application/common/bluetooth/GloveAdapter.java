package com.mygdx.honestmirror.application.common.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.HashMap;
import java.util.Set;


public class GloveAdapter {
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    private HashMap<Integer, String> stateMap = new HashMap<Integer, String>();

    public GloveAdapter() {
        this.stateMap.put(1, "STATE_OFF");
        this.stateMap.put(2, "STATE_TURNING_ON");
        this.stateMap.put(3, "STATE_ON");
        this.stateMap.put(4, "STATE_TURNING_OFF");
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }

    public String getState() {
        return this.stateMap.get(bluetoothAdapter.getState());
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void connect() {
        BluetoothAdapter.connect();
    }
}
