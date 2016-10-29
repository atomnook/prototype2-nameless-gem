val confFilePath = file(sys.props.get("config.file").getOrElse("conf/local.conf")).getAbsolutePath

val defaultSettings = Seq(
  scalaVersion := "2.11.8",
  fork in Test := true,
  javaOptions in Test += s"-Dconfig.file=$confFilePath")

lazy val redis = (project in file("redis")).
  settings(
    defaultSettings,
    libraryDependencies ++= Seq(
      "redis.clients" % "jedis" % "2.9.0",
      "com.typesafe" % "config" % "1.3.1",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test"))

lazy val model = (project in file("model")).
  settings(
    defaultSettings,
    PB.targets in Compile := Seq(scalapb.gen(flatPackage = true, singleLineToString = true, grpc = false) ->
      (sourceManaged in Compile).value),
    libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.42" % "protobuf")
