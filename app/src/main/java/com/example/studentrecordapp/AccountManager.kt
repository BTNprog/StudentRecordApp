package com.example.studentrecordapp

data class Account(val username: String, val password: String)

object AccountManager {
    val accounts = mutableListOf<Account>()
    var isLoggedIn: Boolean = false
    var currentUsername: String? = null

    fun register(account: Account): Boolean {
        if (accounts.any { it.username == account.username }) {
            return false
        }
        accounts.add(account)
        App.instance.saveData()
        return true
    }

    fun login(username: String, password: String): Boolean {
        val success = accounts.any { it.username == username && it.password == password }
        if (success) {
            isLoggedIn = true
            currentUsername = username
            App.instance.saveData()
        }
        return success
    }

    fun logout() {
        isLoggedIn = false
        currentUsername = null
        App.instance.saveData()
    }
}