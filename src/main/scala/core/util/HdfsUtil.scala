package core.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}

class HdfsUtil(hdfsUri: String) {
  def getConfiguration = {
    val conf = new Configuration()
    conf.set("fs.default.name", hdfsUri)
    //disable caching to avoid java.io.IOException: Filesystem closed
    conf.setBoolean("fs.hdfs.impl.disable.cache", true)
    conf
  }

  def copyFromLocal (source: String, dest: String) = {
    val conf = getConfiguration
    val fileSystem = FileSystem.get(conf)
    val (srcPath, dstPath) = new Path(source) -> new Path(dest)
    if (fileSystem.exists(dstPath)) {
      println(s"${dstPath} exists!")
      delFromHdfs(source)
    }

    // Get the filename out of the file path
    val filename = source.substring(source.lastIndexOf('/') + 1, source.length())

    fileSystem.copyFromLocalFile(srcPath, dstPath)
    println(s"File ${filename} (local) copied to ${dest} (hdfs)")
    fileSystem.close()
  }

  def copyFromHdfs (source: String, dest: String) = {
    val conf = getConfiguration
    val fileSystem = FileSystem.get(conf)
    val (srcPath, dstPath) = new Path(source) -> new Path(dest)
    if (fileSystem.exists(dstPath)) println(s"${dstPath} exists!")

    // Get the filename out of the file path
    val filename = source.substring(source.lastIndexOf('/') + 1, source.length())

    fileSystem.copyToLocalFile(srcPath, dstPath)
    println(s"File ${filename} (hdfs) copied to ${dest} (local)")
    fileSystem.close()
  }

  def delFromHdfs(path: String) = {
    val conf = getConfiguration

    val fileSystem = FileSystem.get(conf)
    fileSystem.delete(new Path(path), true)

    println(s"File ${path} (hdfs) deleted")
    fileSystem.close()
  }
}

object HdfsUtil {
  def apply(hdfsUri: String) = new HdfsUtil(hdfsUri)
}