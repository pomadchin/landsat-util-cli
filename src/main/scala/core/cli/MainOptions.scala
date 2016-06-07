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
    }

    help("help") text "prints this usage text"
  }

  def parse(args: Array[String]) = parser.parse(args, MainArgs())
}