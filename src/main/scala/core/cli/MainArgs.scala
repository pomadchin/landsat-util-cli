package core.cli

import geotrellis.proj4.CRS

import org.joda.time.DateTime

case class MainArgs(
  polygon: String = "",
  startDate: String = "",
  endDate: String = "",
  cloudCoverage: Double = 80d,
  bands: Seq[String] = Nil,
  output: String = "",
  multiband: Boolean = false,
  threads: Int = 1,
  crs: String = ""
) {
  def getStartDate = DateTime.parse(startDate)
  def getEndDate   = DateTime.parse(endDate)
  def getCrs       = if(crs.nonEmpty) Some(CRS.fromName(crs)) else None
}
