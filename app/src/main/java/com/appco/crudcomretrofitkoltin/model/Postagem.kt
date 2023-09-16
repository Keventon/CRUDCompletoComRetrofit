package com.appco.crudcomretrofitkoltin.model

data class Postagem(
    val body: String,
    val id: Int,
    val title: String?,
    val userId: Int
)