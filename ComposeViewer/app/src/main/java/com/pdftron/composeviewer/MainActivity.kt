package com.pdftron.composeviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Context
import androidx.compose.ambient
import androidx.compose.unaryPlus
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.imageFromResource
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Card
import androidx.ui.material.themeTextStyle
import com.pdftron.composeviewer.data.BookshelfState
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import com.pdftron.pdf.utils.Utils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val documentsState = BookshelfState();
        setContent {
            MaterialTheme {
                Bookshelf(documentsState)
            }
        }
    }
}

@Composable
private fun Bookshelf(bookshelfState: BookshelfState) {
    val columns = 3
    Table(
        columns = columns
    ) {
        val groupedDocs = bookshelfState.documents.chunked(columns)
        groupedDocs.forEach {
            this.tableRow {
                it.forEach {
                    ClickableBookshelfItem(it)
                }
            }
        }
    }
}

@Composable
fun BookshelfItem(docName: String) {
    val context = +ambient(ContextAmbient)
    val thumbnail = imageFromResource(
        context.resources,
        getThumbnailResourceFromString(context, docName)!!
    )
    Padding(4.dp) {
        Card(shape = RoundedCornerShape(4.dp), elevation = 2.dp) {
            Column(
                crossAxisAlignment = CrossAxisAlignment.Center,
                crossAxisSize = LayoutSize.Wrap
            ) {
                SimpleImage(thumbnail)
                Padding(8.dp) {
                    Text(
                        text = docName,
                        style = (+themeTextStyle { subtitle1 })
                    )
                }
            }
        }
    }
}

@Composable
fun ClickableBookshelfItem(docName: String) {
    val context = +ambient(ContextAmbient)
    val config = ViewerConfig.Builder()
        .multiTabEnabled(false)
        .build();

    // Wrap bookshelf item with a clickable composable
    // that will launch the viewer
    Ripple(bounded = true) {
        Clickable(onClick = {
            // Launch the viewer for the clicked thumbnail
            DocumentActivity.openDocument(
                context,
                getResourceFromString(context, docName),
                config
            );
        }) {
            BookshelfItem(docName)
        }
    }
}

fun getResourceFromString(context: Context, resString: String?): Int {
    return Utils.getResourceRaw(
        context,
        resString!!.substringBeforeLast('.', "")
    )
}

fun getThumbnailResourceFromString(context: Context, resString: String?): Int {
    return getResourceFromString(context, "thumb_$resString")
}
