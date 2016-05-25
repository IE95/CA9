if [ -z "$CATALINA_HOME" ]; then
    echo "The environment variable CATALINA_HOME must be set to the root of the Tomcat installation directory"
    exit 1
fi  

cd ..

rm -rf target/*
mkdir target
mkdir target/WEB-INF
mkdir target/WEB-INF/classes
mkdir target/WEB-INF/lib
mkdir target/js


javac -sourcepath src -classpath $CATALINA_HOME/lib/servlet-api.jar -d target/WEB-INF/classes  src/ir/Boors/Model/*.java src/ir/Boors/ServiceHandler/*.java src/ir/Boors/ServiceHandler/OpTypeHandler/*.java
cp conf/web.xml target/WEB-INF
cp lib/* target/WEB-INF/lib
cp js/* target/js
cp -r pages/* target

