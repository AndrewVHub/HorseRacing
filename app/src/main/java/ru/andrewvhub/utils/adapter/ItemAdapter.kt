package ru.andrewvhub.utils.adapter

import ru.andrewvhub.horseracing.ui.items.Item

interface ItemAdapter {
    fun getItemByPosition(position: Int): Item?
}