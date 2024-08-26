package com.fourinachamber.fortyfive.keyInput.selection

import w_ff_selection_tests.Actor
import w_ff_selection_tests.FocusableActor
import w_ff_selection_tests.OnjScreen
import w_ff_selection_tests.SelectionTests
import w_ff_selection_tests.Vector2
import java.lang.Math.pow
import kotlin.math.*

typealias SelectionGroup = String

class FocusableParent(
    private val transitions: List<SelectionTransition>,
    var onLeave: () -> Unit = {},
    private val startGroup: SelectionGroup? = null,
    groups: List<SelectionGroup> = listOf(),
    var onSelection: (List<FocusableActor>) -> Unit,
) {
    private val groups: Set<SelectionGroup>

    init {
        val allGroups = mutableSetOf<SelectionGroup>()
        transitions.forEach { allGroups.addAll(it.groups) }
        allGroups.addAll(groups)
        this.groups = allGroups
    }

    private var focusableActors: Map<SelectionGroup, List<FocusableActor>> = mutableMapOf()

    fun updateFocusableActors(screen: OnjScreen) {
        val actors = screen.getFocusableActors()
        val focusableActors =
            mutableMapOf<SelectionGroup, MutableList<FocusableActor>>()
        actors
            .filter { it2 -> it2.group in groups }
            .forEach {
                focusableActors.putIfAbsent(it.group!!, mutableListOf())
                focusableActors[it.group]!!.add(it)
            }
        this.focusableActors = focusableActors
    }

    private fun getFocusablePrioritised(
        curActor: FocusableActor,
        screen: OnjScreen
    ): Map<SelectionTransition.TransitionType, List<FocusableActor>> {
        val res: MutableMap<SelectionTransition.TransitionType, MutableList<FocusableActor>> =
            mutableMapOf<SelectionTransition.TransitionType, MutableList<FocusableActor>>()
        val group = curActor.group ?: throw RuntimeException("actor $curActor should have never been selected")
        transitions.forEach {
            if (it.condition.check(screen)) {
                if (group in it.groups) {
                    it.groups.forEach { gr2 ->
                        focusableActors[gr2]?.let { it1 ->
                            run {
                                res.putIfAbsent(it.type, mutableListOf())
                                res[it.type]!!.addAll(it1)
                            }
                        }
                    }
                }
            }
        }
        return res
    }

    fun focusNext(direction: Vector2?, screen: OnjScreen): FocusableActor? {
        val oldFocusedActor = screen.focusedActor
        if (oldFocusedActor == null || oldFocusedActor !is Actor) {
            return if (startGroup == null || focusableActors[startGroup]?.isNotEmpty() != true) {
                getFirstFocused(focusableActors.values.flatten()) as FocusableActor?
            } else {
                getFirstFocused(focusableActors[startGroup]!!) as FocusableActor?
            }
        }
        if (direction == null) return focusNext(screen)
        val oldPos = Vector2(oldFocusedActor.x+oldFocusedActor.width/2,oldFocusedActor.y+oldFocusedActor.height/2)
        val polarDir = toPolarCoords(direction)
        val prios = getFocusablePrioritised(oldFocusedActor, screen)
        val newActor= focusableActors
            .values
            .flatten()
            .filterIsInstance<Actor>()
            .filter { it!=oldFocusedActor }
            .map {
                val curPolar = toPolarCoords(Vector2(it.x-oldPos.x, it.y - oldPos.y))
            curPolar.y = min(
                abs(curPolar.y - polarDir.y),
                min(abs(curPolar.y + 2 * PI - polarDir.y), abs(curPolar.y - 2 * PI - polarDir.y))
            )
            curPolar to (curPolar.x*distanceSpreadMultiplier(curPolar, it, prios).toDouble()) to it
        }
        val res=newActor.minByOrNull { it.first.second }?.second

        return (res ?: focusNext(screen)) as FocusableActor
    }

    private fun distanceSpreadMultiplier(
        v: Vector2,
        actor: Actor,
        prios: Map<SelectionTransition.TransitionType, List<FocusableActor>>
    ): Float {
        if (v.y > PI / 2) return Float.MAX_VALUE

        SelectionTransition.TransitionType.entries.forEach {
            if ((actor as FocusableActor) in (prios[it] ?: listOf())) {
               return ((1 + v.y).pow(it.exponent.toDouble())*it.exponent).toFloat()
//               return (1 + v.y*it.exponent.toDouble()).toFloat()
            }
        }
        return Float.MAX_VALUE
    }

    private fun toPolarCoords(v: Vector2): Vector2 {
        return Vector2(sqrt(v.x * v.x + v.y * v.y), aTan(v))
    }

    private fun aTan(v: Vector2): Double {
        if (v.x < 0) return atan(v.y / v.x) + PI
        if (v.x > 0 && v.y >= 0) return atan(v.y / v.x)
        if (v.x > 0 && v.y < 0) return atan(v.y / v.x) + 2 * PI
        if (v.y > 0) return PI / 2
        return 3 * PI / 2
    }

    private fun focusNext(screen: OnjScreen): FocusableActor? {
        val oldFocusedActor = screen.focusedActor!! as Actor
        return getNextTabFocused(focusableActors.values.flatten(), oldFocusedActor) as FocusableActor?
    }

    fun focusPrevious(screen: OnjScreen): FocusableActor? {
        val oldFocusedActor = screen.focusedActor
        if (oldFocusedActor != null && oldFocusedActor is Actor) {
            return getPreviousTabFocused(focusableActors.values.flatten(), oldFocusedActor) as FocusableActor?
        }
        return if (startGroup == null || focusableActors[startGroup]?.isNotEmpty() != true) {
            getLastFocused(focusableActors.values.flatten()) as FocusableActor?
        } else {
            getLastFocused(focusableActors[startGroup]!!) as FocusableActor?
        }
    }

    private fun getFirstFocused(actors: List<FocusableActor>): Actor? {
        var curBest: Actor? = null
        var curBestPos = Float.MAX_VALUE
        actors.filter { it.isFocusable }.filterIsInstance<Actor>().forEach {
            val newPos = getDistFromStart(it)
            if (curBestPos > newPos) {
                curBest = it
                curBestPos = newPos
            }
        }
        return curBest
    }

    private fun getLastFocused(actors: List<FocusableActor>): Actor? {
        var curBest: Actor? = null
        var curBestPos = Float.MIN_VALUE
        actors.filter { it.isFocusable }.filterIsInstance<Actor>().forEach {
            val newPos = getDistFromStart(it)
            if (curBestPos < newPos) {
                curBest = it
                curBestPos = newPos
            }
        }
        return curBest
    }

    private fun getNextTabFocused(actors: List<FocusableActor>, target: Actor): Actor? {
        var curBest: Actor?
        val targetPos = Vector2(target.x + target.width / 2, target.y + target.height / 2)
        val temp = actors
            .asSequence()
            .filter { it.isFocusable && it != target }
            .filterIsInstance<Actor>()
            .map { it to getRelativePositionFromTarget(it, targetPos) }
            .filter { it.second.x > 0 || (it.second.x == 0.0 && it.second.y > 0.0) }
            .minWithOrNull(
                Comparator
                    .comparingDouble<Pair<Actor, Vector2>?> { it.second.x }
                    .thenComparingDouble { it.second.y }
            )
        temp ?: return getFirstFocused(actors)
        curBest = temp.first

        return curBest //TODO make a range in which it counts as the same x-coordinate
    }

    private fun getPreviousTabFocused(actors: List<FocusableActor>, target: Actor): Actor? {
        var curBest: Actor?
        val targetPos = Vector2(target.x + target.width / 2, target.y + target.height / 2)
        val temp = actors
            .asSequence()
            .filter { it.isFocusable && it != target }
            .filterIsInstance<Actor>()
            .map { it to getRelativePositionFromTarget(it, targetPos) }
            .filter { it.second.x < 0 || (it.second.x == 0.0 && it.second.y < 0.0) }
            .maxWithOrNull(
                Comparator
                    .comparingDouble<Pair<Actor, Vector2>?> { it.second.x }
                    .thenComparingDouble { it.second.y }
            )
        temp ?: return getLastFocused(actors)
        curBest = temp.first

        return curBest //TODO make a range in which it counts as the same x-coordinate
    }

    private fun getDistFromStart(actor: Actor): Float {
        val pos = Vector2(actor.x + actor.width / 2, actor.y + actor.height / 2)
        return ((pos.x * 3) + (SelectionTests.screenSize.y - pos.y)).toFloat()
    }

    private fun getRelativePositionFromTarget(actor: Actor, target: Vector2): Vector2 {
        val pos = Vector2(actor.x + actor.width / 2, actor.y + actor.height / 2)
        val res = Vector2(pos.x - target.x, target.y - pos.y)
        return res
    }
}

class SelectionTransition(
    val type: TransitionType,
    val condition: SelectionTransitionCondition,
    val groups: List<SelectionGroup>
) {
    enum class TransitionType(val exponent: Float) {
        SEAMLESS(4.5F),
        LAST_RESORT(7F);
    }
}

sealed class SelectionTransitionCondition {
    class Always : SelectionTransitionCondition() {
        override fun check(screen: OnjScreen): Boolean {
            return true
        }
    }

    data class Screenstate(private val screenState: String) : SelectionTransitionCondition() {
        override fun check(screen: OnjScreen): Boolean {
            return screenState in screen.screenState
        }
    }

    abstract fun check(screen: OnjScreen): Boolean
}