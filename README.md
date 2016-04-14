process-runner
==============

A simple Scala library for no-fuss process runnning.


Installation
------------
To include process-runner in your project, add the following line to your `build.sbt`:
```
libraryDependencies += "com.ferega.procrun" %% "processrunner" % "0.1.5"
```
It is published for Scala `2.11` and Scala `2.10`.


Features
--------
  * Uses `java.lang.ProcessBuilder` in the background
  * Process managment is asynchronous
  * Collects process output (both stdout and stderr), which can be fetched during execution
  * Processes can be destroyed
  * Can wait for a process to finish (with a timeout)


Examples
--------
Listing folder contents:
```
// Runs ls, in folder specified by `folder`, and immediately returns its
// output.
import com.ferega.procrun._

def listFolder(folder: File) =
  new ProcessRunner("folder listing", folder, "ls").end
```

Generating a calendar:
```
// Runs cal with specified year and month and returns generated calendar if
// successful (from std out). Otherwise, returns error description (from
// std err).
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
