package com.oreomrone.locallens.domain

data class Place(
  val id: String,
  val name: String,
  val address: String,
  val image: String = "",
  val latitude: Double,
  val longitude: Double,
  var posts: List<Post> = listOf(),
) {
  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Place

    return id == other.id
  }
}
