import sbt._

object Dependencies {

  lazy val akka: ModuleID = "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0"

  lazy val akkaStream: ModuleID = "com.typesafe.akka" %% "akka-stream" % "2.8.0"

  lazy val akkaHttp: ModuleID = "com.typesafe.akka" %% "akka-http" % "10.5.0"

  lazy val akkaHttpSprayJson: ModuleID = "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0"

  lazy val ditto: ModuleID = "org.eclipse.ditto" % "ditto-client" % "3.2.0"

  lazy val postgresql: ModuleID = "org.postgresql" % "postgresql" % "42.6.0"

  lazy val quill: ModuleID = "io.getquill" %% "quill-jdbc" % "4.6.0.1"
}
