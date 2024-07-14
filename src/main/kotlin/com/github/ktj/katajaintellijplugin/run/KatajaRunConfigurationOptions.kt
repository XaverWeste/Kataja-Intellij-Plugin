package com.github.ktj.katajaintellijplugin.run

import com.intellij.execution.configurations.RunConfigurationOptions

class KatajaRunConfigurationOptions: RunConfigurationOptions() {

    private val main = string("").provideDelegate(this, "main")
    private val compiler = string("").provideDelegate(this, "compiler")
    private val out = string("").provideDelegate(this, "out")

    fun getMain(): String = main.getValue(this)!!
    fun setMain(path: String) = main.setValue(this, path)

    fun getCompiler(): String = compiler.getValue(this)!!
    fun setCompiler(path: String) = compiler.setValue(this, path)

    fun getOut(): String = out.getValue(this)!!
    fun setOut(path: String) = out.setValue(this, path)
}