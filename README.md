# Overview
This is my first script, and it is NOT PERFECT! Well, lets be honest... It is not really very good. I have a long list 
of TODOs(Check the TODO section of this README) that I want to get done before I will consider this a 1.0 release. 
This project is meant as an example of how to do some basic actions using DB3, and as a code snippet reference for newer 
scripters. 

# Known issues
* If the script crashes, or you stop it then run it again, and your character is not in the first room it can struggle
to resume
    * FIX: Go to src/state/ScriptState Line:4 Change `States.GIELINOR_GUIDE` to `States.<the tutor you are on now>` and restart the script. Also, message me/post a github issue with the logs `Dreambot/Logs -> Sort by latest`

# TODO
Right now these are spread around with the tag `TODO` in a java comment. Feel free to search for them. I will be moving 
them here soon.

# Troubleshooting
If you have any issues with this script please grab the latest logs from your DreamBot logs folder and either PM them 
to Realistic on the DB forums or make a github issue and attach them to that. 


# FAQ
## This is kind of slow...
Yup. 1: I suck at coding, 2: I added some antibanish things, and they slow it down. I will be speeding this up a LOT
and adding options for it to GO FAST at a later data. 