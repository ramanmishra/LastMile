name := "LastMileETARecalculation"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.0"
libraryDependencies +=  "com.typesafe.akka" %% "akka-http-core" % "10.0.5"
libraryDependencies +="com.typesafe.akka" %% "akka-http" % "10.0.5"
libraryDependencies +="com.typesafe.akka" %% "akka-http-testkit" % "10.0.5"
libraryDependencies +="com.typesafe.akka" %% "akka-http-spray-json" % "10.0.5"
libraryDependencies +="com.typesafe.akka" %% "akka-http-jackson" % "10.0.5"
libraryDependencies +="com.typesafe.akka" %% "akka-http-xml" % "10.0.5"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.0.0"
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"