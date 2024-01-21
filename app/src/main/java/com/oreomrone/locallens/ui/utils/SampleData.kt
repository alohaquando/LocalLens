package com.oreomrone.locallens.ui.utils

import MessageThread
import com.oreomrone.locallens.domain.Message
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.User

object SampleData {
  // User
  val sampleUser: User = User(
    id = "user_id_0",
    name = "Sheen Hahn",
    username = "sheen_hahn_athan",
    email = "sheen.hahn@email.com",
    bio = "I'm a software engineer, and I love to travel and take photos!",
    image = "https://images.unsplash.com/photo-1612177277051-70a1e17d9571?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mjh8fGFzaWFuJTIwbWVufGVufDB8fDB8fHww"
  )
  val sampleUser1: User = User(
    id = "user_id_1",
    name = "Alice Johnson",
    username = "alice_j",
    email = "alice.j@sample.com",
    bio = "Wanderlust \uD83C\uDF0E",
    image = "https://images.unsplash.com/photo-1529232356377-57971f020a94?q=80&w=3388&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
  )
  val sampleUser2: User = User(
    id = "user_id_2",
    name = "Bob Smith",
    username = "bob_s",
    email = "bob.smith@sample.com",
    bio = "It's gonna be legen... wait for it... dary! \uD83D\uDE02",
    image = "https://images.unsplash.com/photo-1582439170934-d089aa10abda?q=80&w=3371&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
  )
  val sampleSuperUser: User = User(
    id = "user_id_3",
    name = "Kiryu Kazuma",
    username = "kiryu_k",
    email = "kiryu_k@email.com",
    bio = "I am the fourth chairman of the Tojo Clan (also the Super User) \uD83D\uDC51",
    image = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp" + ".com/f/44f24785-bc55-403f-9852-eb3e642975d6/dezz4h0-11a4d0e3-bd7f-467e-8e31-deff43be094d.png/v1/fill/w_1192,h_670,q_70,strp/kiryu_karaoke_renders___today_is_a_diamond_by_virtualbeef_dezz4h0-pre.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NDMyMCIsInBhdGgiOiJcL2ZcLzQ0ZjI0Nzg1LWJjNTUtNDAzZi05ODUyLWViM2U2NDI5NzVkNlwvZGV6ejRoMC0xMWE0ZDBlMy1iZDdmLTQ2N2UtOGUzMS1kZWZmNDNiZTA5NGQucG5nIiwid2lkdGgiOiI8PTc2ODAifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6aW1hZ2Uub3BlcmF0aW9ucyJdfQ.VLc_tMr0GRmOGvvVv1GHLrZVtbv6U-kCOXkbAs4CVc4",
  )

  // Place
  val samplePlace: Place = Place(
    id = "place_id_1",
    name = "Saigon riverside park\n" + "Công viên Bờ sông Sài Gòn - Tp Thủ Đức",
    address = "31/2 Lương Định Của, An Khánh, Quận 2, Thành phố Hồ Chí Minh, Vietnam",
    image = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?q=80&w=3270&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    latitude = 10.778602471891732,
    longitude = 106.71099622000814
  )
  val samplePlace1: Place = Place(
    id = "place_id_2",
    name = "Bui Vien Walking Street\n" + "Phố đi bộ Bùi Viện",
    address = "Đ. Bùi Viện, Phường Phạm Ngũ Lão, Quận 1, Thành phố Hồ Chí Minh, Vietnam",
    image = "https://images.unsplash.com/photo-1516253954075-008d92ddede0?q=80&w=3274&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    latitude = 10.76757064851108,
    longitude = 106.6939904969385
  )

  // Post
  val samplePost: Post = Post(
    id = "post_id_0",
    place = samplePlace,
    caption = "I love this city!",
    timestamp = "2024-01-14T12:00:00",
    favorites = listOf(
      sampleUser1,
      sampleUser2
    ),
    postImageModel = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?q=80&w=3270&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
  )
  val samplePost1: Post = Post(
    id = "post_id_1",
    place = samplePlace1,
    caption = "Girl just wanna have fun \uD83D\uDC81\u200D♀\uFE0F✨",
    timestamp = "2024-01-14T12:00:00",
    favorites = listOf(
      sampleSuperUser,
      sampleUser,
      sampleUser1,
      sampleUser2
    ),
    postImageModel = "https://images.unsplash.com/photo-1516253954075-008d92ddede0?q=80&w=3274&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
  )

  // Messages
  val sampleMessage = Message(
    sampleUser,
    "As the sun sets on another day, I find myself reflecting on the incredible bond we've built over time.",
    "12:00"
  )

  val sampleMessage1 = Message(
    sampleUser1,
    "I appreciate the genuine connection we share and the way you make every moment memorable.",
    "12:05"
  )

  val sampleMessage2 = Message(
    sampleUser2,
    "Here's to the countless more memories we'll create and the adventures that lie ahead. Cheers" +
            " to our journey!",
    "12:05"
  )

  val sampleThread = MessageThread(
    id = "sample_thread",
    participants = Pair(
      sampleUser,
      sampleUser1
    ),
    messages = listOf(
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
      sampleMessage,
      sampleMessage1,
    ),
    lastUpdated = "12:00",
    hasUnreadMessage = true
  )

  val sampleThread1 = MessageThread(
    id = "sample_thread",
    participants = Pair(
      sampleUser,
      sampleUser2
    ),
    messages = listOf(
      sampleMessage,
      sampleMessage2
    ),
    lastUpdated = "3:00",
    hasUnreadMessage = false
  )

  init {
    // User
    sampleUser.posts = sampleUser.posts.plus(
      samplePost,
    )
    sampleUser.places = sampleUser.places.plus(samplePlace)
    sampleUser.followers = sampleUser.followers.plus(
      listOf(
        sampleSuperUser,
        sampleUser1,
        sampleUser2
      )
    )
    sampleUser.following = sampleUser.following.plus(
      listOf(
        sampleUser1,
        sampleUser2
      )
    )
    sampleUser1.posts = sampleUser1.posts.plus(
      samplePost1,
    )

    // Post
    samplePost.user = sampleUser
    samplePost1.user = sampleUser1

    // Place
    samplePlace.posts = samplePlace.posts.plus(
      samplePost,
    )
  }
}