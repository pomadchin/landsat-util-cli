package core

import core.cli.MainOptions

import geotrellis.vector.Polygon
import geotrellis.vector.io.json.GeoJson
import geotrellis.raster.io.geotiff._
import geotrellis.vector.io._
import geotrellis.raster._
import geotrellis.raster.split._
import com.azavea.landsatutil._

import scala.concurrent.{ExecutionContext, Future}
import java.util.concurrent.Executors
import java.io.File


object Main {
  def main(args: Array[String]): Unit = {
    MainOptions.parse(args) match {
      case Some(config) => {
        val executor = Executors.newFixedThreadPool(config.threads)
        implicit val executionContext = ExecutionContext.fromExecutor(executor)
        val regexp = """^LC8(\d{6})(\d{4})(\d{3})(.)*""".r

        /** Transforms a collection of Landsat image descriptions into RDD of MultibandTiles.
          * Each landsat scene is downloaded, reprojected and then split into 256x256 chunks.
          * Chunking the scene allows for greater parallism and reduces memory pressure
          * produces by processing each partition.
          */

        Future.sequence(Landsat8Query()
          .withStartDate(config.getStartDate)
          .withEndDate(config.getEndDate)
          .withMaxCloudCoverage(config.cloudCoverage)
          .intersects(GeoJson.fromFile[Polygon](config.polygon))
          .collect()
          .map { img => Future {
            val output = regexp.findAllIn(img.sceneId).matchData.map(s => s"${config.output}/${s.group(2)}/${s.group(3)}").next
            new File(output).mkdirs

            val lr =
              if(img.imageExistsS3()) img.getFromS3(config.bands)
              else img.getFromGoogle(config.bands)

            val lrr = lr.raster
            val raster: ProjectedRaster[MultibandTile] = config.getCrs.fold(lrr)(lrr.reproject(_))

            (raster: Raster[MultibandTile])
              .split(TileLayout(31, 31, 256, 256), Split.Options(cropped = false, extend = false))
              .zipWithIndex foreach { case (chunk, k) =>
                val (r, rc) = Raster(chunk.tile, chunk.extent) -> raster.crs

                if (config.multiband)
                  GeoTiff(r, rc).write(s"${output}/${img.sceneId}_B_${config.bands.mkString("")}_${k}.tif")
                else
                  r.bands.zip(config.bands).foreach { case (tile, i) =>
                    GeoTiff(r, rc).write(s"${output}/${img.sceneId}_B_${i}_${k}.tif")
                  }
               }
          } }) onComplete {
          case _ => executor.shutdown()
        }
      }
      case None => throw new Exception("No valid arguments passed")
    }
  }
}
