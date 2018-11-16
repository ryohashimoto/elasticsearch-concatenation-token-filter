# Elasticsearch concatenation token filter

## About

Token filter plug-in for combining tokens in Elasticsearh.

## Build

Get the source code from the repository.

```
git clone https://github.com/ryohashimoto/elasticsearch-concatenation-token-filter.git
```

To check out the tag for Elasticsearch 6.5.0

```
git checkout 6.5.0.0
```

To create the package.

```
mvn package
```

The package will be built under `target/releases` directory.

## Install

To install the plugin on Elasticsearch, execute the following command.

```
elasticsearch-plugin install file://(Plug-in file path)
```

## Using filter in analyzer

You can use it by specifying `concatenation` in the analyzer setting of Elasticsearch.

An example is shown below.

```
"readingform_search_analyzer": {
  "type": "custom",
  "char_filter": [
    "normalize",
    "whitespaces",
    "katakana",
    "romaji"
  ],
  "tokenizer": "japanese_normal",
  "filter": [
    "lowercase",
    "trim",
    "readingform",
    "concatenation",
    "maxlength",
    "asciifolding"
   ]
}
```
