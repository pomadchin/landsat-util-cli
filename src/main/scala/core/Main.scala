package core

import core.cli.MainOptions
import com.azavea.landsatutil._
import geotrellis.raster._
import geotrellis.vector.Polygon
import geotrellis.vector.io._
import geotrellis.vector.io.json.GeoJson
import geotrellis.raster.io.geotiff._

import scala.concurrent.{ExecutionContext, Future}
import java.util.concurrent.Executors

object Main {
  def main(args: Array[String]): Unit = {
    MainOptions.parse(args) match {
      case Some(config) => {
        val executor = Executors.newFixedThreadPool(config.threads)
        implicit val executionContext = ExecutionContext.fromExecutor(executor)

        Future.sequence(Landsat8Query()
          .withStartDate(config.getStartDate)
          .withEndDate(config.getEndDate)
          .withMaxCloudCoverage(config.cloudCoverage)
          .intersects(GeoJson.fromFile[Polygon](config.polygon))
          .collect()
          .map { img => Future {
            val lr =
              if(img.imageExistsS3()) img.getFromS3(config.bands)
              else img.getFromGoogle(config.bands)
            val raster = lr.raster
            if(config.multiband)
              GeoTiff(raster.raster, raster.crs).write(s"${config.output}/${img.sceneId}_B_${config.bands.mkString("")}.tif")
            else
              raster.raster.bands.zip(config.bands).foreach { case (tile, i) =>
                GeoTiff(Raster(tile, raster.extent), raster.crs).write(s"${config.output}/${img.sceneId}_B_${i}.tif")
              }
          } }) onComplete {
          case _ => executor.shutdown()
        }
      }
      case None => throw new Exception("No valid arguments passed")
    }
  }
}
