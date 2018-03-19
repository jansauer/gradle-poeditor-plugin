package de.jansauer.poeditor

import de.jansauer.poeditor.entities.Translation
import groovyx.net.http.FromServer
import groovyx.net.http.OkHttpEncoders
import groovyx.net.http.optional.Download
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.MultipartContent.multipart

class PullTask extends DefaultTask {

  @Input
  final Property<String> apiKey = project.objects.property(String)

  @Input
  final Property<String> projectId = project.objects.property(String)

  @Input
  final ListProperty<Translation> trans = project.objects.listProperty(Translation)

  PullTask() {
    setDescription('Download translations from POEditor.')
    setGroup('poeditor')
  }

  @TaskAction
  def pullTranslations() {
    // @See https://poeditor.com/docs/api#projects_export
    logger.debug("Pulling '{}' sets of translations", trans.get().size())

    trans.get().each {
      logger.info("Pulling language '{}' from project '{}'", it.lang, projectId.get())
      def lang = it.lang
      def type = it.type
      def file = project.file(it.file)

      def result = configure {
        request.uri = 'https://api.poeditor.com/v2/projects/export'
      }.post {
        request.contentType = 'multipart/form-data'
        request.body = multipart {
          field 'api_token', apiKey.get()
          field 'id', projectId.get()
          field 'language', lang
          field 'type', type
        }
        request.encoder 'multipart/form-data', OkHttpEncoders.&multipart

        response.exception { exception ->
          logger.error("{}", exception.printStackTrace())
          throw new GradleException(exception)
        }

        response.failure { FromServer fs ->
          logger.error("{} {}", fs.message, fs.statusCode)
          throw new GradleException(fs.message)
        }
      }

      if (result.response.status == 'success') {
        logger.debug("Loading export from '{}'", result.result.url)
        configure {
          request.uri = result.result.url
        }.get {
          Download.toFile(delegate, file)
        }
      } else {
        logger.error("{} {}", result.response.code, result.response.message)
        throw new GradleException(result.response.message)
      }
    }
  }

  @OutputFiles
  ConfigurableFileCollection getOutOfDateOutputs() {
    return project.files(*trans.get().collect {Paths.get(it.file)})
  }
}
