package models

import play.api.libs.json.*
import play.api.data.format.Formatter
import play.api.data.FormError

package object domain {

  object JsonFormats {
    def opaqueFormat[T](
        apply: JsValue => JsResult[T],
        extract: T => JsValue
    ): Format[T] = new {
      def writes(t: T): JsValue             = extract(t)
      def reads(json: JsValue): JsResult[T] = apply(json)
    }

    def opaqueFormatter[T](
        apply: String => T,
        extract: T => String
    ): Formatter[T] =
      new Formatter[T] {
        override def bind(
            key: String,
            data: Map[String, String]
        ): Either[Seq[FormError], T] =
          data
            .get(key)
            .filter(_.nonEmpty)
            .toRight(Seq(FormError(key, "error.required")))
            .map(apply)

        override def unbind(key: String, value: T): Map[String, String] =
          Map(key -> extract(value))
      }
  }

}
