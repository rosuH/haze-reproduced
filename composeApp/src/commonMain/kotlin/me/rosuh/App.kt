package me.rosuh

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(modifier = Modifier.fillMaxSize()) {
            var tabState by remember { mutableStateOf(0) }
            val titles = listOf("Article", "Social Media", "Image", "Video", "Audio", "Notification")
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(pageCount = {
                titles.size
            })
            val hazeState = remember { HazeState() }
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    tabState = page
                }
            }
            PrimaryScrollableTabRow(
                modifier = Modifier.hazeChild(
                    hazeState,
                    style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                ).statusBarsPadding(),
                containerColor = Color.Transparent,
                selectedTabIndex = tabState,
                edgePadding = 8.dp,
                divider = {}
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = tabState == index,
                        onClick = {
                            tabState = index
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            if (tabState == index) {
                                Text(
                                    text = title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = title,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    )
                }
            }
            HorizontalPager(modifier = Modifier.haze(state = hazeState).fillMaxSize(), state = pagerState) { page ->
                DemoListScreen(titles[page])
            }
        }

    }
}

@Composable
fun DemoListScreen(title: String) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(100) {
            item {
                Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(text = "$title  Item $it", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}
