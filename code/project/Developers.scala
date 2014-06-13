object Developers {
  lazy val members = Map(
    "tferega" -> "Tomo Ferega"
  )

  def toXml =
    <developers>
      {members map { case (nick, name) =>
        <developer>
          <id>{ nick }</id>
          <name>{ name }</name>
          <url>https://github.com/{ nick }</url>
        </developer>
      }}
    </developers>
}
