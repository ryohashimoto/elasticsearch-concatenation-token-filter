# Elasticsearch concatenation token filter

## About

Elasticsearhで分割されたトークンを結合するためのフィルター(token filter)をプラグインにしたものです。

## Motivation

- kuromojiで形態素解析され、ローマ字に変換されたトークンを再度結合する必要があったため作成しました。
- kuromojiに関係なく、トークンを結合する用途で使用できます。
- 例えば、「脳梗塞」という単語を「脳こうそく」で検索しようとした場合に、前者は「nokosoku」、後者は「no」と「kosoku」のトークンに変換されます。
- あらかじめ、「nokosoku」でインデックスされていた場合、後者ではヒットしなくなります。
- このフィルターを用いることで、「脳こうそく」の場合も「nokosoku」のトークンに変換できるようになります。

## Build

リポジトリからソースコードを取得します。

```
git clone https://github.com/ryohashimoto/elasticsearch-concatenation-token-filter.git
```

Elasticsearch 6.5.0に対応したタグをチェックアウトする場合は、以下のようにします。

```
git checkout 6.5.0.0
```

以下の手順でパッケージの作成を行います。

```
mvn package
```

`target/releases`ディレクトリ以下にパッケージがビルドされれば成功です。

## Install

Elasticsearchにプラグインをインストールするには、以下のコマンドを実行します。

```
elasticsearch-plugin install file://(プラグインファイルのパス)
```

## Using filter in analyzer

Elasticsearchのアナライザの設定で`concatenation`を指定すれば、使用できるようになります。

以下に例を示します。

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
