package org.grooscript

import org.grooscript.test.TestJs
import spock.lang.Specification

/**
 * First test for converts groovy code to javascript code
 * Following GroovyInAction Book
 * Chap 2. Groovy basics
 * JFL 27/08/12
 */
class TestAdvancedClass extends Specification {

    def converter = new GsConverter()

    def readAndConvert(nameOfFile,consoleOutput) {

        def file = TestJs.getGroovyTestScript(nameOfFile)

        //Added this conversion option
        converter.addClassNames = true

        def jsScript = converter.toJs(file.text)

        if (consoleOutput) {
            println 'jsScript->\n'+jsScript
        }

        return TestJs.jsEval(jsScript)
    }

    def 'test class names' () {
        when:
        def result = readAndConvert('classes/Names',false)

        then:
        !result.assertFails
    }

    def 'test instanceof basic'() {
        when:
        def result = readAndConvert('classes/InstanceOf',false)

        then:
        !result.assertFails
    }

}
