package com.github.ktj.katajaintellijplugin.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class KatajaConfigurationFactory(type: ConfigurationType): ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration = KatajaRunConfiguration(project, this, "Kataja")

    override fun getId(): String = "KatajaRunConfigurationType"

    override fun getOptionsClass(): Class<out BaseState> = KatajaRunConfigurationOptions::class.java
}