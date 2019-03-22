# Gradle POEditor Plugin

Gradle plugin to manage translations easily within a [POEditor](https://poeditor.com) project. The 
plugin enables you to upload and download terms and respectively translations via gradle tasks.

## Getting Started

Add this snippet to your build script.

```
plugins {
  id 'de.jansauer.poeditor' version '1.0.0'
}

poeditor {
  apiKey = 'd41d8cd98f00b204e9800998ecf8427e'
  projectId = '12345'

  terms lang: 'en', file: 'messages.xmb'
  
  trans lang: 'de', file: 'build/translations_de.xtb'
  trans lang: 'it', file: 'build/translations_it.xtb'
}
```

## Tasks

* `poeditorPush` Upload terms and/or translations
* `poeditorPull` Download translations

## Configuration

```
poeditor {
  apiKey = 'd41d8cd98f00b204e9800998ecf8427e'
  projectId = '12345'

  terms lang: 'en', file: 'messages.xmb', updating: 'terms_translations'
  trans lang: 'it', file: 'build/translations_it.csv', type: 'csv'
}
```

* `apiKey`: Key for the authentication with the poeditor api.<br>
  Can be found at [My Account > API Access](https://poeditor.com/account/api)
* `projectId`: Id of poeditor project terms and translations are pulled from and pushed to.<br>
   Can also be found at [My Account > API Access](https://poeditor.com/account/api)
* `terms`: Can be used multiple times to define terms to push to poeditor.
  * `updating`: One of 'terms', 'terms_translations' or 'translations' (Default: 'terms')
  * `file`: Local file to be uploaded ([List of supported formats](https://poeditor.com/help/#SupportedFormats))
  * `lang`: The language code (Default: 'en')
  * `overwrite`: Set to true if you want to overwrite translations (Default: false)
  * `sync_terms`: Set to true if you want to sync your terms (terms that are 
    not found in the uploaded file will be deleted from project and the new 
    ones added). Ignored if updating is set to 'translations' (Default: false)
* `trans`: Can be used multiple times to define translations to pull from poeditor.
  * `lang`: The language code (Default: 'en')
  * `type`: File format (po, pot, mo, xls, csv, resw, resx, android_strings, apple_strings, xliff, 
     properties, key_value_json, json, xmb, xtb) (Default: 'xtb')
  * `file`: Location where the downloaded translations are stored.

## Tested Gradle Versions

4.10, 4.10.1, 4.10.2, 4.10.3, 5.1, 5.1.1, 5.2, 5.2.1, 5.3

* **Pre 4.10** does not work wel with JDK11 plugin builds
* **5.0** has a bug with manually added ListProperty ([#7961](https://github.com/gradle/gradle/issues/7961))

## Publishing Workflow

Every commit on this repository gets tested via [circleci](https://circleci.com/gh/jansauer/gradle-poeditor-plugin).
Commits that are tagged with a semantic version are also automatically published to the gradle 
plugin directory as a new version.

## Contributing

Pull requests are always welcome. I'm grateful for any help or inspiration.

## License and Authors

Author: Jan Sauer
<[jan@jansauer.de](mailto:jan@jansauer.de)>
([https://jansauer.de](https://jansauer.de))

```text
Copyright 2019, Jan Sauer <jan@jansauer.de> (https://jansauer.de)

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
