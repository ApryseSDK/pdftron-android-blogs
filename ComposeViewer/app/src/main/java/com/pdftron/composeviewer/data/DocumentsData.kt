package com.pdftron.composeviewer.data

import androidx.compose.Model

@Model
class BookshelfState() {
    val documents =
        listOf(
            "contract.pdf",
            "sample.pdf",
            "blueprint.pdf",
            "drawing.pdf",
            "floorplan.pdf",
            "formula.pdf",
            "invoice.pdf",
            "music.pdf",
            "news.pdf"
        )
}