package de.jansauer.poeditor

import de.jansauer.poeditor.entities.Terms
import de.jansauer.poeditor.entities.Translation
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

class POEditorExtension {

  final Property<String> apiKey
  final Property<String> projectId
  final ListProperty<Terms> terms
  final ListProperty<Translation> trans

  POEditorExtension(Project project) {
    apiKey = project.objects.property(String)
    projectId = project.objects.property(String)
    terms = project.objects.listProperty(Terms)
    trans = project.objects.listProperty(Translation)
  }

  void terms(params) {
    this.terms.add(new Terms(params))
  }

  void trans(params) {
    this.trans.add(new Translation(params))
  }
}


