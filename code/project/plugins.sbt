// +-------------------------------------------------------------------------------------+
// | Dependency graph SBT plugin (https://github.com/jrudolph/sbt-dependency-graph)      |
// | Lists all library dependencies in a nicely formatted way for easy inspection.       |
// +-------------------------------------------------------------------------------------+
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

// +-------------------------------------------------------------------------------------+
// | SBT PGP Plugin (http://www.scala-sbt.org/sbt-pgp/)                                  |
// | Provided PGP signing for published artifacts.                                       |
// +-------------------------------------------------------------------------------------+
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
