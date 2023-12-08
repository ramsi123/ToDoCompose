package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.FabBackgroundColor
import com.example.to_docompose.ui.theme.FabContentColor
import com.example.to_docompose.ui.theme.SCREEN_TOP_PADDING
import com.example.to_docompose.ui.theme.ScaffoldContainerColor
import com.example.to_docompose.ui.theme.ScaffoldContentColor
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.launch

/* Why are we passing -1 value on our floating action button (onFabClicked(-1))?
     Because in this case we are not selecting any task. But later from our lazy column or from our list of tasks, whenever we select one
     of those tasks, then we are going to pass the real task id. But that is later, for now it is important that we have satisfied
     that onFabClicked integer from our floating action button.*/
/* How to enable dark mode in scaffold?
     1. You have to create an xml night theme file
     2. You have to adjust the light and night container and content color with getter and "isSystemInDarkTheme()" in Color.kt file.
     How do i know this function "isSystemInDarkTheme()" from? You can see it from Theme.kt file.
     3. Fill the container and content color of scaffold with the variable that you have made in Color.kt file*/
/* Why are we using LaunchedEffect when calling getAllTasks() function?
     See documentation for LaunchedEffect in Side-effects in Compose (https://developer.android.com/jetpack/compose/side-effects#launchedeffect).
     The point is, getAllTasks function can be called as a suspend function because it's using coroutine scope. Based on the documentation
     if we want to call coroutine from a composable function, then we need to use LaunchedEffect. If we don't use LaunchedEffect, then
     this coroutine scope will launch multiple times in the background, which makes it expensive and make our application become slow.
     You can observe this behavior with logcat.*/
/* Why are we using SnackbarHostState rather than ScaffoldState in DisplaySnackBar function parameter?
     Because in ScaffoldState does not exist in material 3, the official document explains about this and its alternative. The M2
     ScaffoldState class no longer exists in M3 as it contains a drawerState parameter which is no longer needed. To show snackbars with
     the M3 Scaffold, use SnackbarHostState instead.
     Source: https://stackoverflow.com/questions/73521366/unresolved-reference-error-for-scaffoldstate-with-material3*/
/* How undo works?
     See the video no. 41. CRUD Operations - Undo Deleted Task*/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val snackbarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        onComplete = {
            sharedViewModel.updateAction(newAction = it)
        },
        onUndoClicked = {
            sharedViewModel.updateAction(newAction = it)
        },
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = SCREEN_TOP_PADDING)
            ) {
                ListContent(
                    allTasks = allTasks,
                    searchedTasks = searchedTasks,
                    lowPriorityTasks = lowPriorityTasks,
                    highPriorityTasks = highPriorityTasks,
                    sortState = sortState,
                    searchAppBarState = searchAppBarState,
                    onSwipeToDelete = { action, task ->
                        sharedViewModel.updateAction(newAction = action)
                        sharedViewModel.updateTaskFields(selectedTask = task)
                        snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        },
        containerColor = ScaffoldContainerColor,
        contentColor = ScaffoldContentColor
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        containerColor = FabBackgroundColor,
        contentColor = FabContentColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(
                id = R.string.add_button
            )
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = setMessage(
                        action = action,
                        taskTitle = taskTitle
                    ),
                    actionLabel = setActionLabel(action = action),
                    duration = SnackbarDuration.Short
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setMessage(
    action: Action,
    taskTitle: String
): String {
    return when (action) {
        Action.DELETE_ALL -> "All Tasks Removed"
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}
