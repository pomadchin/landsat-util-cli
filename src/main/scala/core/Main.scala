package core

import geotrellis.raster._
import com.azavea.landsatutil._
import core.cli.MainOptions
import core.util.HdfsUtil
import geotrellis.vector.Polygon
import geotrellis.vector.io._
import geotrellis.vector.io.json.GeoJson
import geotrellis.raster.io.geotiff._
import org.apache.spark.{SparkConf, SparkContext}

object Main {
  def main(args: Array[String]): Unit = {
    MainOptions.parse(args) match {
      case Some(config) => {
        val sc = new SparkContext(
          new SparkConf()
            .setAppName("GeoTrellis LandsatUtil")
            .setJars(SparkContext.jarOfObject(this).toList)
        )

        lazy val hdfsUtil = HdfsUtil(config.hdfs, sc.hadoopConfiguration)

        Landsat8Query()
          .withStartDate(config.getStartDate)
          .withEndDate(config.getEndDate)
          .withMaxCloudCoverage(config.cloudCoverage)
          .intersects(GeoJson.fromFile[Polygon](config.polygon))
          .collect()
          .foreach { img =>
            val lr =
              if(img.imageExistsS3()) img.getFromS3(config.bands)
              else img.getFromGoogle(config.bands)
            val raster = lr.raster
            if(config.multiband)
              GeoTiff(raster.raster, raster.crs).write(s"${config.output}/B_${config.bands.mkString("")}.tif")
            else
              raster.raster.bands.zip(config.bands).foreach { case (tile, i) =>
                GeoTiff(Raster(tile, raster.extent), raster.crs).write(s"${config.output}/B_${i}.tif")
              }

            if(config.copyToHdfs) hdfsUtil.copyFromLocal(config.output, config.hdfsOutput)
          }

        sc.stop()
      }
      case None => throw new Exception("No valid arguments passed")
    }
  }
}
