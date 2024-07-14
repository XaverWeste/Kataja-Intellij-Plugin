package com.github.ktj.katajaintellijplugin.module

import com.github.ktj.katajaintellijplugin.KatajaIcons
import com.intellij.openapi.module.ModuleType
import javax.swing.Icon

class KatajaModuleType : ModuleType<KatajaModuleBuilder>("KATAJA_MODULE_TYPE") {

    override fun createModuleBuilder(): KatajaModuleBuilder = KatajaModuleBuilder()

    override fun getName(): String = "Kataja"

    override fun getDescription(): String = "Kataja module Type"

    override fun getNodeIcon(p0: Boolean): Icon = KatajaIcons.FILE
}