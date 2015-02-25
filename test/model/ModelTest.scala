package model

import org.specs2.mutable._

class ModelTest extends Specification {

  "Relationship Type" should {

    "Friend String " in {
      val friend = "Friend"
      friend.toUpperCase() must equalTo(Friend.toString.toUpperCase)
      Friend must typedEqualTo(RelationshipTypeFinder.fromValue(friend))
    }

    "Follower String " in {
      val follower = "Follower"
      follower.toUpperCase() must equalTo(Follower.toString.toUpperCase)
      Follower must typedEqualTo(RelationshipTypeFinder.fromValue(follower))
    }

    "Unknown String one " in {
      Unknown must typedEqualTo(RelationshipTypeFinder.fromValue("one"))
    }

    "Unknown String two " in {
      Unknown must typedEqualTo(RelationshipTypeFinder.fromValue("sdajkhg"))
    }

  }

}
