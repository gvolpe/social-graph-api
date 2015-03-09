package model

import auth.role.{SimpleUser, Admin, Role}
import org.specs2.mutable._

class RoleSpec extends Specification {

  "Role Type" should {

    "Admin" in {
      Role("admin") must haveClass[Admin.type]
    }

    "SimpleUser" in {
      Role("user") must haveClass[SimpleUser.type]
    }

    "Unknown" in {
      Role("whatever") must haveClass[auth.role.Unknown.type]
      Role("unknown") must haveClass[auth.role.Unknown.type]
    }

  }

}
