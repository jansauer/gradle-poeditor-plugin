package de.jansauer.poeditor

import de.jansauer.poeditor.entities.Translation
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerConfiguration
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject
import java.nio.file.Paths

class PullTask extends DefaultTask {

  @Input
  final Property<String> apiKey = project.objects.property(String)

  @Input
  final Property<String> projectId = project.objects.property(String)

  @Input
  final ListProperty<Translation> trans = project.objects.listProperty(Translation)

  @Internal
  final WorkerExecutor workerExecutor;

  @Inject
  PullTask(WorkerExecutor workerExecutor) {
    this.workerExecutor = workerExecutor

    setDescription('Download translations from POEditor.')
    setGroup('poeditor')
  }

  @OutputFiles
  ConfigurableFileCollection getOutOfDateOutputs() {
    return project.files(*trans.get().collect {Paths.get(it.file)})
  }

  @TaskAction
  def pullTranslations() {
    // @See https://poeditor.com/docs/api#projects_export
    logger.debug("Pulling '{}' sets of translations", trans.get().size())

    trans.get().each {
      this.workerExecutor.submit(PullRunnable.class, new Action<WorkerConfiguration>() {
        @Override
        void execute(WorkerConfiguration config) {
          config.setIsolationMode(IsolationMode.NONE)
          config.params(apiKey.get(), projectId.get(), it.lang, it.type, project.file(it.file), it.tags)
        }
      })
    }
  }
}
