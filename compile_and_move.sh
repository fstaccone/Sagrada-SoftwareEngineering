#!/bin/bash
basedir=`pwd`/target/classes/it/polimi/ingsw

mvn compile

if [[ $? -eq 0 ]]; then
  # client
  cp $basedir/Client.class ./classloading/client/
  cp $basedir/view/TextView.class ./classloading/client/

  # server
  cp $basedir/Server.class ./classloading/server/
  cp $basedir/control/Controller.class ./classloading/server/


  # codebase
  cp $basedir/control/RemoteController.class ./classloading/common/
  cp $basedir/model/Database.class ./classloading/common/
  cp $basedir/model/User.class ./classloading/common/
  cp $basedir/view/RemoteBaseView.class ./classloading/common/

fi
