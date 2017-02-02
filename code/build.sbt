name := "ProcessRunner"

organization := "com.ferega.procrun"

version := "0.1.6"

scalaVersion := "2.12.1"

scalacOptions := Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-unchecked",
  "-Xcheckinit",
  "-Xlint",
  "-Xno-uescape",
  "-Xverify"
)

libraryDependencies := Seq(
  "joda-time" % "joda-time"    % "2.9.7",
  "org.joda"  % "joda-convert" % "1.8.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test := Nil

// Publishing

publishTo := Some(
  if (version.value endsWith "SNAPSHOT") {
    Opts.resolver.sonatypeSnapshots
  } else {
    Opts.resolver.sonatypeStaging
  }
)

crossScalaVersions      := Seq("2.11.8", "2.10.6")

publishMavenStyle       := true

publishArtifact in Test := false

pomIncludeRepository    := { _ => false }

licenses                += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage                := Some(url("https://github.com/tferega/process-runner/"))

credentials             += Credentials(Path.userHome / ".config" / "tferega.credentials")

startYear               := Some(2013)

scmInfo                 := Some(ScmInfo(url(s"https://github.com/tferega/process-runner/tree/${version.value}"), "scm:git:https://github.com/tferega/process-runner.git"))

pomExtra                ~= (_ ++ {Developers.toXml})
