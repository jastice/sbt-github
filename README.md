# sbt github

Publish sbt projects to github.
This plugin depends on [sbt-git](TODO), and thus includes all the functionality of that plugin.

## Usage

I recommend adding the plugin to your global/user plugins rather than the project itself.

Add this line to `~/.sbt/0.13/plugins/plugins.sbt`

    addSbtPlugin("TODO")
    
### Settings

To make this plugin work at all, you need to create a [GitHub token](TODO). In your `~/.sbt/0.13/Global.sbt`, add this:

    import works.mesh.GithubPlugin.autoImport._
    
    githubToken := "your_github_token"
    

Optionally, set the remote. Default is `origin`

    githubRemote := "origin"
    
    
### Run it

Run the `githubCreateProject` task to create a GitHub project from your sbt project. The ssh url of the freshly created
project is returned by the task. If a project already exists under your account, the task returns the ssh url without 
creating anything. The project name and description on GitHub are based on the project `name` and `description` settings. 

    sbt githubCreateProject