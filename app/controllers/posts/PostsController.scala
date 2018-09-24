package controllers.posts

import scala.concurrent.{ExecutionContext, Future}

import javax.inject.Inject
import javax.inject._

import play.api._
import play.api.libs.json._
import play.api.libs.json.JsValue
import play.api.mvc._



class PostsController @Inject()(
                                 cc: ControllerComponents,
                                 postRepository: PostRepository,
                                 implicit val executionContext: ExecutionContext
                               ) extends AbstractController(cc) {

  /**
    * This takes a Json in the format of
    *
    * {
    * "id": 1,
    * "title": "My Title",
    * "body": "My Post"
    * }
    *
    * and saves it into the persistance layer. The created Post is then returned.
    *
    * It should fail, if there is already a Post with the same id present.
    *
    */
  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val postResult = request.body.validate[Post]

    postResult.fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("status" -> "400", "message" -> JsError.toJson(errors)))
        }
      },
      post => {
        //check if post with id already exists
        postRepository.find(post.id).map { opt: Option[Post] =>
          opt match {
            case Some(p) => BadRequest(Json.obj("status"->400, "message"-> "Id is already in use"))
            case None    => postRepository.insert(post)
                            Ok((Json.obj("status" -> 200, "data" -> Json.toJson(post))))
          }
        }
      }
    )
  }


  /**
    * This returns a Json Array with a list of all Posts.
    *
    * Should return the Posts in ascending order on the ids.
    */
  def readAll(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    postRepository.findAll.map { posts =>
      val json = Json.toJson(posts.sortBy(_.id))
      Ok(Json.obj("status" -> 200, "data" -> json))
    }
  }

  /**
    * Returns only the post with the matching id
    * If the post does not exist, returns a 404 with a json like
    *
    * {
    * "status": 404,
    * "message": "Post not found"
    * }
    *
    */
  def readSingle(id: Int): Action[AnyContent] = Action.async { implicit request =>

    postRepository.find(id).map { opt: Option[Post] =>
      opt match {
        case Some(p) => Ok(Json.obj("status" -> 200, "data" -> p))
        case None => NotFound(Json.obj("status" -> 404, "message" -> "Post not found"))
      }
    }
  }

  /**
    * Does not contain any body in the request
    *
    * Deletes the post with the given id.
    */

  def delete(id: Int): Action[AnyContent] = Action.async { request =>
    postRepository.delete(id).map(res => Ok(Json.toJson("status" -> "200")))
  }

  /**
    * Request body contains the post.
    *
    * Updates the post with the given id.
    * Changing the id of a post must not possible.
    */
  def update(id: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val readResultrequest.body.validate[Post]

    readResult.fold(
      errors => {
        Future.successful {
          NotFound(Json.obj("status" -> "404", "message" -> "Post not found"))
        }
      },
      post => {
       postRepository.updatePosts (id, post).map { e =>
          e match {
            case Right(p)  => Ok(Json.obj ("status" -> 200, "data" -> p))
            case Left(msg) =>  NotFound(Json.obj("status" -> 400, "message" -> msg))
          }
        }
      }
    )
  }

}
