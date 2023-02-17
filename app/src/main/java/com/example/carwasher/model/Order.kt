package com.example.carwasher.model

data class Order(val name:String
, val phoneNumber:String
, val carNumber:String
, val carModel:String
, val washerPhase:String
,val timePhase:Int
,val price :Int) {
}