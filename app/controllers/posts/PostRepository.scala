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

  //TODO Future option
  def find(id: Int): Future[Either[String, Post]] = {

      posts.find(_.id == id) match {
        case Some(post) => Future { Right(post) }
        case None => Future{ Left("Post not found") }
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

  def delete(id: Int): Future[Either[String, Post]] = {

    def optPost = posts.find(_.id == id)

    optPost match {
      case Some(p) =>
        Future {
          posts -= p
          Right(p)
        }
      case None => Future {Left("Id not found")}
    }
  }

  def updatePosts(id: Int, post: Post): Future[Either[String, Post]] = {

    posts.find( p => p.id == id && id == post.id) match {
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
