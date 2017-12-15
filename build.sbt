import _root_.sbtassembly.AssemblyPlugin.autoImport._

name := "HBaseExample"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Apache Repository" at "https://repository.apache.org/content/repositories/releases/",
  "Cloudera repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)

libraryDependencies ++= Seq(
  "io.spray" %% "spray-json" % "1.3.2",
  "org.apache.hbase" % "hbase-server" % "1.2.0-cdh5.12.1",
  "org.apache.hbase" % "hbase-client" % "1.2.0-cdh5.12.1",
  "org.apache.hbase" % "hbase-common" % "1.2.0-cdh5.12.1",
  "org.apache.hadoop" % "hadoop-common" % "2.6.0-cdh5.12.1"

)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { mergeStrategy => {
  case entry => {
    val strategy = mergeStrategy(entry)
    if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
    else strategy
  }
}
}
    