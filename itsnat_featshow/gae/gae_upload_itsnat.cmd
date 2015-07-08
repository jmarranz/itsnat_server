
call _gae_shared_itsnat.cmd

set ITSNAT_GAE=%PROJECT%\fw_dist\gae
set PROJECT_BUILD_LIB=%PROJECT%\build\web\WEB-INF\lib
set PROJECT_BUILD_TMP=%PROJECT_BUILD_LIB%\..\tmp_gae

mkdir %PROJECT_BUILD_TMP%
move  %PROJECT_BUILD_LIB%\batik-dom.jar %PROJECT_BUILD_TMP%\
copy %ITSNAT_GAE%\lib\batik-dom-gae.jar %PROJECT_BUILD_LIB%\

call %GAE%\appcfg.cmd update %PROJECT%\build\web

del  %PROJECT_BUILD_LIB%\batik-dom-gae.jar
move  %PROJECT_BUILD_TMP%\batik-dom.jar %PROJECT_BUILD_LIB%\batik-dom.jar
rmdir %PROJECT_BUILD_TMP%

pause

