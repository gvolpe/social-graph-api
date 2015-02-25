package model

sealed trait RelationshipType
case object Friend extends RelationshipType
case object Follower extends RelationshipType
case object Unknown extends RelationshipType

object RelationshipTypeFinder {

  def fromValue(value: String): RelationshipType = {
    val upperValue = value.trim.toUpperCase()
    upperValue match {
      case "FRIEND" => Friend
      case "FOLLOWER" => Follower
      case _ => Unknown
    }
  }

  def complement(relationship: RelationshipType): RelationshipType = relationship match {
    case Friend => Follower
    case Follower => Friend
    case _ => Unknown
  }

}