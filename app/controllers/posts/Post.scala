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
    )(Post.apply(id, title, body))(Post.unapply())
  )
  //TODO update form
*/

}

