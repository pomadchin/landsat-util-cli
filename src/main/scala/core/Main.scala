package core

import geotrellis.raster._
import com.azavea.landsatutil._
import core.cli.MainOptions
import core.util.HdfsUtil
import geotrellis.vector.Polygon
import geotrellis.vector.io._
import geotrellis.vector.io.json.GeoJson
import geotrellis.raster.io.geotiff._

object Main {
  def main(args: Array[String]): Unit = {
    MainOptions.parse(args) match {
      case Some(config) => {
        lazy val hdfsUtil = HdfsUtil(config.hdfs)

        Landsat8Query()
          .withStartDate(config.getStartDate)
          .withEndDate(config.getEndDate)
          .withMaxCloudCoverage(config.cloudCoverage)
          .intersects(GeoJson.fromFile[Polygon](config.polygon))
          .collect()
          .filter(_.imageExistsS3())
          .foreach { img =>
            val lr = img.getFromS3(config.bands)
            val raster = lr.raster
            if(config.multiband)
              GeoTiff(raster.raster, raster.crs).write(config.output)
            else
              raster.raster.bands.map { tile =>
                GeoTiff(Raster(tile, raster.extent), raster.crs).write(config.output)
              }

            if(config.copyToHdfs) hdfsUtil.copyFromLocal(config.output, config.hdfsOutput)
          }
      }
      case None => throw new Exception("No valid arguments passed")
    }
  }
}
