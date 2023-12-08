package com.example.to_docompose.navigation.destinations

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.to_docompose.ui.screens.task.TaskScreen
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.Constants
import com.example.to_docompose.util.Constants.TASK_ARGUMENT_KEY

// What is key used for in LaunchedEffect?
    // In this case our key is selectedTask, whenever the selectedTask changes, only then we will execute the updateTaskFields. So basically
    // the LaunchedEffect block will execute if there is a change in selectedTask, if there is no change, then it will not get executed.
    // Source: Video no. 36. CRUD Operations - Bug Fix & Fields Validation

fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
) {

    composable(
        route = Constants.TASK_SCREEN,
        arguments = listOf(navArgument(Constants.TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        })
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)
        LaunchedEffect(key1 = taskId) {
            sharedViewModel.getSelectedTask(taskId = taskId)
        }
        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1) {
                sharedViewModel.updateTaskFields(selectedTask = selectedTask)
            }
        }

        TaskScreen(selectedTask = selectedTask, sharedViewModel = sharedViewModel, navigateToListScreen = navigateToListScreen)
    }

}