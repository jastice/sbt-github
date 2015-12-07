package works.mesh

import com.ning.http.client.Response
import sbt._, Keys._
import net.caoticode.buhtig.Buhtig
import net.caoticode.buhtig.Converters._
import com.typesafe.sbt.GitPlugin
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._


object GithubPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = GitPlugin

  object autoImport {
    val githubToken = settingKey[String]("GitHub API token. Don't commit this in your project!")
    val githubRemote = settingKey[String]("The GitHub git remote")

    // TODO reuse keys: name, description
    // TODO add keys for repo options: private, has_issues, has_wiki, has_downloads, default_branch
    // TODO add optional github org setting / publishing
    // TODO feature: create gist?

    // tasks

    /** Create a github project if it does not exist.
      * if `origin` is not set, and the github repo is empty,
      * set it to the github repo and push.
      *
      * Result: Some(ssh url of repo). None if there was any problem */
    val githubCreateProject = taskKey[Option[String]]("Create a github project and push this repository.")
    val githubUser = taskKey[Option[String]]("Get the user (login name) authenticated by the githubToken")

    // TODO add tasks: update settings, get repo info json, get various repo info specifically
  }

  import autoImport._
  import GitPlugin.autoImport._


  override lazy val projectSettings = Seq(

    githubUser := {
      val client = new Buhtig(githubToken.value).syncClient
      for { userJson <- client.user.getOpt[JSON] }
      yield compact(render(userJson \ "login"))
    },

    githubCreateProject := {

      val repoName = name.value
      val client = new Buhtig(githubToken.value).syncClient
      val dir = baseDirectory.value
      val log = sbt.ConsoleLogger.apply()
      val gitRunner = git.runner.value
      def gitRun(args: String*) = gitRunner.apply(args:_*)(dir,log)

      val remotes = gitRun("remote")

      if (remotes contains githubRemote.value) {
        // check github if repo already exists
        for {
          user <- githubUser.value
          repoResponse = (client.repos / user / repoName).get[Response]
          if repoResponse.getStatusCode == 200
        } yield {
          val repoResponseJson: JSON = repoResponse
          compact(render(repoResponseJson \ "ssh_url"))
        }

      } else {
        // create the repo

        val create = client.user.repos POST
          ("name" -> repoName) ~
            ("description" -> description.value)

        val createResponse = create.get[Response]
        val statusCode = createResponse.getStatusCode
        val responseJson: JSON = createResponse

        val repoJsonOpt: Option[JSON] = statusCode match {
          case 200 =>
            Some(responseJson)
          case 422 =>
            val msg = compact(render(responseJson \ "errors" \ "message"))
            if (msg == "name already exists on this account")
              for {
                user <- githubUser.value
              } yield
                (client.repos / user / repoName).getOpt[JSON]

            else None
        }

        for {
          repoJson <- repoJsonOpt
        } yield {
          val sshUrl = compact(render(repoJson \ "ssh_url"))
          gitRun("remote", "add", "origin", sshUrl)
          gitRun("push", "-u", "origin", "master")
        }
      }

    }

  )

}
