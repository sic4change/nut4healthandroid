package org.sic4change.nut4health.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import org.sic4change.nut4health.R

enum class NavItem(
    val navCommand: NavCommand,
    val icon: ImageVector,
    @StringRes val title: Int
) {
    LOGIN(NavCommand.ContentType(Feature.LOGIN), Icons.Default.Home, R.string.login),
    HOME(NavCommand.ContentType(Feature.HOME), Icons.Default.Home, R.string.app_name),
}

sealed class NavCommand(
    internal val feature: Feature,
    internal val subRoute: String = "home",
    private val navArgs: List<NavArg> = emptyList()
) {
    class ContentType(feature: Feature) : NavCommand(feature)

    class ContentTypeDetail(feature: Feature) :
        NavCommand(feature, "detail", listOf(NavArg.ItemId)) {

        fun createRoute(itemId: String) : String {
            return "${feature.route}/$subRoute/$itemId"
        }
    }

    class ContentTypeCreate(feature: Feature) :
        NavCommand(feature, "create", listOf(NavArg.ItemId)) {

        fun createRoute(itemId: String) : String {
            return "${feature.route}/$subRoute/$itemId"
        }
    }

    val route = run {
        val argValues = navArgs.map { "{${it.key}}" }
        listOf(feature.route)
            .plus(subRoute)
            .plus(argValues)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

}

enum class NavArg(val key: String, val navType: NavType<*>) {
    ItemId("itemId", NavType.StringType)
}
