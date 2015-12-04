# MongoDB 3.1.1 Demo

+ 1. MongoDB 2.4.9 (sudo apt-get install mongodb-server)
+ 2. MongoDB-Java-Driver 3.1.1 [quick-tour](http://mongodb.github.io/mongo-java-driver/3.1/driver/getting-started/quick-tour/)
+ 3. JDK 1.8 
+ 4. Gradle 2.8
+ 5. Eclipse 

# Prepared data before run the Test [Authentication](http://stackoverflow.com/questions/21859579/authentication-during-connection-to-mongodb-server-instance-using-java)
+ sudo mongod -dbpath /var/lib/mongodb -logpath=/var/log/mongodb/mongodb_auth.log -auth

+ use admin & db.system.users.find();
```{ "_id" : ObjectId("566103fc6177d4098f7996e6"), "user" : "admin", "readOnly" : false, "pwd" : "90f500568434c37b61c8c1ce05fdf3ae" }```

+ use testdb & db.system.users.find();
```{ "_id" : ObjectId("5661045f6177d4098f7996e7"), "user" : "testdb", "readOnly" : false, "pwd" : "b9ff75cbf18bd98d8554efec12c72090" }```

+ 两张表[testdb, admin]都可以作为验证