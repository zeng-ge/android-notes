package com.example.session.layouts.databinding

class MessageQueue {
    private val blockObject = java.lang.Object()
    val queue = mutableListOf<String>()

    fun enqueue(message: String) {
        queue.add(message)
        blockObject.notify()
    }

    fun next(): String {
        while (true){
            synchronized(blockObject) {
                if(queue.size == 0) {
                    blockObject.wait()
                } else {
                    val message = queue.get(0)
                    queue.removeAt(0)
                    return message
                }
            }
        }
    }
}