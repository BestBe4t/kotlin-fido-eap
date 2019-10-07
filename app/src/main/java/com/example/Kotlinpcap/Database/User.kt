package com.example.Kotlinpcap.Database

class User {
    var id = 0
    lateinit var userName: String
    lateinit var userPassword: String

    constructor(userName: String, userPassword: String){
        this.userName = userName
        this.userPassword = userPassword
    }
}