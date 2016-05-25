$CATALINA_HOME/bin/catalina.sh stop
cp ../conf/server.xml $CATALINA_HOME/conf/
cp ../lib/hsqldb.jar $CATALINA_HOME/lib/
$CATALINA_HOME/bin/catalina.sh start
