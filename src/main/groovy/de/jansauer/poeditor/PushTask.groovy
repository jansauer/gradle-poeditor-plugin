package de.jansauer.poeditor

import de.jansauer.poeditor.entities.Terms
import groovyx.net.http.FromServer
import groovyx.net.http.OkHttpEncoders
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.MultipartContent.multipart

class PushTask extends DefaultTask {

  @Input
  final Property<String> apiKey = project.objects.property(String)

  @Input
  final Property<String> projectId = project.objects.property(String)

  @Input
  final ListProperty<Terms> terms = project.objects.listProperty(Terms)

  PushTask() {
    setDescription('Upload terms to POEditor.')
    setGroup('poeditor')
  }

  @TaskAction
  def pushTerms() {
    // @See https://poeditor.com/docs/api#projects_upload
    logger.debug("Pushing '{}' sets of terms", terms.get().size())

    terms.get().each {
      logger.debug("Pushing language '{}' from '{}' to project '{}'", it.lang, it.file, projectId.get())
      def lang = it.lang
      def termsFile = project.file(it.file)

      def result = configure {
        request.uri = 'https://api.poeditor.com/v2/projects/upload'
      }.post {
        request.contentType = 'multipart/form-data'
        request.body = multipart {
          field 'api_token', apiKey.get()
          field 'id', projectId.get()
          field 'updating', 'terms_translations'
          part 'file', termsFile.name, 'text/plain', termsFile
          field 'language', lang
          field 'sync_terms', '1'
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
        logger.info("{}", result.result)
      } else {
        logger.error("{} {}", result.response.code, result.response.message)
        throw new GradleException(result.response.message)
      }
    }
  }

  @InputFiles
  ConfigurableFileCollection getOutOfDateInputs() {
    return project.files(*terms.get().collect {Paths.get(it.file)})
  }

  @OutputFiles
  ConfigurableFileCollection getOutOfDateOutputs() {
    return project.files(*terms.get().collect {Paths.get(it.file)})
  }
}
