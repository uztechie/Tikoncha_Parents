package uz.tikoncha_parent.presentation.policy.limit_rule.setup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.presentation.policy.common.buildWeekdayChips
import uz.tikoncha_parent.presentation.policy.common.occupiedLimitDays
import uz.tikoncha_parent.presentation.policy.common.selectedDays
import uz.tikoncha_parent.presentation.policy.common.toggleDay
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi

class LimitRuleSetupViewModel : ScreenModel {

    private val _state = MutableStateFlow(LimitRuleSetupState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LimitRuleSetupEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LimitRuleSetupEvent) {
        when (event) {
            is LimitRuleSetupEvent.Init -> handleInit(event)

            is LimitRuleSetupEvent.SelectDay -> _state.update {
                it.copy(weekDays = it.weekDays.toggleDay(event.day))
            }

            is LimitRuleSetupEvent.SelectLimitType -> _state.update {
                // Tipni o'zgartirganda davomiyat resetlanadi — boshqa oraliq
                it.copy(
                    limitType = event.type,
                    duration = HourMinute(0, 0),
                )
            }

            is LimitRuleSetupEvent.SetHour -> _state.update {
                // Hourly tipda soat o'zgartirilmaydi
                if (it.limitType == DayHour.HOUR) it
                else it.copy(duration = it.duration.copy(hour = event.hour))
            }

            is LimitRuleSetupEvent.SetMinute -> _state.update {
                it.copy(duration = it.duration.copy(minute = event.minute))
            }

            LimitRuleSetupEvent.Save -> save()
        }
    }

    private fun handleInit(event: LimitRuleSetupEvent.Init) {
        if (_state.value.isInitialized) return

        val editing = event.editingRule
        val occupied = occupiedLimitDays(
            rules = event.allLimitRules,
            excludeId = editing?.id,
        )

        _state.update {
            LimitRuleSetupState(
                editingId = editing?.id,
                weekDays = buildWeekdayChips(
                    otherRuleDays = occupied,
                    currentSelected = editing?.weekDays.orEmpty(),
                ),
                duration = editing?.time ?: HourMinute(0, 0),
                limitType = editing?.limitType ?: DayHour.DAY,
                isInitialized = true,
            )
        }
    }

    private fun save() {
        val s = _state.value
        if (!s.canSave) return

        val rule = LimitRuleUi(
            // id = 0 → create signal. Edit bo'lsa mavjud id.
            // Yakuniy id PolicySharedModel.upsertLimitRule ichida beriladi.
            id = s.editingId ?: 0,
            weekDays = s.weekDays.selectedDays(),
            time = HourMinute(
                hour = if (s.limitType == DayHour.HOUR) 0 else s.duration.hour,
                minute = s.duration.minute,
            ),
            limitType = s.limitType,
        )

        screenModelScope.launch {
            _effect.send(LimitRuleSetupEffect.Saved(rule))
        }
    }
}