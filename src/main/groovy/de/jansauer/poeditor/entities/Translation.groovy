package de.jansauer.poeditor.entities

class Translation implements Serializable {
  final String lang
  final String type
  final String file

  Translation(params) {
    this.lang = params.get('lang', 'en')
    this.type = params.get('type', 'xtb')
    this.file = params.file
  }
}