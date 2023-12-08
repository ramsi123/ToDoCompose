package com.example.to_docompose.util

/* This is called Generic Class. Why do we need a generic class?
     This generic class is used to tackle the EmptyContent problem. When user don't have data stored in a database, then the ListContent
     will show the EmptyContent screen. But the problem is, when the user has at least one item in database and we launch our application
     for the first time, then this EmptyContent will shown only for a half of a second. The main reason for that is because inside our
     SharedViewModel, the default value for '_allTasks' is an empty list. So whenever we run our application for the first time, this
     empty list will be passed to our ListContent, and that is why only for a half a second we are going to be able to see this empty
     content even if our database is filled with tasks. The purpose of this generic class is to handle this '_allTasks' state. So that
     the ListContent class will show the list of task item or EmptyScreen if the RequestState is success.
     Source: To-Do App with Jetpack Compose MVVM - Android Development - Video no. 28 Display All Tasks - Create RequestState*/

sealed class RequestState<out T> {
    object Idle : RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
    data class Success<T>(val data: T): RequestState<T>()
    data class Error(val error: Throwable): RequestState<Nothing>()
}
