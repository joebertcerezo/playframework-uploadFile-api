package models

import play.api.libs.json.*
import play.api.data.format.Formatter
import play.api.data.FormError

package object domain {

  object JsonFormats {
    def opaqueFormat[T](
        apply: JsValue => JsResult[T],
        extract: T => JsValue
    ): Format[T] = new Format[T] {
      def writes(t: T): JsValue             = extract(t)
      def reads(json: JsValue): JsResult[T] = apply(json)
    }

    /* def opaqueFormat[T](
        apply: JsValue => JsResult[T],
        extract: T => JsValue
    ): Format[T] = new {
      def writes(t: T): JsValue             = extract(t)
      def reads(json: JsValue): JsResult[T] = apply(json)
    } */

    def opaqueFormatter[A, T](
        apply: A => T,
        extract: T => A
    )(using formatter: Formatter[A]): Formatter[T] =
      new Formatter[T] {
        override def bind(
            key: String,
            data: Map[String, String]
        ): Either[Seq[FormError], T] =
          formatter.bind(key, data) match {
            case Left(errors) => Left(errors)
            case Right(value) => Right(apply(value))
          }

        override def unbind(key: String, value: T): Map[String, String] =
          formatter.unbind(
            key,
            extract(value)
          ) 
      }
  }

}
