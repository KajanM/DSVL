# Contributing to Distributed Systems Virtual Lab

## Get the code to your local machine

1. fork the repo to your github repo

1. clone your repo to your computer
`git clone https://github.com/{your-github-name}/DSVL` (replace `{your-github-name}` with real one)

1. add this repo as your upstream (for pulling the latest changes)
`git remote add upstream https://github.com/KajanM/DSVL`

## Import the project to your favorite IDE

Following instructions are for `IntelliJ`. If you want build instructions for another IDE please open an issue.

This is a multi module project. So we will create an empty project and add each modules.
Before continuing make sure you have already cloned the repository.

### Create an empty project

1. File -> New -> Project -> Empty Project -> Next
1. set project name as `DSVL`
1. set the project location to the folder that you have just cloned
1. click Finish
1. Now you have created an empty project, let's add the modules

### Import project modules

#### Import bootstrap-server module

Let's import the `bootstrap-server` first. 

1. File -> Project Structure (`Ctrl + Alt + Shift + S`)
1. In Modules, click the plus button (`Alt + Insert`)
1. import module -> select the location of bootstrap-server directory
1. select `Create module from existing sources` -> Next -> Finish

#### Import other modules

Other module(s) use(s) `Gradle`. Instructions to import `Gradle` projects are given below.
For example let's import the `flood` module.

1. Assuming you are still in the project structure modules window, click the plus button (`Alt + Insert`)
1. import module -> select the location of `flood` directory
1. Select `Import module from external model` -> Gradle -> Next -> Finish
1. Apply -> OK

You have successfully imported the flood module.

## Testing the setup

1. Run `FloodApplication.java`
1. Open the browser
1. navigate to http://localhost:4500/test
1. If you get `cool` as response, well, everything is cool :)

## Contributing to the code

1. It's a good idea to pull changes from upstream often
`git pull upstream master`

1. assuming you are in `master` branch, checkout to a new branch to work on new feature
`git checkout -b {branch-name}` (replace `{branch-name}` with the original one)

1. Happy `coding`

1. Commit often

1. Before pushing to your remote please pull from `upstream` and do a `rebase`
    * `git checkout master`
    * `git pull upstream master`
    * `git checkout {branch-name}`
    * `git rebase master`

1. push to your remote repository
`git push origin {branch-name}`
(for the first push a new branch with `{branch-name}` will be created automatically)

1. create pull request

### Addressing review comments

1. Do necessary code changes
1. Commit
1. Push to the same branch
1. Pull request will be automatically updated
