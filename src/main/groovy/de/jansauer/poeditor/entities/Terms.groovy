package de.jansauer.poeditor.entities

class Terms implements Serializable {
  final String updating
  final String file
  final String lang
  final boolean overwrite
  final boolean sync_terms

  Terms(params) {
    this.updating = params.get('updating', 'terms')
    this.file = params.file
    this.lang = params.get('lang', 'en')
    this.overwrite = params.get('overwrite', false)
    this.sync_terms = params.get('sync_terms', false)
  }
}