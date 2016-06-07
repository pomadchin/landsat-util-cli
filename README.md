# [Landsat Util](github.com/azavea/scala-landsat-util) CLI

Simple run:

```bash
java -jar ${PWD}/target/scala-2.10/landsat-util-cli-assembly-0.1.0.jar \
     --threads 4 \
     --polygon $PWD/data/philly.json \
     --startDate 2014-01-01 \
     --endDate 2015-01-01 \
     --bands 4 \
     --output /data/philly 
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
        
  --help
        prints this usage text
```

## License

* Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0