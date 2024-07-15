package com.github.ktj.katajaintellijplugin.run

import com.github.ktj.katajaintellijplugin.language.KatajaIcons
import com.intellij.execution.configurations.ConfigurationTypeBase

class KatajaRunConfigurationType: ConfigurationTypeBase("KatajaRunConfigurationType", "Kataja", "Kataja run configuration type", KatajaIcons.FILE) {

    init{
        addFactory(KatajaConfigurationFactory(this))
    }
}