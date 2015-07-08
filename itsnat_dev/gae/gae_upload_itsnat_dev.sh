#!/bin/sh

source ./_gae_shared_itsnat_dev.sh

ITSNAT_GAE=$PROJECT/../itsnat/fw_dist/gae
PROJECT_BUILD_LIB=$PROJECT/build/web/WEB-INF/lib
PROJECT_BUILD_TMP=$PROJECT_BUILD_LIB/../tmp_gae

mkdir $PROJECT_BUILD_TMP
mv $PROJECT_BUILD_LIB/batik-dom.jar $PROJECT_BUILD_TMP/
cp $ITSNAT_GAE/lib/batik-dom-gae.jar $PROJECT_BUILD_LIB/

$GAE/appcfg.sh -e jmarranz@innowhere.com update $PROJECT/build/web

rm  $PROJECT_BUILD_LIB/batik-dom-gae.jar
mv  $PROJECT_BUILD_TMP/batik-dom.jar $PROJECT_BUILD_LIB/batik-dom.jar
rmdir $PROJECT_BUILD_TMP

