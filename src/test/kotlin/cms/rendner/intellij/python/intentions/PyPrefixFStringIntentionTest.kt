package cms.rendner.intellij.python.intentions

import com.jetbrains.python.psi.LanguageLevel
import java.nio.file.Paths

/**
 * @author Daniel Schmidt
 */
class PyPrefixFStringIntentionTest : PyIntentionTestCase() {

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
    private fun doNegativeTest() = doNegativeTest(getHint(), LanguageLevel.PYTHON36)
    private fun doTestWithPythonOlderThan36() = doNegativeTest(getHint(), LanguageLevel.PYTHON35)

    private fun getHint() = "Prefix string with 'f' to make it an f-string"

    override fun getTestDataPath(): String {
        return Paths.get("src", "test", "resources", "files").toFile().absolutePath
    }
}