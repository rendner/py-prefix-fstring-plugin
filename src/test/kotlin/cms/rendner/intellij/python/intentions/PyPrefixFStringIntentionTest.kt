package cms.rendner.intellij.python.intentions

import com.jetbrains.python.intentions.PyIntentionTestCase
import com.jetbrains.python.psi.LanguageLevel

/**
 * @author Daniel Schmidt
 */
class PyPrefixFStringIntentionTest : PyIntentionTestCase() {

    // todo: fix import of base class
    // todo: fix path to test data

    fun testOlderThanPython36() = doTestWithPythonOlderThan36()

    fun testByteString() = doNegativeTest()
    fun testCaretBeforeString() = doNegativeTest()
    fun testDocstring() = doNegativeTest()
    fun testFString() = doNegativeTest()
    fun testNotTerminated() = doNegativeTest()
    fun testNoString() = doNegativeTest()
    fun testUnicodeString() = doNegativeTest()

    fun testAdjacent() = doTest()
    fun testBinaryExpressionLHS() = doTest()
    fun testBinaryExpressionRHS() = doTest()
    fun testCaretAfterString() = doTest()
    fun testCaretSomewhereInsideString() = doTest()
    fun testEmptyString() = doTest()
    fun testJoinedLines() = doTest()
    fun testNestedFString() = doTest()
    fun testRawString() = doTest()
    fun testSingleQuotes() = doTest()
    fun testTripleQuotes() = doTest()

    private fun doTest() = doTest(getHint(), LanguageLevel.PYTHON36)
    private fun doNegativeTest() = runWithLanguageLevel(LanguageLevel.PYTHON36) { doNegativeTest(getHint()) }
    private fun doTestWithPythonOlderThan36() = runWithLanguageLevel(LanguageLevel.PYTHON35) { doNegativeTest(getHint()) }
    private fun getHint() = "Prefix string with 'f' to make it an f-string"
}