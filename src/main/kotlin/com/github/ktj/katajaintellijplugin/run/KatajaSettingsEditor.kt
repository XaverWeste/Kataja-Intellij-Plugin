package com.github.ktj.katajaintellijplugin.run

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class KatajaSettingsEditor: SettingsEditor<KatajaRunConfiguration>() {

    private var panel: JPanel
    private var mainPathField: TextFieldWithBrowseButton
    private var compilerPathField: TextFieldWithBrowseButton
    private var outPathField: TextFieldWithBrowseButton

    init{
        mainPathField = TextFieldWithBrowseButton()
        mainPathField.addBrowseFolderListener("Select Main File", null, null, FileChooserDescriptorFactory.createSingleFileDescriptor())
        compilerPathField = TextFieldWithBrowseButton()
        compilerPathField.addBrowseFolderListener("Select Compiler", null, null, FileChooserDescriptorFactory.createSingleFileDescriptor())
        outPathField = TextFieldWithBrowseButton()
        outPathField.addBrowseFolderListener("Select Out Folder", null, null, FileChooserDescriptorFactory.createSingleFileDescriptor())
        panel = FormBuilder.createFormBuilder().addLabeledComponent("Main File:", mainPathField).addLabeledComponent("Compiler:", compilerPathField).addLabeledComponent("Out Folder:", outPathField).panel
    }

    override fun resetEditorFrom(configuration: KatajaRunConfiguration) {
        mainPathField.text = configuration.getMain()
        compilerPathField.text = configuration.getCompiler()
        outPathField.text = configuration.getOut()
    }

    override fun applyEditorTo(configuration: KatajaRunConfiguration){
        configuration.setMain(mainPathField.text)
        configuration.setCompiler(compilerPathField.text)
        configuration.setOut(outPathField.text)
    }

    override fun createEditor(): JComponent = panel
}