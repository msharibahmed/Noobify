package com.example.myapplication.helpers

open class Event<out T>(private val data:T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled():T?{
        return if(hasBeenHandled){
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    //if wanna peek content even though its handled
    fun peekContent() = data
}
