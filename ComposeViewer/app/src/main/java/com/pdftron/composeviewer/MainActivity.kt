package com.pdftron.composeviewer

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pdftron.composeviewer.ui.theme.ComposeViewerTheme
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import com.pdftron.pdf.utils.Utils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        TopAppBar(title = { Text(text = "Compose Viewer") })
                        Bookshelf()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Bookshelf() {
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


    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(documents) {
            BookshelfItem(it)
        }
    }
}

@Composable
fun BookshelfItem(document: String) {

    val context = LocalContext.current
    val painter: Painter = painterResource(id = getThumbnailResourceFromString(context, document))
    val config = ViewerConfig.Builder()
        .multiTabEnabled(false)
        .build();

    val docRes = getResourceFromString(context, document)

    Card(
        modifier = Modifier
            .border(1.dp, Color.Transparent)
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    DocumentActivity.openDocument(
                        context,
                        docRes,
                        config
                    )
                }
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
            Text(text = document)
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
    val suffix = resString!!.substringBeforeLast('.', "")
    return Utils.getResourceDrawable(
        context,
        "thumb_$suffix"
    )
}