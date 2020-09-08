Open Source DB3 Tutorial Island TaskScript! 

Hey all,
I just finished making my first script! I developed it using the DB3 beta. Right now it is not meant for public use, but more aimed at showing devs a decent, large DB3 example. It includes most things that any bot will do in game. 
I tried to follow the new DB3 best practices IE: Widgets.getClosest not getWidgets ETC... I did not use any methods that will be deprecated in DB3. 
Please post a comment or open a PR on github if you notice anything I did wrong, any bugs, or have any improvements. Any and all feedback is welcome! 

NOTE: This script is still not a 1.0 release and has some bugs! It is also missing some features (paint), and it does some very sketchy things (Clicking through walls...). I am working on improving the utils I wrote to be more Realistic. 
Please feel free to test it and report any bugs (GitHub issues or here). I will be pushing this to the DB script repo (Free) as soon as I am happy with it and DB3 is released. 

More details about the script:

I did try something kind of weird. The script extends TaskScript and using TaskNodes (standard), but I also added a double layered state machine. ScriptState and TaskState. The script is broken into ScriptState (each tutor), and TaskStates (each step to complete a tutor).
 
The ScriptState keeps track of what TaskNode(tutor) should be run, and the second state machine is essentially a double linked list of jobs
that need to be completed for each tutor. The actual TaskNode.execute just runs the TaskState. Each enum in TaskState contains its own logic that decides if it should be run, what it should do, and what task is next. 

This was a fun little experiment to try, and it seems to work well. For simple, linear tasks. I am probably going mess around with a slight variation on it in my next script. In that one some logic that tells the TaskState if is should be run will be pulled
into a hash table/B-tree. I think Pandemic posted something along this line already. 