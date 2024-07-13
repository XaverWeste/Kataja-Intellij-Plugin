package com.github.ktj.katajaintellijplugin

import com.intellij.icons.AllIcons
import com.intellij.ide.FileIconProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import javax.swing.Icon

class KatajaFileIconProvider: FileIconProvider {
    override fun getIcon(file: VirtualFile, flags: Int, project: Project?): Icon? {
        if(!file.extension.equals("ktj")) return null

        val fileContent = PsiManager.getInstance(project!!).findFile(file)?.text
        var type = 0

        if(fileContent!!.contains("class")){
            if(fileContent.split("class").size - 1 == 1){
                type = if (fileContent.split("abstract").size - 1 == 1) 5
                else 1
            }else return KatajaIcons.FILE
        }

        if(fileContent.contains("type")) {
            if(fileContent.split("type").size - 1 == 1){
                if (type == 0) type = 2
                else return KatajaIcons.FILE
            }else return KatajaIcons.FILE
        }

        if(fileContent.contains("data")) {
            if(fileContent.split("data").size - 1 == 1){
                if(type == 0) type = 3
                else return KatajaIcons.FILE
            }else return KatajaIcons.FILE
        }

        if(fileContent.contains("interface")) {
            if(fileContent.split("interface").size - 1 == 1){
                if(type == 0) type = 4
                else return KatajaIcons.FILE
            }else return KatajaIcons.FILE
        }

        return when(type){
            0 -> AllIcons.Nodes.Static
            1 -> AllIcons.Nodes.Class
            2 -> AllIcons.Nodes.Type
            3 -> AllIcons.Nodes.AnonymousClass
            4 -> AllIcons.Nodes.Interface
            5 -> AllIcons.Nodes.AbstractClass
            else -> KatajaIcons.FILE
        }
    }
}