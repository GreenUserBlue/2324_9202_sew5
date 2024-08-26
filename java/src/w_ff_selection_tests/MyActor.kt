package w_ff_selection_tests

import com.fourinachamber.fortyfive.keyInput.selection.SelectionGroup

class MyActor(
    override var group: SelectionGroup?,
    var x: Double,
    var y: Double,
) : Actor(x, y), FocusableActor {
    override var isFocusable: Boolean = true
    override var isFocused: Boolean = false
    override var isSelected: Boolean = false
    override fun toString(): String {
        return "MyActor(x=$x, y=$y, isFocused=$isFocused)"
    }
}