package com.example.to_docompose.data.repositories

import com.example.to_docompose.data.ToDoDao
import com.example.to_docompose.data.models.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/* Why are we using @ViewModelScoped?
     This annotation is part of dagger hilt library, which basically tell that the instance of our ToDoRepository will be alive as long
     as the shared view model in which we are going to inject this repository.*/
/* @Inject constructor(toDoDao: ToDoDao)
     So this is how we inject a dependency with dagger hilt. Dagger hilt library knows how to inject this ToDoDao, because inside of the
     DatabaseModule class we have created a function called provideDao that return a ToDoDao. Basically our hilt library is using that
     return type to decide which one of those provides function should be injected into our ToDoRepository. Since we are injecting ToDoDao,
     that is how our dagger hilt library knows how to provide this instance.*/

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAllTask()
    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<ToDoTask>> = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return toDoDao.getSelectedTask(taskId = taskId)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        toDoDao.addTask(toDoTask = toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask) {
        toDoDao.updateTask(toDoTask = toDoTask)
    }

    suspend fun deleteTask(toDoTask: ToDoTask) {
        toDoDao.deleteTask(toDoTask = toDoTask)
    }

    suspend fun deleteAllTasks() {
        toDoDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> {
        return toDoDao.searchDatabase(searchQuery = searchQuery)
    }

}