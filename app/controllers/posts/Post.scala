package controllers.posts

import play.api.libs.json.{Format, Json}
import play.api.data._


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

}

