    package com.o7solutions.upi.BroadcastReceiver

    import android.content.BroadcastReceiver
    import android.content.Context
    import android.content.Intent
    import android.widget.Toast

    class Receiver: BroadcastReceiver() {


        override fun onReceive(context: Context?, intent: Intent?) {

    //        getting intent action when specific intent is received
            val action = intent!!.action

            when(action) {

    //            receiving intent when airplane mode changed
                Intent.ACTION_AIRPLANE_MODE_CHANGED->{

                    val airplaneFlag = intent.getBooleanExtra("state",false)

                    if(airplaneFlag) {
                        Toast.makeText(context, "Airplane mode is On!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Airplane mode is Off!", Toast.LENGTH_SHORT).show()
                    }
                }

    //            action boot completed
                Intent.ACTION_BOOT_COMPLETED -> {
                    Toast.makeText(context, "Boot Completed!", Toast.LENGTH_SHORT).show()
                }
//                Intent.


//                action for battery low
                Intent.ACTION_BATTERY_LOW -> {
                    Toast.makeText(context,"Battery Low!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }