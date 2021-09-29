package com.acompworld.teamconnect.utils
sealed class Resource<T> (
    val  data : T? =null,
    val  msg : String? = null
){
    class success<T>(data : T): Resource<T>(data)
    class error<T>(data: T?=null, msg: String?): Resource<T>(data, msg)
    class loading<T>(data: T?=null) : Resource<T>(data)
}