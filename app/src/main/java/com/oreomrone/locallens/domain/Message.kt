package com.oreomrone.locallens.domain

data class Message(
  val sender: User,
  val content: String,
  val timestamp: String
)