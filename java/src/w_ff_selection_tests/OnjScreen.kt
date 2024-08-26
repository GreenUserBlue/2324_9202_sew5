package w_ff_selection_tests

import com.fourinachamber.fortyfive.keyInput.selection.FocusableParent

class OnjScreen {
    val screenState: MutableList<String> = mutableListOf("myScreenstate")

    val actors = mutableListOf<Actor>()

    var focusedActor: FocusableActor? = null
    val selectionHierarchy: ArrayDeque<FocusableParent> = ArrayDeque()
    fun getFocusableActors(): MutableList<FocusableActor> {
        return actors.filterIsInstance<FocusableActor>().filter { it.group != null }.toMutableList()
    }

    fun focusNext(direction: Vector2? = null) {
        if (selectionHierarchy.isEmpty()) return
        val focusableElement = selectionHierarchy.last().focusNext(direction, this)
        focusedActor?.let { it.onFocusChange(it, focusableElement) }
        focusableElement?.let { it.onFocusChange(it, focusableElement) }
        println("newElement: $focusableElement")
        this.focusedActor = focusableElement
    }

    fun focusPrevious() {
        if (selectionHierarchy.isEmpty()) return
        val focusableElement = selectionHierarchy.last().focusPrevious(this)
        focusedActor?.let { it.onFocusChange(it, focusableElement) }
        focusableElement?.let { it.onFocusChange(it, focusableElement) }
        this.focusedActor = focusableElement
    }
}