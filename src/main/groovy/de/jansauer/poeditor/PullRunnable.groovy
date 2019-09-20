package de.jansauer.poeditor

import groovyx.net.http.FromServer
import groovyx.net.http.OkHttpEncoders
import groovyx.net.http.optional.Download
import org.gradle.api.GradleException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject

import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.MultipartContent.multipart

class PullRunnable implements Runnable {
  private final String apiKey
  private final String projectId
  private final String lang
  private final String type
  private final File file
  private final List<String> tags

  private final Logger logger = LoggerFactory.getLogger(PullRunnable.class)

  @Inject
  PullRunnable(String apiKey, String projectId, String lang, String type, File file, List<String> tags) {
    this.apiKey = apiKey
    this.projectId = projectId
    this.lang = lang
    this.type = type
    this.file = file
    this.tags = tags
  }

  @Override
  void run() {
    logger.info("Pulling language '{}' from project '{}'", lang, projectId)

    def result = configure {
      request.uri = 'https://api.poeditor.com/v2/projects/export'
    }.post {
      request.contentType = 'multipart/form-data'
      request.body = multipart {
        field 'api_token', apiKey
        field 'id', projectId
        field 'language', lang
        field 'type', type

        tags.forEach { tag ->
          field 'tags', tag
        }
      }
      request.encoder 'multipart/form-data', OkHttpEncoders.&multipart

      response.exception { exception ->
        logger.error("{}", exception.printStackTrace())
        throw new GradleException(exception as String)
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
