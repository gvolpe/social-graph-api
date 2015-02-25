package model

sealed trait RelationshipType
case object Friend extends RelationshipType
case object WorkMate extends RelationshipType
case object Unknown extends RelationshipType