package models

import java.util.UUID

import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api.uuidColumnType
import slick.jdbc.PostgresProfile.api.MappedColumnType

import models.domain.types.*

package object repo {

  given JdbcType[IdUser] =
    MappedColumnType.base[IdUser, UUID](_.value, IdUser.apply)

  given JdbcType[IdFile] =
    MappedColumnType.base[IdFile, UUID](_.value, IdFile.apply)
}
