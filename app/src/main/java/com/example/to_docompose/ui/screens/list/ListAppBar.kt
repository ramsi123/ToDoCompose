package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.components.DisplayAlertDialog
import com.example.to_docompose.components.PriorityItem
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.theme.LARGE_PADDING
import com.example.to_docompose.ui.theme.ScaffoldContainerColor
import com.example.to_docompose.ui.theme.ScaffoldContentColor
import com.example.to_docompose.ui.theme.SortMenuBackgroundColor
import com.example.to_docompose.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.to_docompose.ui.theme.TopAppBarBackgroundColor
import com.example.to_docompose.ui.theme.TopAppBarContentColor
import com.example.to_docompose.ui.theme.Typography
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.SearchAppBarState

/* The state of searchAppBarState and searchTextState
     If you see from ListAppBar function, we observe the state of searchAppBarState and searchTextState directly from a parameter
     which is passed from a variable in ListScreen Class. But when we try to reassign that state, notice that we use sharedViewModel
     instead. It's because searchAppBarState and searchTextState in ListScreen is only an object which taken from SharedViewModel class.
     Those objects have a data type of SearchAppBarState and String respectively, therefore we cannot reassign it because it's val.
     But in SharedViewModel class, those state has a mutableState type even though it's val, in jetpack compose we can reassign it.*/
/* Why are we using when instead of regular if else like in java?
     Example we can use something like this
        if (searchTextState.isNotEmpty()) {
            sharedViewModel.searchTextState.value = ""
        } else {
            sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
        }
     But why are we using when? Yes you can use if else like in java. But that is how you do it in java, in kotlin you are using when.
     The reason tutor use 'when' method is because he wants the code to be more readable. First he creates the TrailingIconState
     enum class in util directory. Value from that enum class will be taken to represent the trailingIconState value used for 'when'.
     Then he is using if else to do a boolean check, because you can't do that with when. The trailingIconState is used in SearchAppBar
     function, trailingIcon parameter.
        when (trailingIconState) {
            TrailingIconState.READY_TO_DELETE -> {
                if (text.isEmpty()) {
                    onCloseClicked()
                } else {
                    onTextChange("")
                    trailingIconState = TrailingIconState.READY_TO_CLOSE
                }
            }
            TrailingIconState.READY_TO_CLOSE -> {
                if (text.isNotEmpty()) {
                    onTextChange("")
                } else {
                    onCloseClicked()
                    trailingIconState = TrailingIconState.READY_TO_DELETE
                }
            }
        }*/

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.updateAppBarState(newState = SearchAppBarState.OPENED)
                },
                onSortClicked = {
                    sharedViewModel.persistSortState(priority = it)
                },
                onDeleteAllConfirmed = {
                    sharedViewModel.updateAction(newAction = Action.DELETE_ALL)
                }
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText ->
                    sharedViewModel.updateSearchText(newText = newText)
                },
                onCloseClicked = {
                    sharedViewModel.updateAppBarState(newState = SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchText(newText = "")
                },
                onSearchClicked = {
                    sharedViewModel.searchDatabase(searchQuery = it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.list_screen_title),
                color = TopAppBarContentColor
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TopAppBarBackgroundColor
        )
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_action),
            tint = TopAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = TopAppBarContentColor
        )
        DropdownMenu(
            modifier = Modifier
                .background(color = SortMenuBackgroundColor),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Priority.values().slice(setOf(0, 2, 3)).forEach { priority ->
                DropdownMenuItem(
                    text = {
                        PriorityItem(priority = priority)
                    },
                    onClick = {
                        expanded = false
                        onSortClicked(priority)
                    }
                )
            }
            /*DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.LOW)
                },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.LOW)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.HIGH)
                },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.HIGH)
                }
            )
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = Priority.NONE)
                },
                onClick = {
                    expanded = false
                    onSortClicked(Priority.NONE)
                }
            )*/
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = TopAppBarContentColor
        )
        DropdownMenu(
            modifier = Modifier
                .background(color = ScaffoldContainerColor),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier
                            .padding(start = LARGE_PADDING),
                        text = stringResource(id = R.string.delete_all_action),
                        style = Typography.labelLarge,
                        color = ScaffoldContentColor
                    )
                },
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        shadowElevation = 4.dp
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(0.7f),
                    text = stringResource(id = R.string.search_placeholder),
                    color = TopAppBarContentColor
                )
            },
            textStyle = TextStyle(
                color = TopAppBarContentColor,
                fontSize = Typography.labelLarge.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(0.5f),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = TopAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = TopAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = TopAppBarBackgroundColor,
                cursorColor = TopAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview
fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllConfirmed = {}
    )
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}