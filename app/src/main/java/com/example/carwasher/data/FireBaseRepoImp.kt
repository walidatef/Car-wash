package com.example.carwasher.data

import android.content.Context
import android.widget.Toast
import com.example.carwasher.IsSuccessOrder
import com.example.carwasher.model.Order
import com.google.firebase.database.*

class FireBaseRepoImp(private val database: DatabaseReference, private val context: Context) :
    FireBaseRepo {
    override fun getQueue() {
        TODO("Not yet implemented")
    }

    override fun getPhases() {
    }

    override fun insertOrder(order: Order) {
        val myRef = database.child("orders")
        val orderId = myRef.push().key!!
        myRef.child(orderId).setValue(order).addOnCompleteListener {
            if (it.isSuccessful) {
                database.child("queue").child("Cars_front")
                    .setValue(ServerValue.increment(1))
                database.child("queue").child("total_time")
                    .setValue(ServerValue.increment(order.timePhase.toLong()))

                IsSuccessOrder.setFlag(true)
            }
        }.addOnFailureListener { error ->
            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            IsSuccessOrder.setFlag(false)
        }
    }

}