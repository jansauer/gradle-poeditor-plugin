package de.jansauer.poeditor

import org.gradle.api.Plugin
import org.gradle.api.Project

class POEditorPlugin implements Plugin<Project> {

  void apply(Project target) {
    def extension = target.extensions.create('poeditor', POEditorExtension, target)

    target.tasks.create('poeditorPush', PushTask) {
      apiKey = extension.apiKey
      projectId = extension.projectId
      terms = extension.terms
    }
    target.tasks.create('poeditorPull', PullTask) {
      apiKey = extension.apiKey
      projectId = extension.projectId
      trans = extension.trans
    }
  }
}
