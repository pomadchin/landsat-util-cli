package core.cli

import org.joda.time.DateTime

case class MainArgs(
  polygon: String = "",
  startDate: String = "",
  endDate: String = "",
  cloudCoverage: Double = 80d,
  bands: Seq[String] = Nil,
  output: String = "",
  multiband: Boolean = false,
  hdfs: String = "",
  hdfsOutput: String = ""
) {
  def getStartDate = DateTime.parse(startDate)
  def getEndDate = DateTime.parse(endDate)
  def copyToHdfs = hdfs.nonEmpty && hdfsOutput.nonEmpty
}
