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
git checkout 6.5.0.1
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

## License

```
Copyright 2018 Ryo Hashimoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
