TACAdX
======

This is the repo for Brown University's AdX agent.

If the runAgent.sh and runServer.sh scripts aren't exicutable: chmod +x <name>.sh

To run the server: ./runServer.sh
To run the agent:

1) edit the TACAdX/config/aw-1.conf file so that agentImpl=brown.tac.adx.agents.BrownAgent

2) make a jar of the brown directory, found in TACAdX/bin: jar cf brown-agent.jar brown
3) move the jar to the TACAdX/lib directory: mv brown-agent.jar ../lib  
-yes, you want to override

4) from the TACAdX directory: ./runAgent


Tips:
- you should start the server before you start the agent
- if the agent starts throwing multiple connections errors, close all terminals and restart
- 
