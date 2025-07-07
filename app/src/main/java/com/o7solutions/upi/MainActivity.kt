package com.o7solutions.upi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.o7solutions.upi.BroadcastReceiver.BroadcastReceiverActivity
import com.o7solutions.upi.Sensor.SensorActivity

class MainActivity : AppCompatActivity() {


    private val UPI_PAYMENT_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val click: TextView = findViewById(R.id.click)
        val show: TextView = findViewById(R.id.show)
        val sensorBTn: Button = findViewById<Button>(R.id.sensorBtn)

        sensorBTn.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
            finish()

        }


        click.setOnClickListener {

            if (show.visibility == View.GONE) {
                show.visibility = View.VISIBLE
            } else {
                show.visibility = View.GONE

            }
        }

        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener {
            payUsingUpi(
                amount = "1.00",
                upiId = "merchant@upi",
                name = "Merchant Name",
                note = "Payment for order"
            )
        }

        val broadcastBtn: Button = findViewById(R.id.broadcast)
        broadcastBtn.setOnClickListener { view->

            val intent = Intent(this,BroadcastReceiverActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val response = data.getStringExtra("response")
                    Log.d("UPI", "Response: $response")
                    if (response != null) {
                        if (response.lowercase().contains("success")) {
                            Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri

        val chooser = Intent.createChooser(intent, "Pay with UPI")

        if (chooser.resolveActivity(packageManager) != null) {
            startActivityForResult(chooser, UPI_PAYMENT_REQUEST_CODE)
        } else {
            Toast.makeText(
                this,
                "No UPI app found. Please install one to continue.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}