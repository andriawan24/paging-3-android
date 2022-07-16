package com.andriawan.testinglearning.utils.mapper

interface Mapper<LOCAL, REMOTE> {
    fun mapIncoming(response: REMOTE): LOCAL
    fun mapOutgoing(data: LOCAL): REMOTE
}