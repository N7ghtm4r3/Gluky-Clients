package com.tecknobit.gluky.ui.screens

import androidx.compose.foundation.layout.ColumnScope
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
import com.tecknobit.equinoxnavigation.I18nNavigationTab
import com.tecknobit.equinoxnavigation.NavigatorScreen
import com.tecknobit.gluky.ui.components.ProfilePic
import com.tecknobit.gluky.ui.icons.Meals
import com.tecknobit.gluky.ui.screens.account.presenter.AccountScreen
import com.tecknobit.gluky.ui.screens.analyses.presenter.AnalysesScreen
import com.tecknobit.gluky.ui.screens.meals.presenter.MealsScreen
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.GlukyTheme
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.account
import gluky.composeapp.generated.resources.analyses
import gluky.composeapp.generated.resources.app_version
import gluky.composeapp.generated.resources.meals
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeApi::class)
class HomeScreen : NavigatorScreen<I18nNavigationTab>() {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        GlukyTheme {
            NavigationContent()
        }
    }

    @Composable
    override fun ColumnScope.SideNavigationHeaderContent() {
        ProfilePic(
            modifier = Modifier
                .padding(
                    top = 16.dp
                ),
            size = 115.dp,
            onClick = {
                activeNavigationTabIndex.value = 2
            }
        )
    }

    @Composable
    override fun ColumnScope.SideNavigationFooterContent() {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "v${stringResource(Res.string.app_version)}",
            style = AppTypography.labelMedium,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    override fun SideNavigationItem(
        index: Int,
        tab: I18nNavigationTab,
    ) {
        if (tab.isNotAccountTab())
            super.SideNavigationItem(index, tab)
    }

    @Composable
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
                        onClick = {
                            activeNavigationTabIndex.value = 2
                        }
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

    private fun I18nNavigationTab.isNotAccountTab(): Boolean {
        return title != Res.string.account
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
    override fun Int.tabContent(): EquinoxNoModelScreen {
        return when (this) {
            0 -> MealsScreen()
            1 -> AnalysesScreen()
            else -> AccountScreen()
        }
    }

}