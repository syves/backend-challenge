package controllers.posts

import javax.inject._
import play.api._
import play.api.mvc._
import com.google.inject.Inject
import play.api.libs.json._
import play.api.libs.json.JsValue
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import akka.stream.ActorMaterializer
/**
  * A "persistance" layer for the [[Post]]
  */
class PostRepository @Inject()(
                                implicit val executionContext: ExecutionContext
                              ) {

  /**
    * This is the place where all posts are stored. You may change the type, but stick to solution form the
    * scala-std-library.
    */
  private val posts: ListBuffer[Post] = ListBuffer(
    Post(2, "Title 2", "Body 2"),
    Post(1, "Title 1", "Body 1")
  )

  def find(id: Int): Future[Option[Post]] = {
    Future {
      posts.find(_.id == id)
    }
  }

  def findAll: Future[Seq[Post]] = {
    Future {
      posts
    }
  }

  def insert(post: Post): Future[Post] = {
    Future {
      posts += post
      post
    }
  }

  //TODO should these be implemented in terms of the other methods?
  def delete(id: Int): Future[JsValue] = {

    //list diff List(id)

    def optPost = posts.find(_.id == id)

    optPost match {
      case Some(p) =>
        Future {
          posts -= p
          Json.obj("message" -> "Post has been deleted")
        }
    }
  }





}

object PostRepository {
  def apply(implicit ec: ExecutionContext): PostRepository = {
    val p = new PostRepository()
    p
  }
}