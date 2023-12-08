package com.example.to_docompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.to_docompose.data.models.ToDoTask
import kotlinx.coroutines.flow.Flow

// Why didn't we add the same suspend keyword in the first two functions or last three function?
    // Because those functions are using Flow. Flow is part of coroutine library, so this Flow is actually running asynchronously by
    // default. So that's why we don't need to add this suspend keyword. But all other functions which don't use this Flow should have
    // this suspend keyword included.

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id")
    fun getAllTask(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE id = :taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>>

    @Query(
        """
            SELECT
                *
            FROM
                todo_table
            ORDER BY
                CASE
            WHEN
                priority LIKE 'L%' THEN 1
            WHEN
                priority LIKE 'M%' THEN 2
            WHEN
                priority LIKE 'H%' THEN 3
            END
        """
    )
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query(
        """
            SELECT
                *
            FROM
                todo_table
            ORDER BY
                CASE
            WHEN
                priority LIKE 'H%' THEN 1
            WHEN
                priority LIKE 'M%' THEN 2
            WHEN
                priority LIKE 'L%' THEN 3
            END
        """
    )
    fun sortByHighPriority(): Flow<List<ToDoTask>>

}