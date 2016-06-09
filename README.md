# [Landsat Util](github.com/azavea/scala-landsat-util) CLI

Simple run:

```bash
java -jar ${PWD}/target/scala-2.10/landsat-util-cli-assembly-0.1.0.jar \
     --threads 4 \
     --polygon ${PWD}/data/rnd.json \
     --startDate 2015-363 \
     --endDate 2015-363 \
     --bands 4 \
     --crs EPSG:3857 \
     --output /data/rnd 
```

## Build

To build you can just run 

```bash
./sbt assembly
```

## Args

```bash
landsat-util-cli 0.1.0
Usage: landsat-util-cli [options]

  --polygon <value>
          polygon is a non-empty String property, path to json file
    --startDate <value>
          startDate is a non-empty String property
    --endDate <value>
          endDate is a non-empty String property
    --cloudCoverage <value>
          
    --bands <value>
          bands is a non-empty String property
    --output <value>
          output is a non-empty String property
    --multiband <value>
          multiband is boolean property, to save as batch of singleband tiles or as a multiband tile
    --threads <value>
          threads is an int property, specify threads number, every scene processes in one thread
    --crs <value>
          crs is a string property, specify destination crs to reproject landsat tiles
    --split <value>
          split is a boolean property, if true than splits landsat scene into smaller tiles
    --layoutCols <value>
          layoutCols is an int property, layout cols for a chunk of a splited landsat tile
    --layoutRows <value>
          layoutRows is an int property, layout rows for a chunk of a splited landsat tile
    --tileCols <value>
          tileCols is an int property, tile cols for a chunk of a splited landsat tile
    --tileRows <value>
          tileRows is an int property, tile rows for a chunk of a splited landsat tile
    --help
          prints this usage text
```

## License

* Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0