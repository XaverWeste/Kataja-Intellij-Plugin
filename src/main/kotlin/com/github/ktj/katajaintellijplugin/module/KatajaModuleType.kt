package com.github.ktj.katajaintellijplugin.module

import com.github.ktj.katajaintellijplugin.language.KatajaIcons
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import javax.swing.Icon

class KatajaModuleType : ModuleType<KatajaModuleBuilder>("KATAJA_MODULE_TYPE") {

    override fun createModuleBuilder(): KatajaModuleBuilder = KatajaModuleBuilder()

    override fun getName(): String = "Kataja"

    override fun getDescription(): String = "Kataja module Type"

    override fun getNodeIcon(p0: Boolean): Icon = KatajaIcons.FILE

    override fun createWizardSteps(
        wizardContext: WizardContext,
        moduleBuilder: KatajaModuleBuilder,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> = ModuleWizardStep.EMPTY_ARRAY
}