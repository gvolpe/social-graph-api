package controllers

import model.{RelationshipType, User}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repository.RelationshipRepository

import scala.concurrent.Future

trait DefaultRelationshipRepositoryProvider {

  class RepoImpl extends DefaultRelationshipRepository
  val repo = new RepoImpl

}

trait DefaultRelationshipRepository extends RelationshipRepository {

  def create(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean] = Future {
    true
  }

  def delete(relationshipType: RelationshipType, id1: Long, id2: Long): Future[Boolean] = Future {
    true
  }

  def find(relationshipType: RelationshipType, id: Long): Future[List[Option[User]]] = Future {
    List()
  }

}
