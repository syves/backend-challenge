package controllers


import controllers.posts.{PostRepository, PostsController}
import controllers.posts.Post

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import javax.inject._
import com.google.inject.Inject

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.JsValue
import play.api.test._
import play.api.test.Helpers._

import org.scalatestplus.play._
import org.scalatestplus.play.guice._


import scala.concurrent.{ExecutionContext, Future}

//TODO dry up tests
class PostsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "PostsController POST/posts should" should {

    "This takes a Json Post saves it into the persistance layer. The created Post is then returned" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
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
      contentAsString(create) mustBe """{"status":200,"data":{"id":4,"title":"Shakrah","body":"Yves"}}"""
    }

    "It should fail, if there is already a Post with the same id present" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext
      )

      val newPost = Json.obj("id" -> 1, "title" -> "Shakrah", "body" -> "Yves")
      val create = controller.create()
        .apply(
          FakeRequest(
            method=POST,
            uri="/posts",

            headers=FakeHeaders(Seq("Content-type"-> "application/json")),
            body=newPost))

      status(create) mustBe 400
    }

    "It should fail, if Json content is incorrect" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext
      )

      val newPost = Json.obj("id" -> 1, "title" -> 1, "body" -> "Yves")
      val create = controller.create()
        .apply(
          FakeRequest(
            method=POST,
            uri="/posts",

            headers=FakeHeaders(Seq("Content-type"-> "application/json")),
            body=newPost))

      status(create) mustBe 400
    }
  }


  "PostsController GET/posts/:id" should {

    "Returns only the post with the matching id" in {

      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext)

      val getById = controller.readSingle(id=1)().apply(FakeRequest(GET, "/posts/:id"))

      status(getById) mustBe OK
      val expected = """{"status":200,"data":{"id":1,"title":"Title 1","body":"Body 1"}}"""

      contentType(getById) mustBe Some("application/json")
      contentAsString(getById) mustBe expected
    }

    "If the post does not exist, returns a 404 with a json" in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext)

      val getById = controller.readSingle(id=3)().apply(FakeRequest(GET, "/posts/:id"))

      status(getById) mustBe 404
    }
  }

  "PostsController GET/posts" should {

    "returns all the posts in ascending order" in {

      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext)

      val posts = controller.readAll().apply(FakeRequest(GET, "/posts"))
      val expected = """{"status":200,"data":[{"id":1,"title":"Title 1","body":"Body 1"},{"id":2,"title":"Title 2","body":"Body 2"}]}"""
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
        new PostRepository()(executionContext),
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
      contentAsString(updateById) mustBe """{"status":200,"data":{"id":1,"title":"newTitle","body":"newBody"}}"""
    }

    "Changing the id of a post must not possible." in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext)

      val newPost = Json.obj("id" -> 3, "title" -> "Title 1", "body" -> "Body 1")
      val updateById = controller.update(1)
        .apply(
          FakeRequest(
            method=POST,
            uri="/posts/:id",

            headers=FakeHeaders(Seq("Content-type"-> "application/json")),
            body=newPost))

      status(updateById) mustBe 404
    }

    "fails on id not found." in {
      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext)

      val newPost = Json.obj("id" -> 5, "title" -> "newTitle", "body" -> "newBody")
      val updateById = controller.update(1)
        .apply(
          FakeRequest(
            method=POST,
            uri="/posts/:id",

            headers=FakeHeaders(Seq("Content-type"-> "application/json")),
            body=newPost))

      status(updateById) mustBe 404
    }
  }

  "PostsController DELETE/posts/:id" should {

    "Does not contain any body in the request. Deletes the post with the given id." in {

      val executionContext = inject[ExecutionContext]
      implicit val sys = ActorSystem("MyTest")
      implicit val materializer = ActorMaterializer()

      val controller = new PostsController(
        stubControllerComponents(),
        new PostRepository()(executionContext),
        executionContext)

      val DeleteById = controller.delete(id=2)().apply(FakeRequest(POST, "/posts/:id"))

      status(DeleteById) mustBe 200
    }
  }

}