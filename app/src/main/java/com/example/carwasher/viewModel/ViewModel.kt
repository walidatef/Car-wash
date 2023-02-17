package com.example.carwasher.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.carwasher.IsSuccessOrder
import com.example.carwasher.model.Order
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Objects

class ViewModel : androidx.lifecycle.ViewModel() {
    private val db = Firebase.database.reference
    val queueData = MutableLiveData<Map<String, Any>>()
    val phasesData = MutableLiveData<Map<*, *>>()

    fun fetchData(context: Context) {

        val queueListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                queueData.postValue(dataSnapshot.value as Map<String, Any>?)
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.toException().toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        db.child("queue").addValueEventListener(queueListener)

        val phasesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                phasesData.postValue(dataSnapshot.value as Map<*, *>?)
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.toException().toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        db.child("phases").addValueEventListener(phasesListener)
    }


    fun insertOrder(context: Context, order: Order) {

        val myRef = db.child("orders")
        val orderId = myRef.push().key!!
        myRef.child(orderId).setValue(order).addOnCompleteListener {

            db.child("queue").child("Cars_front")
                .setValue(ServerValue.increment(1))
            db.child("queue").child("total_time")
                .setValue(ServerValue.increment(order.timePhase.toLong()))

            IsSuccessOrder.setFlag(true)
        }.addOnFailureListener { error ->
            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            IsSuccessOrder.setFlag(false)
        }


    }
}