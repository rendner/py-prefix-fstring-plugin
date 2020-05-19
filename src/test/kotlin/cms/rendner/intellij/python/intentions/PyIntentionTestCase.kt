package cms.rendner.intellij.python.intentions

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.python.psi.LanguageLevel

/**
 * @author Daniel Schmidt
 */
abstract class PyIntentionTestCase : BasePlatformTestCase() {

    fun doTest(hint: String, languageLevel: LanguageLevel) {
        val testFileName = getTestName(true)
        val file = myFixture.configureByFile("$testFileName.py")
        injectLanguageLevel(file, languageLevel)
        myFixture.launchAction(myFixture.findSingleIntention(hint))
        myFixture.checkResultByFile(testFileName + "_after.py", true)
    }

    protected open fun doNegativeTest(hint: String, languageLevel: LanguageLevel) {
        val testFileName = getTestName(true)
        val file = myFixture.configureByFile("$testFileName.py")
        injectLanguageLevel(file, languageLevel)
        assertEmpty(myFixture.filterAvailableIntentions(hint))
    }

    private fun injectLanguageLevel(file: PsiFile, languageLevel: LanguageLevel) {
        /*
        This is a hack to set the expected language level.

        The language level in "PyPrefixFStringIntention" is determined via "LanguageLevel.forElement(psiElement)".
        Somewhere down the road "PyUtil.getLanguageLevelForVirtualFile(this.getProject(), virtualFile)" is called
        which fetches the language level from the parent of "virtualFile" via "getUserData(LanguageLevel.KEY)".
         */
        file.virtualFile.parent.putUserData(LanguageLevel.KEY, languageLevel)
    }
}