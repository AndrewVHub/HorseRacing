package ru.andrewvhub.horseracing.ui.items

import ru.andrewvhub.horseracing.data.model.RaceStatus

data class RaceHistoryItem(
    val timestamp: String,
    val winner: String,
    val horseNames: String,
    val distance: String,
    val status: RaceStatus
) : Item {

    override val id: String
        get() = "${timestamp}_${winner}_${distance}"

    override fun getType(): ItemContentType = ItemContentType.HistoryItemType

    override fun areContentsSame(newItem: Item): Boolean {
        newItem as RaceHistoryItem
        return timestamp == newItem.timestamp
                && winner == newItem.winner
                && horseNames == newItem.horseNames
                && distance == newItem.distance
                && status == newItem.status
    }

    override fun getChangePayload(newItem: Item): Any {
        newItem as RaceHistoryItem
        val diff = mutableListOf<String>()
        if (timestamp != newItem.timestamp) diff.add(TIMESTAMP_KEY)
        if (winner != newItem.winner) diff.add(WINNER_KEY)
        if (horseNames != newItem.horseNames) diff.add(HORSE_NAMES_KEY)
        if (distance != newItem.distance) diff.add(DISTANCE_KEY)
        if (status != newItem.status) diff.add(STATUS_KEY)
        return diff
    }

    companion object {
        const val TIMESTAMP_KEY = "TIMESTAMP_KEY"
        const val WINNER_KEY = "WINNER_KEY"
        const val HORSE_NAMES_KEY = "HORSE_NAMES_KEY"
        const val DISTANCE_KEY = "DISTANCE_KEY"
        const val STATUS_KEY = "STATUS_KEY"
    }
}