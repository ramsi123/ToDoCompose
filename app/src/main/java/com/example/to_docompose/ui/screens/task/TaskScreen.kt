package com.example.to_docompose.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.theme.SCREEN_TOP_PADDING
import com.example.to_docompose.ui.theme.ScaffoldContainerColor
import com.example.to_docompose.ui.theme.ScaffoldContentColor
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action

/* How to store a value that change and observe it?
     This is how to store if there is something change in a value (onTitleChange = { sharedViewModel.title.value = it }).
     This is how to observe it (var title: String by sharedViewModel.title).
     This applies to description and priority as well.
     So whenever we open up our application and whenever we type here or something inside our description, title text field, or priority
     drop down, those values will automatically saved inside our SharedViewModel. Those same variables will also be observed from our
     TaskScreen, and those values will be passed to our TaskContent. Whenever those values change, then our TaskContent will be notified
     and immediately recomposed with the new values.*/
/* How this TaskScreen works?
     Watch video no. 34 Task Screen - Display Dynamic Content - 8.16*/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {

    val title: String = sharedViewModel.title
    val description: String = sharedViewModel.description
    val priority: Priority = sharedViewModel.priority

    val context = LocalContext.current

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = SCREEN_TOP_PADDING)
            ) {
                TaskContent(
                    title = title,
                    onTitleChange = {
                        sharedViewModel.updateTitle(it)
                    },
                    description = description,
                    onDescriptionChange = {
                        sharedViewModel.updateDescription(newDescription = it)
                    },
                    priority = priority,
                    onPrioritySelected = {
                        sharedViewModel.updatePriority(it)
                    }
                )
            }
        },
        containerColor = ScaffoldContainerColor,
        contentColor = ScaffoldContentColor
    )

}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields Empty.",
        Toast.LENGTH_SHORT
    ).show()
}

// This is the BackHandler function that we create manually because we want to handle the back navigation from android system.
// Watch video no. 58 Update #2 - Code Cleanup - handleDatabaseActions()
// But it turns out that there is already predefined function that we can use instantly, and ironically has the same function name.
// Watch video no. 60 Update #4 - Configuration Change, SnackBar, SearchAppBar Bugs Fixed and more.. - 6.20
/*@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backDispatcher) {
        backDispatcher?.addCallback(onBackPressedCallback = backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}*/
