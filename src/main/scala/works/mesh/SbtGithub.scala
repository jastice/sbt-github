package works.mesh

import sbt._, Keys._
import dispatch._, dispatch.Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._
import spray.json._
import DefaultJsonProtocol._

object SbtGithub extends AutoPlugin {

  override def trigger = allRequirements

  object autoImport {
    val githubToken = settingKey[String]("GitHub API token. Don't commit this in your project!")
    val githubCreateProject = taskKey[Unit]("Create a github project and push this repository.")
  }

  import autoImport._


  override lazy val projectSettings = Seq(

    githubCreateProject := {

      val create = dispatch.url("https://api.github.com/user/repos")
        .addHeader("Authorization",s"token ${githubToken.value}")
        .setContentType("application/json", "UTF-8")
        .POST << s"""{ "name":${name.value} }"""

      val created = Http(create OK as.String)

      val gitPush = for {
        response <- created
      } yield {
        val sshUrl = response.parseJson.asJsObject.getFields("ssh_url").head.convertTo[String]
        s"git remote add origin $sshUrl".!!
        "git push -u origin master".!!
      }

      println(Await.result(gitPush ,1.minute))
    }

  )

}
