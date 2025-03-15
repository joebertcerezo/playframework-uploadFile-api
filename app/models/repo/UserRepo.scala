package models
package repo

import java.util.UUID
import javax.inject.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.*
import scala.annotation.targetName

import play.api.db.slick.*

import slick.jdbc.JdbcProfile

import domain.*

@Singleton
class UserRepo @Inject() (
    val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import dbConfig.profile.api.*

  class UserTable(tag: Tag) extends Table[User](tag, "USER") {
    val id       = column[UUID]("ID", O.PrimaryKey)
    val name     = column[String]("NAME")
    val email    = column[String]("EMAIL")
    val username = column[String]("USERNAME")
    val password = column[String]("PASSWORD")
    val avatar   = column[Option[String]]("PASSWORD")

    def * = (id, name, email, username, password, avatar).mapTo[User]
  }

  object Table extends TableQuery(new UserTable(_)) {
    def create(user: User): Future[Int] = {
      db.run(this += user)
    }

    def find(id: UUID): Future[Option[User]] = {
      db.run(this.filter(_.id === id).result.headOption)
    }

    def find(usernameOrEmail: String): Future[Option[User]] = {
      db.run(
        this
          .filter(x =>
            x.username === usernameOrEmail || x.email === usernameOrEmail
          )
          .result
          .headOption
      )
    }
  }
}
