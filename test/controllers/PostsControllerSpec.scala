package controllers


import controllers.posts.{PostRepository, PostsController}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import javax.inject._
import play.api._
import play.api.mvc._
import com.google.inject.Inject
import play.api.test._
import play.api.test.Helpers._
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import play.api.libs.json._
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}


class PostsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  /*
  "PostsController POST/posts should" should {

    val executionContext = inject[ExecutionContext]
    implicit val sys = ActorSystem("MyTest")
    implicit val materializer = ActorMaterializer()

    "render a single post on `create` page from a new instance of controller" in {
      //postRepository, executionContext
      val controller = new PostsController(
        stubControllerComponents(),
        PostRepository(ec = executionContext),
        executionContext
      )

      val newPost = Post
      val create = controller.create(newPost).apply(FakeRequest(POST, "/posts"))

      status(create) mustBe OK

      contentType(create) mustBe Some("application/json")
      contentAsString(create) must include ("Create Post")
    }
  }
  */

  "PostsController GET/posts/:id" should {

    "render a single post on `posting` page from a new instance of controller" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        PostRepository(ec = executionContext),
        executionContext)

      val getById = controller.readSingle(id=1)().apply(FakeRequest(GET, "/posts/:id"))

      status(getById) mustBe OK
      //TODO create posting view
      val expected = Json.obj("id" -> 1 ,"title" -> "Title 1","body"-> "Body 1").toString

      contentType(getById) mustBe Some("application/json")
      contentAsString(getById) mustBe expected
    }
  }
/*
  "PostsController GET/posts" should {


    "render all the posts on `list` page from a new instance of controller" in {

    val executionContext = inject[ExecutionContext]
    implicit val sys = ActorSystem("MyTest")
    implicit val materializer = ActorMaterializer()
      val controller = new PostsController(stubControllerComponents(), PostRepository(ec = executionContext),
        executionContext)
      val posts = controller.readall().apply(FakeRequest(GET, "/posts"))

      status(posts) mustBe OK
      //TODO count of posts
      //TODO extra credit pagination?
      contentType(posts) mustBe Some("application/json")
      contentAsString(posts) must include ("Posts")
    }
    //    * TODO: Should return the Posts in ascending order on the ids.
  }

  "PostsController PUT/posts/:id" should {


    "render a single post on `update` page, after update, from a new instance of controller" in {
        val executionContext = inject[ExecutionContext]
    implicit val sys = ActorSystem("MyTest")
    implicit val materializer = ActorMaterializer()

      val controller = new PostsController(stubControllerComponents(),PostRepository(ec = executionContext),
        executionContext)
      val updateById = controller.update(id: Int)().apply(FakeRequest(PUT, "/posts/:id"))

      status(updateById) mustBe OK
      //TODO create update view

      contentType(updateById) mustBe Some("application/json")
      contentAsString(updateById) must include ("Update by id")
    }
  }

  "PostsController DELETE/posts/:id" should {


    "render a single message on `delete` page, after update, from a new instance of controller" in {

     val executionContext = inject[ExecutionContext]
    implicit val sys = ActorSystem("MyTest")
    implicit val materializer = ActorMaterializer()
      val controller = new PostsController(stubControllerComponents(), PostRepository(ec = executionContext),
        executionContext)
      val DeleteById = controller.delete(id: Int)().apply(FakeRequest(POST, "/posts/:id"))

      status(DeleteById) mustBe OK

      //TODO check for post after by Id
      //TODO create delete view

      contentType(DeleteById) mustBe Some("application/json")
      contentAsString(DeleteById) must include ("Delete by id")
    }
  }
*/



} //this is the closing bracket