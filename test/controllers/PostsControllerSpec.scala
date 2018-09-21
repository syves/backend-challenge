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

    "This takes a Json Post saves it into the persistance layer. The created Post is then returned" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        PostRepository(ec = executionContext),
        executionContext
      )

      val newPost = Json.obj("id" -> 4, "title" -> "Shakrah", "body" -> "Yves")
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
    //"It should fail, if there is already a Post with the same id present" in {}
    //if wrong content type error contains an example?
  }


  "PostsController GET/posts/:id" should {

    "Returns only the post with the matching id" in {

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
    // "If the post does not exist, returns a 404 with a json" on {}
  }

  "PostsController GET/posts" should {

    "returns all the posts in ascending order" in {

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


  "PostsController PUT/posts/:id" should {


    "This takes a Json Post.Updates the post with the given id. Changing the id of a post must not possible." in {
        val executionContext = inject[ExecutionContext]
        implicit val sys = ActorSystem("MyTest")
        implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        PostRepository(ec = executionContext),
        executionContext)

      val newPost = Json.obj("id" -> 1, "title" -> "newTitle", "body" -> "newBody")
      val updateById = controller.update(1)
        .apply(
          FakeRequest(
            method=POST,
            uri="/posts/:id",

            headers=FakeHeaders(Seq("Content-type"-> "application/json")),
            body=newPost))

      status(updateById) mustBe OK

      contentType(updateById) mustBe Some("application/json")
      contentAsString(updateById) mustBe """{"id":1,"title":"newTitle","body":"newBody"}"""
    }
    //"Changing the id of a post must not possible." in {}
    //when id is not found
  }

  "PostsController DELETE/posts/:id" should {


    "Does not contain any body in the reques. Deletes the post with the given id." in {

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
    //"when id not found" in {}
  }




} //this is the closing bracket