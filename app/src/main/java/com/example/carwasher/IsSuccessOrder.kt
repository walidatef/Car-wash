package com.example.carwasher

import java.util.EventListener

interface IsSuccessOrder {

    fun isSuccess(flag: Boolean)


    companion object {
        private var listener: IsSuccessOrder? = null
        fun setListener(listener: IsSuccessOrder) {
            this.listener = listener
        }

        fun setFlag(flag: Boolean) {
            listener?.isSuccess(flag)
        }
    }
}