package com.tecknobit.gluky.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxnavigation.I18nNavigationTab
import com.tecknobit.equinoxnavigation.NavigationMode
import com.tecknobit.equinoxnavigation.NavigationMode.BOTTOM_NAVIGATION
import com.tecknobit.equinoxnavigation.NavigationMode.SIDE_NAVIGATION
import com.tecknobit.equinoxnavigation.NavigationTab
import com.tecknobit.equinoxnavigation.NavigatorTab
import org.jetbrains.compose.resources.stringResource

/**
 * The `NavigatorScreen` provides a responsive navigation system that dynamically adjusts the navigation bars and content
 * of each screen based on the current [com.tecknobit.equinoxcompose.utilities.ResponsiveClass]
 *
 * Related documentation: [NavigatorScreen.md](https://github.com/N7ghtm4r3/Equinox/blob/main/documd/navigation/NavigatorScreen.md)
 *
 * @property loggerEnabled Whether enabled the logging to log the events occurred in the [ShowContent] composable,
 * it is suggested to disable it in production
 *
 * @param T The type of the [NavigatorTab] used by the navigator
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxNoModelScreen
 *
 * @since 1.0.0
 *
 */
@Structure
@ExperimentalComposeApi
@Deprecated("TO USE THE EQUINOX BUILT-IN")
@FutureEquinoxApi(
    protoBehavior = "This NavigatorScreen introduces some feature will have the built-in one",
    releaseVersion = "1.0.2"
)
abstract class TMPNavigatorScreen<T : NavigatorTab<*>>(
    loggerEnabled: Boolean = true,
) : EquinoxNoModelScreen(
    loggerEnabled = loggerEnabled
) {

    @FutureEquinoxApi(
        additionalNotes = """
            Create the related method for SIDE_NAVIGATION sideNavigationContentPadding() also
            """
    )
    protected open fun bottomNavigationContentPadding(): PaddingValues {
        return PaddingValues(
            top = 25.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 96.dp
        )
    }

    /**
     *`activeNavigationTabIndex` the index of the current [NavigatorTab] displayed
     */
    protected lateinit var activeNavigationTabIndex: MutableState<Int>

    /**
     *`tabs` the reachable destinations tabs
     */
    protected val tabs: Array<T> by lazy { navigationTabs() }

    /**
     *`navigationMode` current navigation mode adopted by the navigator
     */
    private lateinit var navigationMode: MutableState<NavigationMode>

    /**
     * Method used to retrieve the tabs to assign to the [tabs] array
     *
     * @return the tabs used by the [TMPNavigatorScreen] as [Array] of [T]
     */
    protected abstract fun navigationTabs(): Array<T>

    /**
     * Responsive navigation system based on the current [com.tecknobit.equinoxcompose.utilities.ResponsiveClass]
     *
     * @param sideBarModifier The modifier to apply to the [SideNavigationArrangement] bar
     * @param sideBarWidth The default width of the [SideNavigationArrangement] bar
     * @param bottomBarModifier The modifier to apply to the [BottomNavigationArrangement] bar
     * @param navigationBarColor The color to apply to the navigation bars
     * @param backgroundTab The color to apply as background of the tabs
     */
    @Composable
    @LayoutCoordinator
    protected fun NavigationContent(
        sideBarModifier: Modifier = Modifier,
        sideBarWidth: Dp = 185.dp,
        bottomBarModifier: Modifier = Modifier,
        navigationBarColor: Color = BottomAppBarDefaults.containerColor,
        backgroundTab: Color = MaterialTheme.colorScheme.background,
    ) {
        ResponsiveContent(
            onExpandedSizeClass = {
                SideNavigationArrangement(
                    modifier = sideBarModifier,
                    sideBarWidth = sideBarWidth,
                    navigationBarColor = navigationBarColor,
                    backgroundTab = backgroundTab
                )
            },
            onMediumSizeClass = {
                SideNavigationArrangement(
                    modifier = sideBarModifier,
                    sideBarWidth = sideBarWidth,
                    navigationBarColor = navigationBarColor,
                    backgroundTab = backgroundTab
                )
            },
            onMediumWidthExpandedHeight = {
                BottomNavigationArrangement(
                    modifier = bottomBarModifier,
                    navigationBarColor = navigationBarColor,
                    backgroundTab = backgroundTab
                )
            },
            onCompactSizeClass = {
                BottomNavigationArrangement(
                    modifier = bottomBarModifier,
                    navigationBarColor = navigationBarColor,
                    backgroundTab = backgroundTab
                )
            }
        )
    }

    /**
     * Custom [NavigationRail] displayed on the [EXPANDED_CONTENT] and [MEDIUM_CONTENT] responsive screen classes
     *
     * @param modifier The modifier to apply to the navigation bar
     * @param sideBarWidth The default width of the navigation bar
     * @param navigationBarColor The color to apply to the navigation bars
     * @param backgroundTab The color to apply as background of the tabs
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun SideNavigationArrangement(
        modifier: Modifier,
        sideBarWidth: Dp,
        navigationBarColor: Color,
        backgroundTab: Color,
    ) {
        navigationMode.value = SIDE_NAVIGATION
        Row {
            NavigationRail(
                modifier = modifier
                    .width(sideBarWidth),
                containerColor = navigationBarColor,
                header = { SideNavigationHeaderContent() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Column(
                        modifier = Modifier
                            .weight(2.7f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            SideNavigationItem(
                                index = index,
                                tab = tab
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                vertical = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        content = { SideNavigationFooterContent() }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(backgroundTab)
            ) {
                ScreenTabContent(
                    paddingValues = PaddingValues(
                        top = 35.dp,
                        start = 35.dp,
                        end = 35.dp,
                        bottom = 16.dp
                    )
                )
            }
        }
    }

    /**
     * Custom header content to display on the [SideNavigationArrangement] bar
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    protected open fun ColumnScope.SideNavigationHeaderContent() {
    }

    /**
     * Custom footer content to display on the [SideNavigationArrangement] bar
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    protected open fun ColumnScope.SideNavigationFooterContent() {
    }

    /**
     * The navigation item of the [SideNavigationArrangement] bar
     *
     * @param index The index related to the item in the [tabs] array
     * @param tab The related tab of the [index]
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    protected open fun SideNavigationItem(
        index: Int,
        tab: T,
    ) {
        NavigationDrawerItem(
            modifier = Modifier
                .padding(
                    end = 10.dp
                ),
            selected = index == activeNavigationTabIndex.value,
            onClick = { activeNavigationTabIndex.value = index },
            shape = RoundedCornerShape(
                topEnd = 10.dp,
                bottomEnd = 10.dp
            ),
            icon = {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.contentDescription
                )
            },
            label = {
                Text(
                    text = tab.prepareTitle(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
    }

    /**
     * Custom [BottomAppBar] displayed on the [MEDIUM_EXPANDED_CONTENT] and [COMPACT_CONTENT] responsive screen classes
     *
     * @param modifier The modifier to apply to the navigation bar
     * @param navigationBarColor The color to apply to the navigation bars
     * @param backgroundTab The color to apply as background of the tabs
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [MEDIUM_EXPANDED_CONTENT, COMPACT_CONTENT]
    )
    private fun BottomNavigationArrangement(
        modifier: Modifier,
        navigationBarColor: Color,
        backgroundTab: Color,
    ) {
        navigationMode.value = BOTTOM_NAVIGATION
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundTab)
        ) {
            ScreenTabContent(
                paddingValues = bottomNavigationContentPadding()
            )
            BottomAppBar(
                modifier = modifier
                    .align(Alignment.BottomCenter),
                containerColor = navigationBarColor
            ) {
                tabs.forEachIndexed { index, tab ->
                    BottomNavigationItem(
                        index = index,
                        tab = tab
                    )
                }
            }
        }
    }

    /**
     * The navigation item of the [BottomNavigationItem] bar
     *
     * @param index The index related to the item in the [tabs] array
     * @param tab The related tab of the [index]
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [MEDIUM_EXPANDED_CONTENT, COMPACT_CONTENT]
    )
    protected open fun RowScope.BottomNavigationItem(
        index: Int,
        tab: T,
    ) {
        NavigationBarItem(
            selected = index == activeNavigationTabIndex.value,
            onClick = { activeNavigationTabIndex.value = index },
            icon = {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.contentDescription
                )
            },
            label = {
                Text(
                    text = tab.prepareTitle(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
    }

    /**
     * Method used to prepare the title for the [SideNavigationItem] and the [BottomNavigationItem] based on
     * the [NavigatorTab] used by the navigator
     *
     * @return the title for the tab as [String]
     */
    @Composable
    protected open fun NavigatorTab<*>.prepareTitle(): String {
        return when (this) {
            is NavigationTab -> title
            is I18nNavigationTab -> stringResource(title)
            else -> title.toString()
        }
    }

    /**
     * The content of the tab
     *
     * @param paddingValues The values of the padding to apply to the content
     */
    @Composable
    private fun ScreenTabContent(
        paddingValues: PaddingValues,
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column {
                AnimatedContent(
                    targetState = activeNavigationTabIndex.value
                ) { activeIndex ->
                    var screenTab by remember { mutableStateOf<EquinoxNoModelScreen?>(null) }
                    LaunchedEffect(Unit) {
                        screenTab = activeIndex.tabContent()
                    }
                    screenTab?.ShowContent()
                }
            }
        }
    }

    /**
     * Method used to instantiate the related screen based on the current [activeNavigationTabIndex]
     *
     * @return the screen as [EquinoxNoModelScreen]
     */
    protected abstract fun Int.tabContent(): EquinoxNoModelScreen

    /**
     * Method used to check whether the current [navigationMode] is [SIDE_NAVIGATION]
     *
     * @return whether the navigation mode is [SIDE_NAVIGATION] as [Boolean]
     */
    protected fun isSideNavigationMode(): Boolean {
        return navigationMode.value == SIDE_NAVIGATION
    }

    /**
     * Method used to check whether the current [navigationMode] is [BOTTOM_NAVIGATION]
     *
     * @return whether the navigation mode is [BOTTOM_NAVIGATION] as [Boolean]
     */
    protected fun isBottomNavigationMode(): Boolean {
        return navigationMode.value == BOTTOM_NAVIGATION
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        activeNavigationTabIndex = rememberSaveable { mutableStateOf(0) }
        navigationMode = rememberSaveable { mutableStateOf(BOTTOM_NAVIGATION) }
    }

}