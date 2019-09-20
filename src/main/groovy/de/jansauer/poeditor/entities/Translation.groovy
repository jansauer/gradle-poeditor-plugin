package de.jansauer.poeditor.entities

class Translation implements Serializable {
  final String lang
  final String type
  final String file
  final List<String> tags

  Translation(params) {
    this.lang = params.get('lang', 'en')
    this.type = params.get('type', 'xtb')
    this.file = params.file
    this.tags = params.get('tags', [])
  }
}