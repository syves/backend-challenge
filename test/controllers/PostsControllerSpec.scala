package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class PostsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "PostsController GET/posts" should {

    "render all the posts on `list` page from a new instance of controller" in {
      val controller = new PostsController(stubControllerComponents())
      val posts = controller.readall().apply(FakeRequest(GET, "/posts"))

      status(posts) mustBe OK
      //TODO some("json")
      //TODO create posts
      //TODO delete posts after
      //TODO count of posts
      //TODO extra credit pagination?
      //contentType(posts) mustBe Some("text/html")
      contentAsString(posts) must include ("Posts")
    }
  }


  "PostsController GET/posts/:id" should {

    "render a single post on `posting` page from a new instance of controller" in {
      val controller = new PostsController(stubControllerComponents())
      val getById = controller.readSingle(id: Int)().apply(FakeRequest(GET, "/posts/:id"))

      status(getById) mustBe OK
      //TODO some("json")
      //TODO create post
      //TODO delete post after
      //TODO create posting view

      //contentType(getById) mustBe Some("text/html")
      contentAsString(getById) must include ("Post by id")
    }
  }

  "PostsController PUT/posts/:id" should {

    "render a single post on `update` page, after update, from a new instance of controller" in {
      val controller = new PostsController(stubControllerComponents())
      val updateById = controller.update(id: Int)().apply(FakeRequest(PUT, "/posts/:id"))

      status(updateById) mustBe OK
      //TODO some("json")
      //TODO create post
      //TODO delete post after
      //TODO create update view

      //contentType(updateById) mustBe Some("text/html")
      contentAsString(updateById) must include ("Update by id")
    }
  }

  "PostsController DELETE/posts/:id" should {

    //todo check delete behavior..i think returns a message?
    "render a single post? on `delete` page, after update, from a new instance of controller" in {
      val controller = new PostsController(stubControllerComponents())
      val DeleteById = controller.delete(id: Int)().apply(FakeRequest(Delete, "/posts/:id"))

      status(DeleteById) mustBe OK
      //TODO some("json") ?
      //TODO create post
      //TODO check for post after by Id
      //TODO create delete view

      //contentType(DeleteById) mustBe Some("text/html")
      contentAsString(DeleteById) must include ("Delete by id")
    }
  }

  "PostsController POST/posts should" should {

    "render a single post on `create` page from a new instance of controller" in {
      val controller = new PostsController(stubControllerComponents())
      val create = controller.create().apply(FakeRequest(POST, "/posts"))

      status(create) mustBe OK
      //TODO some("json") ?
      //TODO create post
      //TODO check for post after by Id
      //TODO create delete view

      //contentType(create) mustBe Some("text/html")
      contentAsString(create) must include ("Create Post")
    }
  }
  

} //this is the closing bracket