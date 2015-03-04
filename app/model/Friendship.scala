package model

import play.api.libs.json.Json

case class Friendship(me: Long, friend: Long)

object Friendship {

  implicit val friendshipFormat = Json.format[Friendship]

}
