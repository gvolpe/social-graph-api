package repository

import auth.repository.{DefaultPasswordRepository, PasswordRepositoryImpl, PasswordRepository, DefaultUserIdentityRepository}
import auth.service.{DefaultUserIdentityService, UserIdentityService}
import auth.{SignUp, UserIdentity}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import org.specs2.matcher.FutureMatchers
import org.specs2.mutable._

import scala.concurrent.Future

class DefaultPasswordRepositoryTest extends Specification with FutureMatchers {

  "DefaultPasswordRepository" should {

    "Find some password info" in {
      val repo: PasswordRepository = new PasswordRepositoryImpl with DefaultPasswordRepository
      val loginInfo = LoginInfo("id", "key")
      val result: Future[Option[PasswordInfo]] = repo.findPwd(loginInfo)
      result must be_==(None).await
    }

    "Save and find the password info" in {
      val repo: PasswordRepository = new PasswordRepositoryImpl with DefaultPasswordRepository
      val loginInfo = LoginInfo("some-id", "some-key")
      val authInfo = PasswordInfo("bcrypt", "h4sh3d-p4ssw0rd")
      val result: Future[PasswordInfo] = repo.savePwd(loginInfo, authInfo)
      result must be_==(authInfo).await

      val foundPwdInfo: Future[Option[PasswordInfo]] = repo.findPwd(loginInfo)
      foundPwdInfo must be_==(Some(authInfo)).await
    }

  }

}
