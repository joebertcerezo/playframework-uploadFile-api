package models
package repo

import javax.inject.*

import play.api.db.slick.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import slick.jdbc.JdbcProfile

import domain.*
import domain.types.*

@Singleton
class FileRepo @Inject() (
    val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import dbConfig.profile.api.*

  class FileTable(tag: Tag) extends Table[File](tag, "FILES") {
    val id   = column[IdFile]("ID", O.PrimaryKey)
    val name = column[String]("NAME")
    val path = column[String]("PATH")
    val idUser = column[Option[IdUser]]("IDUSER")

    def * = (id, name, path, idUser).mapTo[File]
  }

  object Table extends TableQuery(new FileTable(_)){
    def create(file: File): Future[Int] = {
      db.run(this += file)
    }
  }
}
