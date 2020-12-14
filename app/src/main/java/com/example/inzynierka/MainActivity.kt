package com.example.inzynierka

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    val REQUEST_ENABLE_BLUETOOTH = 1
    var bluetoothSocket: BluetoothSocket? = null
    lateinit var bluetoothAdapter: BluetoothAdapter
    var isConnected: Boolean = false
    var connectSuccess = true


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seek = findViewById<SeekBar>(R.id.simpleSeekBar)
        val text = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        text?.text = "Grade is 0%"


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Devices does not support bluetooth", Toast.LENGTH_SHORT).show()
        }
        if (!bluetoothAdapter?.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
            Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show()

        }
        seek?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text?.text = "Grade is " + seek.progress + "%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@MainActivity,
                        "You stopped giving grade at " + seek.progress + "%",
                        Toast.LENGTH_SHORT).show()
            }

        })
        button.setOnClickListener {
            if (bluetoothAdapter.isEnabled) {

                Toast.makeText(this, "Connection started", Toast.LENGTH_SHORT).show()
                connect()
            }
            else {
                Toast.makeText(this, "BT is disabled", Toast.LENGTH_SHORT).show()
            }
        }
        button2.setOnClickListener {
            if (bluetoothAdapter.isEnabled) {

                Toast.makeText(this, "Disconnecting", Toast.LENGTH_SHORT).show()
                disconnect()
            }
            else {
                Toast.makeText(this, "BT is disabled", Toast.LENGTH_SHORT).show()
            }
        }

        val grade = seek.progress
        val strGrade = grade.toString()
        val timer = object: CountDownTimer(1000000, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                writeData("\n" + strGrade)
            }
            override fun onFinish() {

            }
        }
        if (isConnected) {
            timer.start()
        }

    }
    private fun connect() {

        try {
            if (bluetoothSocket == null || !isConnected) {
                Toast.makeText(this, "Connection started", Toast.LENGTH_SHORT).show()
                val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice("98:D3:81:FD:68:E1")
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                bluetoothSocket!!.connect()
            }
        } catch (e: IOException) {
            connectSuccess = false
            e.printStackTrace()
        }
        if (!connectSuccess) {
            Log.i("data", "couldn't connect")
        } else {
            isConnected = true
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
        }

    }
    private fun disconnect() {
        if (bluetoothSocket != null)
        try {
            bluetoothSocket?.close()
            bluetoothSocket = null
            isConnected = false
        } catch (e: IOException) {
            Log.e("TAG", "Could not close the client socket", e)
            e.printStackTrace()
        }
    }
    fun writeData(data: String) {
        if ( bluetoothSocket != null)
            try {
                bluetoothSocket!!.outputStream.write(data.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_ENABLE_BLUETOOTH ->
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Could not connect", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}









