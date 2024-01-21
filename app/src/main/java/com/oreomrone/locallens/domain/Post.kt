package com.oreomrone.locallens.domain

data class Post(
  val id: String,
  val place: Place,
  val caption: String,
  val timestamp: String,
  val favorites: List<User> = listOf(),
  val image: Any,
  var user: User? = null,
) {
  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Post

    return id == other.id
  }
}

// TODO: Visibility