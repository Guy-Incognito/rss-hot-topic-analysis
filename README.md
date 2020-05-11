# rss-hot-topic-analysis

[![Build Status](https://travis-ci.com/Guy-Incognito/rss-hot-topic-analysis.svg?branch=master)](https://travis-ci.com/Guy-Incognito/rss-hot-topic-analysis)

## Docker

Image is pushed to dockerhub automatically.

### Run
Run prebuilt image with docker:
```
docker run --rm -p 8080:8080 georgmoser/rss-hot-topic-analysis 
```

### Build
```
docker build . -t rss-hot-topic-analysis   
```

## OpenApi Doc:
```
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
```

