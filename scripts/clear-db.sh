cp init-db.script ../db/bourse.script
cd ../db
java -cp ../lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:bourse --dbname.0 bourse
