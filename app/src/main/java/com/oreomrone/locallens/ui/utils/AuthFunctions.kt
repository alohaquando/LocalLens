package com.oreomrone.locallens.ui.utils

import com.oreomrone.locallens.domain.User

fun validatePassword(password: String): Boolean {
  return password.length > 8 && password.contains(
    regex = Regex(
      """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\d\s])"""
    )
  )
}

fun validatePasswordConfirm(
  passwordConfirm: String,
  password: String
): Boolean {
  return passwordConfirm == password
}

fun validateEmail(email: String): Boolean {
  return email.isNotBlank() && email.contains(Regex("""^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"""))
}

suspend fun getCurrentUser(): User {
  return SampleData.sampleUser // TODO
}