package ru.andrewvhub.horseracing.ui.viewHolders

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import ru.andrewvhub.horseracing.core.BaseViewHolder
import ru.andrewvhub.horseracing.databinding.ItemRaceHistoryBinding
import ru.andrewvhub.horseracing.ui.items.RaceHistoryItem

class RaceHistoryViewHolder(private val viewBinding: ItemRaceHistoryBinding) :
    BaseViewHolder<RaceHistoryItem>(viewBinding.root) {

    override fun bind(item: RaceHistoryItem) {
        updateTime(item)
        updateHorses(item)
        updateDistance(item)
        updateStatusOrSetWinner(item)
        updateStatus(item)
    }

    override fun update(item: RaceHistoryItem, payloads: Set<*>) = payloads.forEach {
        when (it) {
            RaceHistoryItem.TIMESTAMP_KEY -> updateTime(item)
            RaceHistoryItem.WINNER_KEY -> updateStatusOrSetWinner(item)
            RaceHistoryItem.HORSE_NAMES_KEY -> updateHorses(item)
            RaceHistoryItem.DISTANCE_KEY -> updateDistance(item)
            RaceHistoryItem.STATUS_KEY -> updateStatus(item)
        }
    }

    private fun updateTime(item: RaceHistoryItem) {
        viewBinding.time.text = item.timestamp
    }

    private fun updateHorses(item: RaceHistoryItem) {
        viewBinding.horseNames.text = item.horseNames
    }

    private fun updateDistance(item: RaceHistoryItem) {
        viewBinding.distance.text = item.distance
    }

    private fun updateStatusOrSetWinner(item: RaceHistoryItem): Unit = with(viewBinding) {
        val hasWinner = item.winner.isNotBlank()
        crownImage.isVisible = hasWinner
        // Текст — либо имя победителя, либо статус из ресурсов
        raceResultInfo.text =
            item.winner.takeIf { hasWinner } ?: root.context.getString(item.status.nameResId)
    }

    private fun updateStatus(item: RaceHistoryItem): Unit = with(viewBinding) {
        backgroundCard.backgroundTintList =
            ContextCompat.getColorStateList(root.context, item.status.colorStatus)
    }
}