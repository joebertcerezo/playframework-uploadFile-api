package models

import java.util.UUID

import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api.uuidColumnType
import slick.jdbc.PostgresProfile.api.MappedColumnType
import slick.jdbc.PostgresProfile.api.stringColumnType

import models.domain.types.*

package object repo {

  given JdbcType[IdUser] =
    MappedColumnType.base[IdUser, UUID](_.value, IdUser.apply)

  given JdbcType[IdFile] =
    MappedColumnType.base[IdFile, UUID](_.value, IdFile.apply)

  given JdbcType[EmailUser] =
    MappedColumnType.base[EmailUser, String](_.value, EmailUser.apply)

  given JdbcType[Username] =
    MappedColumnType.base[Username, String](_.value, Username.apply)
}
