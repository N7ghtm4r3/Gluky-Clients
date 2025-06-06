package com.tecknobit.gluky.ui.screens.home

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxnavigation.I18nNavigationTab
import com.tecknobit.equinoxnavigation.NavigatorScreen
import com.tecknobit.gluky.CloseApplicationOnNavBack
import com.tecknobit.gluky.ui.components.ProfilePic
import com.tecknobit.gluky.ui.icons.Meals
import com.tecknobit.gluky.ui.screens.TMPNavigatorScreen
import com.tecknobit.gluky.ui.screens.account.presenter.AccountScreen
import com.tecknobit.gluky.ui.screens.analyses.presenter.AnalysesScreen
import com.tecknobit.gluky.ui.screens.measurements.presenter.MeasurementsScreen
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenTab
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.GlukyTheme
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.account
import gluky.composeapp.generated.resources.analyses
import gluky.composeapp.generated.resources.app_version
import gluky.composeapp.generated.resources.meals
import org.jetbrains.compose.resources.stringResource

/**
 * The [HomeScreen] class is used to display the specific tab and handle the navigation in app
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxNoModelScreen
 * @see NavigatorScreen
 * @see I18nNavigationTab
 */
@OptIn(ExperimentalComposeApi::class)
class HomeScreen : TMPNavigatorScreen<I18nNavigationTab>() {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        GlukyTheme {
            NavigationContent()
        }
    }

    /**
     * Custom header content to display on the [SideNavigationArrangement] bar
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    override fun ColumnScope.SideNavigationHeaderContent() {
        ProfilePic(
            modifier = Modifier
                .padding(
                    top = 16.dp
                ),
            size = 115.dp,
            onClick = { activeNavigationTabIndex.value = 2 }
        )
    }

    /**
     * Custom footer content to display on the [SideNavigationArrangement] bar
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    override fun ColumnScope.SideNavigationFooterContent() {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "v${stringResource(Res.string.app_version)}",
            style = AppTypography.labelMedium,
            textAlign = TextAlign.Center
        )
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
    override fun SideNavigationItem(
        index: Int,
        tab: I18nNavigationTab,
    ) {
        if (tab.isNotAccountTab())
            super.SideNavigationItem(index, tab)
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
    override fun RowScope.BottomNavigationItem(
        index: Int,
        tab: I18nNavigationTab,
    ) {
        val isNotAccountTab = tab.isNotAccountTab()
        NavigationBarItem(
            selected = index == activeNavigationTabIndex.value,
            onClick = { activeNavigationTabIndex.value = index },
            icon = {
                if (isNotAccountTab) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.contentDescription
                    )
                } else {
                    ProfilePic(
                        size = 40.dp,
                        onClick = { activeNavigationTabIndex.value = 2 }
                    )
                }
            },
            label = if (isNotAccountTab) {
                {
                    Text(
                        text = tab.prepareTitle(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else
                null,
            alwaysShowLabel = false
        )
    }

    /**
     * Method used to check whether the [I18nNavigationTab] is the [AccountScreen] one
     *
     * @return whether the tab is the account one as [Boolean]
     */
    private fun I18nNavigationTab.isNotAccountTab(): Boolean {
        return title != Res.string.account
    }

    @FutureEquinoxApi
    override fun bottomNavigationContentPadding(): PaddingValues {
        return if (activeNavigationTabIndex.value != 0)
            super.bottomNavigationContentPadding()
        else {
            PaddingValues(
                top = 0.dp,
                start = 0.dp,
                end = 0.dp,
                bottom = 80.dp
            )
        }
    }

    /**
     * Method used to retrieve the tabs to assign to the [tabs] array
     *
     * @return the tabs used by the [NavigatorScreen] as [Array] of [I18nNavigationTab]
     */
    override fun navigationTabs(): Array<I18nNavigationTab> {
        return arrayOf(
            I18nNavigationTab(
                title = Res.string.meals,
                icon = Meals,
                contentDescription = "Meals"
            ),
            I18nNavigationTab(
                title = Res.string.analyses,
                icon = Icons.Default.Analytics,
                contentDescription = "Analyses"
            ),
            I18nNavigationTab(
                title = Res.string.account,
                icon = Icons.Default.Settings,
                contentDescription = "Account"
            )
        )
    }

    /**
     * Method used to instantiate the related screen based on the current [activeNavigationTabIndex]
     *
     * @return the screen as [EquinoxNoModelScreen]
     */
    override fun Int.tabContent(): GlukyScreenTab<*> {
        return when (this) {
            0 -> MeasurementsScreen()
            1 -> AnalysesScreen()
            else -> AccountScreen()
        }
    }

}