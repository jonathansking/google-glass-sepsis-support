How to use git for Team iHuman

When you start working, 
$ git pull
So that you are up to speed.

When you are done working
	$ git add fileYouWantChanged //OR// $ git add *
	$ git commit -m "Message of what you did this commit"
At this point, its commited to your local, but not to bitbucket.
	To add to BitBucket
	$ git push

===============
===BRANCHING===
To list all branches on local
	$ git branch

To create a new branch (aka a new feature)
	$ git checkout -b NewFeature

To delete a branch
	$ git branch -d NewFeature

To push branch to remote, while in branch
	$ git push origin NewFeature


