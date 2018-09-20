package controllers.posts

import play.api.libs.json.{Format, Json}
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.mvc._


/**
  * Represents a single post
  */
case class Post(
               id: Int,
               title: String,
               body: String
               )

object Post {

  implicit val postFormat: Format[Post] = Json.format[Post]

  implicit def queryStringBindable(implicit postBinder: QueryStringBindable[Post]) = new QueryStringBindable[Post] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Post]] = {
      /*
      for {
        id <- postBinder.bind("id", params)
        title <- postBinder.bind("title", params)
        body <- postBinder.bind("body", params)
      } yield {
        (id, title, body) match {
          case (Right(id), Right(title), Right(body)) => Right(Post(id, title, body))
          case _ => Left("Unable to bind a Post")
        }
      }
      */
      Some(Right(Post(3, "test", "test body")))
    }
    override def unbind(key: String, post: Post): String = {
      postBinder.unbind("id", post) + "&" + postBinder.unbind("title", post) + "&" + postBinder.unbind("body", post)
    }
  }

  def apply(id: Int, title: String, body: String): Post = {
    val p = new Post(id, title, body)
    p
  }

  /*
  val postForm = Form(
    mapping(
      "id" -> number,
      "title" -> nonEmptyText,
      "body" -> nonEmptyText
    )(postForm.apply)(postForm.unapply)
  )
*/
}

