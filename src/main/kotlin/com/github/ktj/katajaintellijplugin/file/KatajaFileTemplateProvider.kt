package com.github.ktj.katajaintellijplugin.file

import com.github.ktj.katajaintellijplugin.KatajaIcons
import com.intellij.icons.AllIcons
import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory

class KatajaFileTemplateProvider:FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor("Kataja File", KatajaIcons.FILE)
        group.addTemplate(FileTemplateDescriptor("Kataja File", KatajaIcons.FILE))
        group.addTemplate(FileTemplateDescriptor("Kataja Class", AllIcons.Nodes.Class))
        group.addTemplate(FileTemplateDescriptor("Kataja Interface", AllIcons.Nodes.Interface))
        group.addTemplate(FileTemplateDescriptor("Kataja Data", AllIcons.Nodes.AnonymousClass))
        return group
    }
}