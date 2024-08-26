package w_ff_selection_tests

import com.fourinachamber.fortyfive.keyInput.selection.SelectionGroup

interface FocusableActor {

    fun onFocusChange(oldElement: FocusableActor?, newElement: FocusableActor?) {
        isFocused = this == newElement
    }

    fun onSelectChange(oldElements: List<FocusableActor>, newElements: List<FocusableActor>) {
        isSelected = this in newElements
    }

    var group: SelectionGroup?

    var isFocusable: Boolean
    var isFocused: Boolean
    var isSelected: Boolean

}