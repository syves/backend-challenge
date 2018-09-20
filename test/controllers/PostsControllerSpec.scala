package controllers


import controllers.posts.{PostRepository, PostsController}
import controllers.posts.Post
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

  "PostsController POST/posts should" should {

    "render a single post on `create` page from a new instance of controller" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        PostRepository(ec = executionContext),
        executionContext
      )

      val newPost = Json.obj("id" -> 4, "title" -> "Shakrah", "body" -> "Yves")
      //FakeRequest(POST, path, postHeaders, body)
      val create = controller.create()
        .apply(
          FakeRequest(
            method=POST,
            uri="/posts",

            headers=FakeHeaders(Seq("Content-type"-> "application/json")),
            body=newPost))

      status(create) mustBe OK

      contentType(create) mustBe Some("application/json")
      contentAsString(create) mustBe """{"id":4,"title":"Shakrah","body":"Yves"}"""
    }
  }


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
      val expected = Json.obj("id" -> 1 ,"title" -> "Title 1","body"-> "Body 1").toString

      contentType(getById) mustBe Some("application/json")
      contentAsString(getById) mustBe expected
    }
  }

  "PostsController GET/posts" should {

    "render all the posts in ascending order on `list` page from a new instance of controller" in {

    val executionContext = inject[ExecutionContext]
    implicit val sys = ActorSystem("MyTest")
    implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        PostRepository(ec = executionContext),
        executionContext)

      val posts = controller.readAll().apply(FakeRequest(GET, "/posts"))
      val expected = """[{"id":1,"title":"Title 1","body":"Body 1"},{"id":2,"title":"Title 2","body":"Body 2"}]"""
      status(posts) mustBe OK
      contentType(posts) mustBe Some("application/json")
      contentAsString(posts) mustBe expected
    }
  }

  /*

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
*/
  "PostsController DELETE/posts/:id" should {


    "render a single message on `delete` page, after update, from a new instance of controller" in {

     val executionContext = inject[ExecutionContext]
    implicit val sys = ActorSystem("MyTest")
    implicit val materializer = ActorMaterializer()
      val controller = new PostsController(stubControllerComponents(), PostRepository(ec = executionContext),
        executionContext)
      val DeleteById = controller.delete(id=2)().apply(FakeRequest(POST, "/posts/:id"))

      status(DeleteById) mustBe OK

      contentType(DeleteById) mustBe Some("application/json")
      contentAsString(DeleteById) must include ("Post has been deleted")

      val posts = controller.readAll().apply(FakeRequest(GET, "/posts"))
      val expected = """[{"id":1,"title":"Title 1","body":"Body 1"}]"""
      contentAsString(posts) must include
    }
  }




} //this is the closing bracket