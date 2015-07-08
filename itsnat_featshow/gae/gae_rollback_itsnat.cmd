
call _gae_shared_itsnat.cmd


call %GAE%\appcfg.cmd rollback %PROJECT%\build\web

pause

