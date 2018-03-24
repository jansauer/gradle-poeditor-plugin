package de.jansauer.poeditor

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Path

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class POEditorPluginTest extends Specification {

  @Rule
  final TemporaryFolder testProjectDir = new TemporaryFolder()

  String tocken
  File buildFile
  File termsFile
  Path buildFolder

  def setup() {
    tocken = System.env.PO_TOKEN
    testProjectDir.create()
    buildFile = testProjectDir.newFile('build.gradle')
    buildFolder = testProjectDir.newFolder('build').toPath()
    termsFile = testProjectDir.newFile('build/messages.xmb')
  }

  def "should upload terms and download translations"() {
    given:
    buildFile << """
        plugins {
          id 'de.jansauer.poeditor'
        }

        poeditor {
          apiKey = '${tocken}'
          projectId = '171275'

          terms lang: 'en', file: 'build/messages.xmb'
          
          trans lang: 'de', file: 'build/translations_de.xtb'
          trans lang: 'it', file: 'build/translations_it.xtb'
        }
    """
    termsFile << new File('src/test/resources/messages.xmb').text

    when: "should push terms"
    def result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withArguments('--build-cache', 'poeditorPush')
        .withPluginClasspath()
        .forwardOutput()
        .withDebug(true)
        .build()

    then:
    result.task(':poeditorPush').outcome == SUCCESS

    when: "should pull translations"
    result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withArguments('--build-cache', 'poeditorPull')
        .withPluginClasspath()
        .forwardOutput()
        .withDebug(true)
        .build()

    then:
    result.task(':poeditorPull').outcome == SUCCESS

    when: "check pulled translation fiel content"
    def de = buildFolder.resolve('translations_de.xtb').toFile().text

    then:
    de.contains('Träumer')

    when: "should be up to date when pushing without changes"
    result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withArguments('--build-cache', 'poeditorPush')
        .withPluginClasspath()
        .forwardOutput()
        .withDebug(true)
        .build()

    then:
    result.task(':poeditorPush').outcome == UP_TO_DATE

    when: "should be up to date when pulling without changes"
    result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withArguments('--build-cache', 'poeditorPull')
        .withPluginClasspath()
        .forwardOutput()
        .withDebug(true)
        .build()

    then:
    result.task(':poeditorPull').outcome == UP_TO_DATE

    where:
    gradleVersion << ['4.5', '4.6']
  }

  def cleanup() {
    sleep(30000)  // poeditor api is rate limited
  }
}
