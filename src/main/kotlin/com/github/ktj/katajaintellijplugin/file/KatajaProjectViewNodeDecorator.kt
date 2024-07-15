package com.github.ktj.katajaintellijplugin.file

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.openapi.vfs.VirtualFile

class KatajaProjectViewNodeDecorator: ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>?, data: PresentationData?) {
        if(node == null || data == null) return

        if(node.value is KatajaFile && isInKatajaFolder(node.value as KatajaFile)) data.presentableText = (node.value as KatajaFile).virtualFile.nameWithoutExtension
    }

    private fun isInKatajaFolder(file: KatajaFile): Boolean{
        var folder: VirtualFile? = file.virtualFile.parent
        while(folder != null){
            if(folder.name == "kataja" || folder.name == "src") return true
            folder = folder.parent
        }
        return false
    }
}