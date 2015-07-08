#!/bin/sh

source ./_gae_shared_itsnat_dev.sh

$GAE/dev_appserver.sh $PROJECT/build/web
