package com.oreomrone.locallens.ui.utils

import com.oreomrone.locallens.domain.User

fun getRecipientFromThreadParticipantsPair(
  participants: Pair<User, User>,
  currentUser: User
): User {
  return if (participants.first.id !== currentUser.id) {
    participants.first
  } else {
    participants.second
  }
}