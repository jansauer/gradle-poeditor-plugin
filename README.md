# Gradle POEditor Plugin

Gradle plugin to manage translations easily within a [POEditor](https://poeditor.com) project. The plugin enables you 
to upload and download terms and respectively translations via gradle tasks.

## Getting Started

Add this snippet to yout build script.

```
plugins {
  id 'de.jansauer.poeditor:1.0.0'
}

poeditor {
  apiKey = 'd41d8cd98f00b204e9800998ecf8427e'
  projectId = '12345'

  terms lang: 'en', file: 'messages.xmb'
  trans lang: 'de', file: 'build/translations_de.xtb'
  trans lang: 'it', file: 'build/translations_it.xtb'
}
```

**Upload terms and translations:**

```
$ ./gradlew poeditorPush
```

**Download translations:**

```
$ ./gradlew poeditorPull
```

## Contributing

Pull requests are always welcome. I'm grateful for any help or inspiration.

## License and Authors

Author: Jan Sauer
<[jan@jansauer.de](mailto:jan@jansauer.de)>
([https://jansauer.de](https://jansauer.de))

```text
Copyright 2018, Jan Sauer <jan@jansauer.de> (https://jansauer.de)

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
