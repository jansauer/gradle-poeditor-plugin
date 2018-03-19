package de.jansauer.poeditor.entities

class Terms implements Serializable {
  final String lang
  final String file

  Terms(params) {
    this.lang = params.lang
    this.file = params.file
  }
}