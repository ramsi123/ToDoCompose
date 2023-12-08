package com.example.to_docompose.util

// What is this function (fun String?.toAction(): Action)?
    // This is called an extension function. This function is used to convert a string to an Action. This function will be used
    // when sending an argument from ListComposable to task screen.
    // Source: Video no. 37. CRUD Operations - Add Task

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action {
    // This is the new implementation
    // Source: Video no. 61. Update #5 - Project Cleanup
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)

    // This is the old implementation
    /*return when {
        this == "ADD" -> {
            Action.ADD
        }
        this == "UPDATE" -> {
            Action.UPDATE
        }
        this == "DELETE" -> {
            Action.DELETE
        }
        this == "DELETE_ALL" -> {
            Action.DELETE_ALL
        }
        this == "UNDO" -> {
            Action.UNDO
        }
        else -> {
            Action.NO_ACTION
        }
    }*/
}
