package com.example.to_docompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF141414)

val LowPriorityColor = Color(0xFF00C980)
val MediumPriorityColor = Color(0xFFFFC114)
val HighPriorityColor = Color(0xFFFF4646)
val NonePriorityColor = MediumGray

/*val SplashScreenBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Purple40*/

val TaskItemBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkGray else Color.White

val TaskItemTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGray else DarkGray

val ScaffoldContainerColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkGray else Color.White

val ScaffoldContentColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGray else DarkGray

val FabBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Purple40 else Purple80

val FabContentColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGray else Color.Black

val TopAppBarContentColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGray else Color.White

val TopAppBarBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Purple40

val SortMenuBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Color.White

val SortMenuContentColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGray else Color.Black