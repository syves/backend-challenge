package controllers.posts

import com.google.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

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

  def delete(id: Int): Future[Unit] = {

    def optPost = posts.find(_.id == id)

    optPost match {
      case Some(p) =>
        Future {
          posts -= p
        }
    }
  }

  def updatePosts(id: Int, post: Post): Future[Either[String, Post]] = {

    def optPost = posts.find( p => p.id == id && id == post.id)

    optPost match {
      case Some(toUpdate)  =>
        Future {
          posts -= toUpdate
          posts += post
          Right(post)
        }
      case None => Future{ Left("Not able to update post.") }
    }
  }

}

object PostRepository {
  def apply(implicit ec: ExecutionContext): PostRepository = {
    val p = new PostRepository()
    p
  }
}