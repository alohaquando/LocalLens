package com.oreomrone.locallens.domain

data class User(
  val id: String = "",
  val name: String = "",
  val username: String = "",
  val email: String = "",
  val bio: String = "",
  val image: String = "",
  val isSuperUser: Boolean = false,
  val isPrivate: Boolean = false,
  var posts: List<Post> = listOf(),
  var places: List<Place> = listOf(),
  var followers: List<User> = listOf(),
  var followings: List<User> = listOf(),
)

// TODO: Requested