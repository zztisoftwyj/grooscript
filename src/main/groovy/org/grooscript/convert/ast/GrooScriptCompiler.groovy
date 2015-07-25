package org.grooscript.convert.ast

import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.CompilerConfiguration
import org.grooscript.util.GrooScriptException

import static org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder.withConfig

/**
 * Created by jorgefrancoleza on 14/2/15.
 */
class GrooScriptCompiler {

    private static int GROOSCRIPT_PHASE = CompilePhase.SEMANTIC_ANALYSIS.phaseNumber

    def classpath
    Closure customization

    protected CompilationUnit compiledCode(
            String sourceCode, String scriptClassName = defaultScriptName, int phaseNumber = GROOSCRIPT_PHASE) {
        def conf = getCustomizedCompilerConfiguration()
        def compilationUnitFinal = new CompilationUnit(conf, null, grooscriptClassLoader(conf))
        compilationUnitFinal.addSource(scriptClassName, sourceCode)
        compilationUnitFinal.compile(phaseNumber)
        compilationUnitFinal
    }

    protected String getDefaultScriptName() {
        'script' + System.currentTimeMillis()
    }

    private CompilerConfiguration getCustomizedCompilerConfiguration() {
        CompilerConfiguration conf = new CompilerConfiguration()
        //Add customization to configuration
        if (customization) {
            withConfig(conf, customization)
        }
    }

    private GroovyClassLoader grooscriptClassLoader(CompilerConfiguration conf) {
        def parent = new GroovyClassLoader()
        addClassPathToGroovyClassLoader(parent)
        GroovyClassLoader classLoader = new GroovyClassLoader(parent, conf)
        addClassPathToGroovyClassLoader(classLoader)
        classLoader
    }

    private void addClassPathToGroovyClassLoader(GroovyClassLoader classLoader) {
        if (classpath) {
            if (!(classpath instanceof String || classpath instanceof GString || classpath instanceof Collection)) {
                throw new GrooScriptException('The classpath must be a String or a List')
            }

            if (classpath instanceof Collection) {
                classpath.each {
                    classLoader.addClasspath(it)
                }
            } else {
                classLoader.addClasspath(classpath)
            }
        }
    }
}