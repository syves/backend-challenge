package controllers.posts


import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.JsValue


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
    * TODO: It should fail, if there is already a Post with the same id present.
    *
    */
  def create(post: Post): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val postResult = request.body.validate[Post]

    postResult.fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("status" -> "400", "message" -> JsError.toJson(errors)))
        }
      },
      post => {
        postRepository.insert(post).map(persisted => Ok(Json.toJson(persisted)))
      }
    )
  }

  /**
    * This returns a Json Array with a list of all Posts.
    *
    * TODO: Should return the Posts in ascending order on the ids.
    */
  def readAll(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    postRepository.findAll.map { posts =>
      val json = Json.toJson(posts.sortBy(_.id))
      Ok(json)
    }
  }

  /**
    * TODO: Returns only the post with the matching id
    * TODO: If the post does not exist, returns a 404 with a json like
    *
    * {
    * "status": 404,
    * "message": "Post not found"
    * }
    *
    */
  def readSingle(id: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val readResult = request.body.validate[Post]

    readResult.fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("status" -> "404", "message" -> "Post not found"))
        }
      },
      get => {
        def futOptPost = postRepository.find(id)

        futOptPost.map { opt: Option[Post] =>
          opt match {
            case Some(p) => Ok(Json.toJson(p))
          }
        }
      }
    )
  }

  /**
    * Does not contain any body in the request
    *
    * TODO Deletes the post with the given id.
    */

  def delete(id: Int): Action[JsValue] = Action.async(parse.json){ implicit request =>
    //val readResult = request.body.validate[Post]

    //readResult.fold(
      //errors => {
        //Future.successful {
          //BadRequest(Json.obj("status" -> "404", "message" -> "Post not found"))
        //}
      //},
      //post => {
        def futJS = postRepository.delete(id)

        futJS.map { j: JsValue => Ok(j)}
      //}
    //)
  }

  /**
    * Request body contains the post.
    *
    * TODO Updates the post with the given id.
    * TODO Changing the id of a post must not possible.
    */
  def update(id: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>
    val readResult = request.body.validate[Post]

    readResult.fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("status" -> "404", "message" -> "Post not found"))
        }
      },
      post => {
        def futOptPost = postRepository.update(id)

        futOptPost.map { opt: Option[Post] =>
          opt match {
            case Some(p) => Ok(Json.toJson(p))
          }
        }
      }
    )
  }


}
