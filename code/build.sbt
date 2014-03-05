name := "ProcessRunner"

organization := "com.ferega"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.2"

autoScalaLibrary := false

scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-optimise",
    "-encoding", "UTF-8",
    // "-explaintypes",
    "-Xcheckinit",
    // "-Xfatal-warnings",
    "-Yclosure-elim",
    "-Ydead-code",
    // "-Yinline",
    // "-Yinline-warnings",
    "-Xmax-classfile-name", "72",
    "-Yrepl-sync",
    "-Xlint",
    "-Xverify",
    "-Ywarn-all",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials")

libraryDependencies := Seq(
  "joda-time"      % "joda-time"      % "2.2",
  "org.joda"       % "joda-convert"   % "1.3.1",
  "org.scala-lang" % "scala-library"  % "2.10.2")

unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(_ :: Nil)

unmanagedSourceDirectories in Test := Nil



// Publishing

credentials += Credentials(Path.userHome / ".config" / "process-runner" / "nexus.config")

crossScalaVersions := Seq("2.10.2", "2.10.1", "2.10.0")

publishArtifact in (Compile, packageDoc) := false

publishTo <<= (version) { version =>
    Some(
      if (version endsWith "SNAPSHOT")
        "Element Snapshots" at "http://repo.element.hr/nexus/content/repositories/snapshots/"
      else
        "Element Releases" at "http://repo.element.hr/nexus/content/repositories/releases/"
    )
  }
