package cms.rendner.intellij.python.intentions

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl
import com.jetbrains.python.psi.LanguageLevel

/**
 * @author Daniel Schmidt
 */
abstract class PyIntentionTestCase : BasePlatformTestCase() {

    private var requestedLanguageLevel: LanguageLevel = LanguageLevel.PYTHON27

    fun doTest(hint: String, languageLevel: LanguageLevel) {
        runWithLanguageLevel(languageLevel) {
            val testFileName = getTestName(true)
            myFixture.configureByFile("$testFileName.py")
            myFixture.launchAction(myFixture.findSingleIntention(hint))
            myFixture.checkResultByFile(testFileName + "_after.py", true)
        }
    }

    protected open fun doNegativeTest(hint: String, languageLevel: LanguageLevel) {
        runWithLanguageLevel(languageLevel) {
            val testFileName = getTestName(true)
            myFixture.configureByFile("$testFileName.py")
            assertEmpty(myFixture.filterAvailableIntentions(hint))
        }
    }

    private fun runWithLanguageLevel(languageLevel: LanguageLevel, runnable: () -> Unit) {
        requestedLanguageLevel = languageLevel
        try {
            runnable.invoke()
        } finally {
            requestedLanguageLevel = LanguageLevel.PYTHON27
        }
    }

    override fun createTempDirTestFixture(): TempDirTestFixture {
        return object : LightTempDirTestFixtureImpl(true) {
            override fun createFile(targetPath: String): VirtualFile {
                /*
                This is a hack to set the expected language level.

                The language level in "PyPrefixFStringIntention" is determined via "LanguageLevel.forElement(psiElement)".
                Somewhere down the road "PyUtil.getLanguageLevelForVirtualFile(this.getProject(), virtualFile)" is called
                which fetches the language level from the parent of "virtualFile" via "getUserData(LanguageLevel.KEY)".
                The "virtualFile" is the file which is created in this method.
                 */
                val file = super.createFile(targetPath)
                file.parent.putUserData(LanguageLevel.KEY, requestedLanguageLevel)
                return file
            }
        }
    }
}