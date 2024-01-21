package com.oreomrone.locallens.ui.utils

fun validateUsername(username: String): Boolean {
  return username.isNotBlank() && !username.contains(" ") // TODO: check if username is unique
}

fun validateBio(bio: String): Boolean {
  return bio.isNotBlank()
}

fun validateName(name: String): Boolean {
  return name.isNotBlank()
}