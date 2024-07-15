package com.github.ktj.katajaintellijplugin.module

import com.github.ktj.katajaintellijplugin.language.KatajaIcons
import com.intellij.ide.util.projectWizard.*
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.testFramework.utils.vfs.createDirectory
import javax.swing.Icon

class KatajaModuleBuilder: ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> = ModuleTypeManager.getInstance().findByID("KATAJA_MODULE") as KatajaModuleType

    override fun setupRootModel(model: ModifiableRootModel){
        doAddContentEntry(model)!!.addSourceFolder(model.project.baseDir.createDirectory(""), false)
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep = KatajaModuleWizardStep()

    override fun getNodeIcon(): Icon = KatajaIcons.FILE
}