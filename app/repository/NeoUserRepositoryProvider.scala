package repository

trait NeoUserRepositoryProvider {

  class NeoUserRepoImpl extends NeoUserRepository
  val repo = new NeoUserRepoImpl

}

trait NeoRelationshipRepositoryProvider {

  class NeoRelationshipRepoImpl extends NeoRelationshipRepository
  val repo = new NeoRelationshipRepoImpl

}