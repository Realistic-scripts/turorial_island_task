Open Source DB3 Tutorial Island TaskScript! 

Hey all,
I just finished making my first script! I developed it using the DB3 beta JAR. Here is the source on Github!

Right now it is NOT meant for public use, but aimed at showing devs a decent, large DB3 example and getting feedback from other devs about how I can improve. It includes most things that any bot will do. I tried to follow the new DB3 best practices IE: Widgets.getClosest not getWidgets ETC... I did not use any methods that will be deprecated in DB3. That being said THIS IS NOT PERFECT. I am new to Java/Scripting and want any and all feedback! Please post a comment or open a PR/Issue on GitHub if you notice anything I did wrong, bugs, or have any suggestions.

NOTE: This script is still not a 1.0 release and has some (lots of) bugs! It is also missing some features (paint), and it does some very sketchy things (Clicking through walls...). I am working on improving the utils I wrote to be more Realistic(TM) ;) and I will be updating the repo on a regular bases.

I will be pushing this to the DB script repo as a free script as soon as I am happy with it and DB3 is released. Until then, have fun looking through the code and running it from source!



More details about the script:

I tried something kind of weird. The script extends TaskScript and using TaskNodes (standard), but I decided to use  a double layered state machine. The layers are in two files: ScriptState and TaskState.

The script is broken into ScriptState (each tutor), and TaskStates (each step to complete a tutor).
 
The ScriptState keeps track of what TaskNode(tutor) should be run, and the second state machine is essentially a double linked list of jobs that need to be completed for each tutor. The actual TaskNode.execute()  just runs the TaskState in each Java file. While each enum in TaskState contains its own logic that decides if it should be run, what it should do, and what task is next.

This was a fun little experiment to try, and it seems to work well, At least for simple linear tasks. I am going mess around with a slight variation it in my next project. In that one the logic that tells the TaskState if it should be run will be pulled into a hash table/B-tree and the current player state will be checked vs that to decide what to do. I think Pandemic posted something along this line already.

I have also started working on another git repo that contains useful snippets, DB3 vs DB2 code, and any open source DB scripts that I can find. I will be making a post about that in the next couple of days.

Thank you to @holic for providing the snippet I used to generate names. See their original topic here. 
# Overview
This is my first script and it is NOT PERFECT! Well, lets be honest... It is not really very good. I have a long list of TODOs(Check the TODO section of this README)
that I want to get done before I will consider this a 1.0 release. This project is meant as an example of how to do some
basic actions using DB3, and as a code snippet reference for newer scripters.  

# TODO


# Troubleshooting
If you have any issues with this script please grab the latest logs from your DreamBot logs folder and either PM them to Realistic
on the DB forums or make a github issue and attach them to that. 