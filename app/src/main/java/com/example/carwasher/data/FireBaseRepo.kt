package com.example.carwasher.data

import com.example.carwasher.model.Order

interface FireBaseRepo {
    fun getQueue()
    fun getPhases()
    fun insertOrder(order: Order)

}