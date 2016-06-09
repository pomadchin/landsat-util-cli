package core.cli

import scopt.OptionParser

object MainOptions {

  val parser = new OptionParser[MainArgs](Info.name) {
    head(Info.name, Info.version)

    opt[String]("polygon") action { (x, c) =>
      c.copy(polygon = x)
    } validate { x =>
      if (x.nonEmpty) success else failure(s"Option --polygon must be non-empty")
    } text s"polygon is a non-empty String property, path to json file"

    opt[String]("startDate") action { (x, c) =>
      c.copy(startDate = x)
    } validate { x =>
      if (x.nonEmpty) success else failure(s"Option --startDate must be non-empty")
    } text s"startDate is a non-empty String property"

    opt[String]("endDate") action { (x, c) =>
      c.copy(endDate = x)
    } validate { x =>
      if (x.nonEmpty) success else failure(s"Option --endDate must be non-empty")
    } text s"endDate is a non-empty String property"

    opt[Double]("cloudCoverage") action { (x, c) =>
      c.copy(cloudCoverage = x)
    }

    opt[String]("bands") action { (x, c) =>
      c.copy(bands = x.split(","))
    } validate { x =>
      if (x.nonEmpty) success else failure(s"Option --bands must be non-empty")
    } text s"bands is a non-empty String property"

    opt[String]("output") action { (x, c) =>
      c.copy(output = x)
    } validate { x =>
      if (x.nonEmpty) success else failure(s"Option --output must be non-empty")
    } text s"output is a non-empty String property"

    opt[Boolean]("multiband") action { (x, c) =>
      c.copy(multiband = x)
    } text s"multiband is boolean property, to save as batch of singleband tiles or as a multiband tile"

    opt[Int]("threads") action { (x, c) =>
      c.copy(threads = x)
    } text s"threads is an int property, specify threads number, every scene processes in one thread"

    opt[String]("crs") action { (x, c) =>
      c.copy(crs = x)
    } text s"crs is a string property, specify destination crs to reproject landsat tiles"

    opt[Boolean]("split") action { (x, c) =>
      c.copy(split = x)
    } text s"split is a boolean property, if true than splits landsat scene into smaller tiles"

    opt[Int]("layoutCols") action { (x, c) =>
      c.copy(layoutCols = x)
    } text s"layoutCols is an int property, layout cols for a chunk of a splited landsat tile"

    opt[Int]("layoutRows") action { (x, c) =>
      c.copy(layoutRows = x)
    } text s"layoutRows is an int property, layout rows for a chunk of a splited landsat tile"

    opt[Int]("tileCols") action { (x, c) =>
      c.copy(tileCols = x)
    } text s"tileCols is an int property, tile cols for a chunk of a splited landsat tile"

    opt[Int]("tileRows") action { (x, c) =>
      c.copy(tileRows = x)
    } text s"tileRows is an int property, tile rows for a chunk of a splited landsat tile"

    help("help") text "prints this usage text"
  }

  def parse(args: Array[String]) = parser.parse(args, MainArgs())
}