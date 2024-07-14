package com.github.ktj.katajaintellijplugin.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.modules
import com.intellij.openapi.roots.ModuleRootManager
import com.jetbrains.rd.generator.nova.PredefinedType
import java.io.File
import java.net.URLClassLoader

class KatajaRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?): RunConfigurationBase<KatajaRunConfigurationOptions>(project, factory, name) {
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return object : CommandLineState(environment) {
            override fun startProcess(): ProcessHandler {
                val commandLine = GeneralCommandLine()
                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }
        }
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = KatajaSettingsEditor()

    override fun getOptions(): KatajaRunConfigurationOptions = super.getOptions() as KatajaRunConfigurationOptions

    override fun checkConfiguration() {
        if(options.getMain() == "") throw RuntimeConfigurationWarning("Main File is not specified and will be searched automatically")
        else{
            val file = File(options.getMain())
            if(file.extension != "ktj") throw RuntimeConfigurationError("Expected kataja file")
            if(!file.absolutePath.startsWith(project.basePath!!)) throw RuntimeConfigurationError("Main is out of Project")
        }

        if(options.getCompiler() == "") throw RuntimeConfigurationError("Compiler is not defined")
        else{
            val file = File(options.getCompiler())
            if(file.extension != "jar") throw RuntimeConfigurationError("Expected jar file")
            try{
                val clazz = URLClassLoader.newInstance(arrayOf(file.parentFile.toURI().toURL())).loadClass("com.github.ktj.compiler.Compiler")
                //if(clazz.getMethod("validate", PredefinedType.int.javaClass).invoke(null, -2060307643) != -1925309133) throw RuntimeConfigurationError("Failed to validate Compiler")
            }catch(ignored: ClassNotFoundException){
                throw RuntimeConfigurationError("Failed to load Compiler")
            }
        }
    }

    fun getMain(): String = options.getMain()

    fun setMain(main: String) = options.setMain(main)

    fun getCompiler(): String = options.getCompiler()

    fun setCompiler(compiler: String) = options.setCompiler(compiler)

    fun getOut(): String = options.getOut()

    fun setOut(out: String) = options.setOut(out)
}