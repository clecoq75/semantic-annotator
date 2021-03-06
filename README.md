[![Build Status](https://travis-ci.org/clecoq75/semantic-annotator.svg?branch=master)](https://travis-ci.org/clecoq75/semantic-annotator)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=cle.nlp:semantic-annotator&metric=bugs)](https://sonarcloud.io/dashboard?id=cle.nlp%3Asemantic-annotator)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=cle.nlp:semantic-annotator&metric=coverage)](https://sonarcloud.io/dashboard?id=cle.nlp%3Asemantic-annotator)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=cle.nlp:semantic-annotator&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=cle.nlp%3Asemantic-annotator)
[![Reliability](https://sonarcloud.io/api/project_badges/measure?project=cle.nlp:semantic-annotator&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=cle.nlp%3Asemantic-annotator)
[![Security](https://sonarcloud.io/api/project_badges/measure?project=cle.nlp:semantic-annotator&metric=security_rating)](https://sonarcloud.io/dashboard?id=cle.nlp%3Asemantic-annotator)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=cle.nlp:semantic-annotator&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=cle.nlp%3Asemantic-annotator)

# Semantic Annotator

`Semantice Annotator` is a library that generates semantic annotations 
based on a system of `syntactic patterns`. It rely on the [Stanford CoreNLP](http://stanfordnlp.github.io/CoreNLP/) library.

## Taggers

A `tagger` is a single json file which describe the way to extract annotations 
on a text using a list of `rules` and a list of test to validate the `tagger`. 

`Semantice Annotator` will load all `taggers` contained in given directory and validate
all rules and unit tests on load. Then it will be able to generate the annotations
defined by those `taggers`.
 
A tagger can import other tagger which will be executed before its own rules. But only generatedTagLabels flagged as exportable in the tagger itself will be returned.

```json
{
  "importRules": [ "otherTagger1", "otherTagger2" ],
  "rules": []
}
```

## Collections

A `collection` is a single json file listing other `taggers`. 

```json
{
  "collection": [
    "otherTagger1",
    "otherTagger2"
  ],
  "unitTests": [
    {
      "verbatim": [ "My cat is red", "Your dog is blue" ],
      "generatedTagLabels": [ "coloredPet" ]
    }
  ]
}
```

## Rules

A `rule` describe the way to detect an annotation based on a `syntactic pattern` 
(or regular expression) and/or to apply transformations on the input text.

A list of `samples` allow to validate the `rule`.

For instance, the following rule will replace the first matching group with "SMALL".

```json
{
  "pattern": "(petit@ADJ|mini|minuscule) :NC",
  "substitutions": "1:SMALL",
  "samples": [ "le petit lavabo" ]
}
```

This second rule will generate the annotation "smallDog" if the `syntactic pattern` matches.

```json
{
  "pattern": "@SMALL chien",
  "generatedTagLabels": [ { "value": "smallDog", "exported": true } ],
  "samples": [ "ce petit chien", "le petit chien" ]
}
```

#### Token Patterns

A `token pattern` describe a single token (aka 'word').
Its syntax is : 
> *text*__@__*type*[__;__*property*__=__*value*]\*
>
> where :
> - *text* : the exact text value or its lemma
> - *type* : the "part of speech" label of the token such as noun, verb, adjective, etc. It use the Treebank POS tag set.
> - *property*__=__*value* : a list of morphosyntactic properties separated by a semicolon.

Any part of a `token pattern` can be empty.

You can combine multiple patterns by separating them by the boolean operator | (*or*).

##### *Examples :*

| token pattern | will match tokens |
| --- | --- |
| samples@NN;lemma=sample;nb=p | samples |
| @NN;lemma=sample;nb=p | samples |
| samples;lemma=sample;nb=p | samples |
| samples;lemma=sample | samples or sample |
| samples@V | *none* |
| samples@ | samples or sample |
| @ | *any token* |
| samples@&#124;example:NN | samples, sample, example or examples |

### Syntactic Patterns

A `syntactic pattern` describe the syntactic structure of a text. It is composed of a sequence of `token pattern`.

You can define groups using parenthesis. It is useful for applying substitutions or to apply a quantifier to it.

Quantifiers are :
* ? : None or once
* \* : any
* \+ : Once or many

##### *Examples :*

| syntactic pattern | will match text |
| --- | --- |
| @DT (@JJ)? dog@NN | the dog, the big dog, a small dog, etc. |
| (@DT)* dog@NN | dog, the dog, the the dog, etc. |

### Substitutions

The "substitutions" member of a rule allow to replace a matching group by a given tag.

> The syntax is : *index*__:__*value*
> 
> Where :
> - *groupIndex* : the group index to replace (starting at 1)
> - *value* : the value which will replace the specified group

##### *Examples :*

| syntactic pattern | substitutions | text | result |
| --- | --- | --- | --- |
| @DT (dog@NN&#124;cat@NN) | 1:Pet | the dog | the *Pet* |
| (hello@&#124;hi) (@NNP) | 1:HI,2:WHO | Hello Bryan | *HI* *WHO* |

## The "Semantic Annotator Console"

This tool allow you to test and debug you taggers. This is a command line interface which can be used
with any text editor to validate your `tagger` files.

##### *Rules validation 'on the fly'*

Each time you save a file, the `Semantic Annotator Console` will validate its content and
will display debug information in case of error. [Watch the video](https://youtu.be/LR7MsUaMjaA).

##### *Use substitutions*

This [video](https://youtu.be/atdXk_A1wbw) describe the way to create substitutions.

##### *Use substitutions*

This [video](https://youtu.be/9UPJ7QMegLo) describe the way to use a shared `tagger`.

##### *Dependencies validation*

Each time you save a file, the `Semantic Annotator Console` also validates all `tagger`
which depends on it to check if your modifications does not introduce regressions. [Watch the video](https://youtu.be/4ArdsY7GjIY).

##### *Regexp validation*

Rules based on regular expressions are also validated. [Watch the video](https://youtu.be/nspbeOHv9mg)

##### *Short text validation*

You can test your `taggers` easily. [Watch the video](https://youtu.be/yKuLjzt5cok)

##### *Long text validation*

This feature allow you to run all your `taggers` on a large text file (a book for instance). It is
really useful to detect invalid annotations. [Watch the video](https://youtu.be/yiNFWylRMyc)

##### *Run the console with MAVEN:*

If this is the first time you run the console :

`mvn -Dmaven.test.skip=true -pl '!semantic-annotator-console-delivery' clean install`

Then :

`mvn exec:java -pl semantic-annotator-console`


## How to embed the "Semantic Annotator" in your code :
```java
import cle.nlp.annotator.SemanticAnnotator;
import cle.nlp.tagger.Tag;

public class App {
    public static void main(String[] args) {
        SemanticAnnotator annotator = new SemanticAnnotator(SupportedLanguages.FR, "/path/dir");
        Collection<Tag> generatedTagLabels = annotator.getTags("this is a text"); 
    }
}
```
