import com.oreomrone.locallens.domain.Message
import com.oreomrone.locallens.domain.User

data class MessageThread(
  val id: String,
  val participants: Pair<User, User>,
  val messages: List<Message>,
  val lastUpdated: String,
  val hasUnreadMessage: Boolean
)