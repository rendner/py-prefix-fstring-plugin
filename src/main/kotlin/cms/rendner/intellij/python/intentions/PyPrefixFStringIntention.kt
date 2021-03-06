package cms.rendner.intellij.python.intentions

import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.codeInsight.intentions.PyBaseIntentionAction
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyStringElement

/**
 * Prefixes a string literal with "f" to make it an f-string literal (for Python versions >= 3.6).
 *
 * @author Daniel Schmidt
 */
class PyPrefixFStringIntention : PyBaseIntentionAction(), HighPriorityAction {

    init {
        text = "Prefix string with 'f' to make it an f-string"
    }

    override fun getFamilyName(): String = text

    override fun doInvoke(project: Project, editor: Editor, file: PsiFile) {
        finStringElementAtCaret(file, editor)?.apply {
            val expression = PyElementGenerator.getInstance(project).createStringLiteralAlreadyEscaped("f${text}")
            replace(expression.stringElements.first())
        }
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        if (file !is PyFile) return false
        return finStringElementAtCaret(file, editor) != null
    }

    private fun finStringElementAtCaret(file: PsiFile, editor: Editor): PyStringElement? {
        // offset - 1: to allow prefixing the following case -> 'age: {x}'<caret>
        return file.findElementAt(editor.caretModel.offset - 1).let {
            if ((it !is PyStringElement) ||
                    (!it.isTerminated) ||
                    (PsiUtilCore.getElementType(it) == PyTokenTypes.DOCSTRING) ||
                    (it.isFormatted || it.isUnicode || it.isBytes) ||
                    (LanguageLevel.forElement(it).isOlderThan(LanguageLevel.PYTHON36))) null
            else it
        }
    }
}