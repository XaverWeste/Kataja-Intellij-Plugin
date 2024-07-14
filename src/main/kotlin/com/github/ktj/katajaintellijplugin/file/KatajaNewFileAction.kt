package com.github.ktj.katajaintellijplugin.file

import com.github.ktj.katajaintellijplugin.KatajaIcons
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.actions.CreateFromTemplateAction
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

class KatajaNewFileAction: CreateFromTemplateAction<PsiFile>("Kataja File", "new Kataja File", KatajaIcons.FILE) {
    override fun createFile(name: String?, templateName: String?, dir: PsiDirectory?): PsiFile? {
        val project = dir?.project ?: return null
        val template = FileTemplateManager.getInstance(project).getInternalTemplate(templateName ?: return null)
        return try {
            val fileName = "$name.ktj"
            FileTemplateUtil.createFromTemplate(template, fileName, null, dir) as PsiFile
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun buildDialog(project: Project, dir: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder){
        builder.setTitle("New Kataja File")
            .addKind("File", KatajaIcons.FILE, "Kataja File")
            .addKind("Class", AllIcons.Nodes.Class, "Kataja Class")
            .addKind("Interface", AllIcons.Nodes.Interface, "Kataja Interface")
            .addKind("Data", AllIcons.Nodes.AnonymousClass, "Kataja Data")
    }

    override fun getActionName(dir: PsiDirectory?, newName: String, templateName: String?): String {
        return "Creating $newName"
    }
}