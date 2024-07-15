package com.github.ktj.katajaintellijplugin.language

import com.intellij.openapi.projectRoots.*
import org.jdom.Element

class KatajaSdkType: SdkType("KATAJA_SDK_TYPE") {
    override fun saveAdditionalData(data: SdkAdditionalData, element: Element) {

    }

    override fun suggestHomePath(): String? = null

    override fun isValidSdkHome(path: String): Boolean = false

    override fun suggestSdkName(name: String?, home: String): String = "Kataja SDK"

    override fun createAdditionalDataConfigurable(model: SdkModel, modificator: SdkModificator): AdditionalDataConfigurable? {
        TODO("Not yet implemented")
    }

    override fun getPresentableName(): String = "Kataja SDK"

    override fun setupSdkPaths(sdk: Sdk, sdkModel: SdkModel): Boolean {
        val modificator = sdk.sdkModificator
        modificator.versionString = getVersionString(sdk)
        modificator.commitChanges()
        return true
    }
}