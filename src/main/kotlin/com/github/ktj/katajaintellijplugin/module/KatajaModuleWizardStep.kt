package com.github.ktj.katajaintellijplugin.module

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import javax.swing.JComponent
import javax.swing.JPanel

class KatajaModuleWizardStep: ModuleWizardStep() {
    override fun getComponent(): JComponent = JPanel()

    override fun updateDataModel() {

    }
}