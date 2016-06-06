name := "landsat-util-cli"
version := "0.1.0"
scalaVersion := "2.10.6"
crossScalaVersions := Seq("2.11.8", "2.10.6")
organization := "com.azavea"
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Yinline-warnings",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:existentials",
  "-feature")
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

resolvers ++= Seq(Resolver.bintrayRepo("azavea", "geotrellis"), Resolver.bintrayRepo("azavea", "maven"))

libraryDependencies ++= Seq(
  "com.github.scopt"      %% "scopt"              % "3.4.0",
  "com.azavea.geotrellis" %% "geotrellis-spark"   % "0.10.0",
  "org.apache.spark"      %% "spark-core"         % "1.5.2",
  "org.apache.hadoop"      % "hadoop-client"      % "2.7.1",
  "com.azavea"            %% "scala-landsat-util" % "0.2.0-32180dc",
  "org.scalatest"         %%  "scalatest"         % "2.2.0" % "test"
)

sourceGenerators in Compile <+= (sourceManaged in Compile, version, name) map { (d, v, n) =>
  val file = d / "core/cli/Info.scala"
  IO.write(file, """package core.cli
                   |object Info {
                   |  val version = "%s"
                   |  val name    = "%s"
                   |}
                   |""".stripMargin.format(v, n))
  Seq(file)
}

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case "reference.conf" => MergeStrategy.concat
  case "application.conf" => MergeStrategy.concat
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case "META-INF\\MANIFEST.MF" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.SF" => MergeStrategy.discard
  case _ => MergeStrategy.first
}
