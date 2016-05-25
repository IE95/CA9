if [ -z "$CATALINA_HOME" ]; then
    echo "The environment variable CATALINA_HOME must be set to the root of the Tomcat installation directory"
    exit 1
fi  

cd ..

APP_CONTEXT=boors

rm -rf $CATALINA_HOME/webapps/$APP_CONTEXT/*
mkdir $CATALINA_HOME/webapps/$APP_CONTEXT/
cp -r target/* $CATALINA_HOME/webapps/$APP_CONTEXT/