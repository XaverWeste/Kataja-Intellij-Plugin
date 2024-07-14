package com.github.ktj.katajaintellijplugin.module

import com.github.ktj.katajaintellijplugin.KatajaIcons
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import javax.swing.Icon

class KatajaModuleBuilder: ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> = ModuleTypeManager.getInstance().findByID("KATAJA_MODULE_TYPE") as KatajaModuleType

    override fun getNodeIcon(): Icon = KatajaIcons.FILE
}