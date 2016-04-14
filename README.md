process-runner
==============
A simple Scala library for no-fuss process runnning.


Installation
------------
To include _process-runner_ in your project, add the following line to your `build.sbt`:

```scala
libraryDependencies += "com.ferega.procrun" %% "processrunner" % "0.1.5"
```

It is published for Scala `2.11` and `2.10`.


Features
--------
  * Uses `java.lang.ProcessBuilder` in the background
  * Process managment is asynchronous
  * Collects process output (both `stdout` and `stderr`), which can be fetched at any time during or after execution
  * Provides an easy way to write to process `stdin`
  * Processes can be destroyed
  * Can wait for a process to finish (with a timeout)


Examples
--------
Listing folder contents by specifying a working directory (Linux):

```scala
// Runs `ls`, in the specified folder, and immediately returns its output.
import com.ferega.procrun._

def listFolder(folder: File): String =
  new ProcessRunner("folder listing", folder, "ls").end.stdOut
```


Listing folder contents by passing the directory as an argument (Linux):

```scala
// Runs `ls`, with a single argument, and immediately returns its output.
import com.ferega.procrun._

def listFolder(folder: File): String =
  new ProcessRunner("folder listing", "ls", Seq(folder.getAbsolutePath)).end.stdOut
```


Generating a calendar (Linux):

```scala
// Runs `cal` with specified year and month and returns generated calendar if
// successful (from stdout). Otherwise, returns error description (from stderr).
import com.ferega.procrun._

def getCalendar(year: Int, month: Int): String = {
  val result = new ProcessRunner("calendar", "cal", "-m" | month | year).end
  if (result.exitCode == 0) {
    result.stdOut.trim
  } else {
    "Error: " + result.stdErr.split("\n")(0)  // Just the first line of output
  }
}
```


Running `cmd` and executing a specified command by writing to `stdin` (Windows):

```scala
  import com.ferega.procrun._

  def runCommand(command: String): String = {
    val p = new ProcessRunner("command executor", "cmd").run
    p.writeToStdIn(command + "\r\n")
    p.end.stdOut
  }
```