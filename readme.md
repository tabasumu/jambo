<!-- markdownlint-configure-file {
  "MD013": {
    "code_blocks": false,
    "tables": false
  },
  "MD033": false,
  "MD041": false
} -->

<div align="center">

<p align="center"><img width=12.5% src="/images/logo.svg"></p>

# Jambo

![GitHub Repo stars](https://img.shields.io:/github/stars/MamboBryan/poetree?style=for-the-badge) ![GitHub branch checks state](https://img.shields.io:/github/checks-status/MamboBryan/poetree/develop?style=for-the-badge)


Jambo is an open source remote **logging library**. <br/>
For those who would like to see their logs remotely on their android device Jambo is the library for you. Jambo installs a seperate debug app for intercepting all Jambo 

[Installation](#installation) •
[Usage](#usage) •
[Configuration](#configuration) •
[Contributing](#contributing)

</div>

## Installation

### Kotlin DSL

```kotlin
dependency {
  implementation( "com.somefancycommunityname.jambo:$version")
} 
```

### Groovy
```groovy
dependency {
  implementation 'com.somefancycommunityname.jambo:$version'
} 
```

<br/>

## Usage

- Initialize Jambo in the App module

### Kotlin
```kotlin
class App : Application {

    Jambo.Builder()
        .enableDebugVillage()      // required
        .enableNotifications(true) // not required & false by default 
        .build()

}
```

### Java
```java
class App extends Application {

    Jambo.Builder()
        .enableDebugVillage()      // required
        .enableNotifications(true) // not required & false by default 
        .build()

}
```
<br/>

## Contributing

![GitHub tag (latest by date)](https://img.shields.io:/github/v/tag/MamboBryan/jambo?style=for-the-badge)
![GitHub contributors](https://img.shields.io:/github/contributors/MamboBryan/jambo?style=for-the-badge) ![GitHub last commit](https://img.shields.io:/github/last-commit/MamboBryan/jambo?style=for-the-badge) [![Good first issues](https://img.shields.io/github/issues/MamboBryan/jambo/good%20first%20issue?style=for-the-badge)](https://github.com/MamboBryan/jambo/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22) ![GitHub](https://img.shields.io:/github/license/MamboBryan/jambo?style=for-the-badge) ![GitHub issues](https://img.shields.io:/github/issues-raw/MamboBryan/jambo?style=for-the-badge) ![GitHub pull requests](https://img.shields.io:/github/issues-pr/MamboBryan/jambo?style=for-the-badge) 

Your contributions are especially welcome.
Whether it comes in the form of code patches, ideas, discussion, bug reports, encouragement or criticism, your input is needed.

Visit [issues](https://github.com/MamboBryan/jambo/issues) to get started.

<br/>

## Credits
[Timber](https://github.com/JakeWharton/timber)