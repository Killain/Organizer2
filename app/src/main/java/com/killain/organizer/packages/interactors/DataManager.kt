package com.killain.organizer.packages.interactors

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import co.metalab.asyncawait.async

import com.killain.organizer.packages.database.AppDatabase
import com.killain.organizer.packages.interfaces.DBComponent
import com.killain.organizer.packages.interfaces.DaggerDBComponent

import com.killain.organizer.packages.interfaces.SubTaskDAO
import com.killain.organizer.packages.interfaces.TaskDAO
import com.killain.organizer.packages.models.SubTask
import com.killain.organizer.packages.models.Task
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.ArrayList
import javax.inject.Inject
import kotlin.NullPointerException

class DataManager(val context: Context) {

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var taskDAO: TaskDAO

    @Inject
    lateinit var subTaskDAO: SubTaskDAO

    private var taskList: List<Task>? = null

    private var subTaskList: List<SubTask>? = null
    private var date: String? = null

    var component: DBComponent? = null

//    val undeletedTasks: MutableList<Task>?
//        get() {
//            taskList = taskDAO?.undeletedTasks
//            return taskList as? ArrayList<Task>?
//        }

    init {
        component = DaggerDBComponent.builder().dBInjectorModule(DBInjectorModule(context)).build()
        component?.injectInManager(this)
    }

    fun getUndeletedTasks() : MutableList<Task?>?  {
        return if (::taskDAO.isInitialized) {
            runBlocking {
                taskList = withContext(IO) {
                    taskDAO.getUndeletedTasks()
                }
//                    taskList = taskDAO.getUndeletedTasks()
//                    return taskList as MutableList<Task?>?
            }
            taskList as MutableList<Task?>?

        } else {
            throwTaskDAOException()
            null
        }
    }

    fun getTasksByState(isCompleted: Boolean, isDeleted: Boolean): ArrayList<Task>? {
        return if (::taskDAO.isInitialized) {
            runBlocking {
                taskList = withContext(IO) {
                    taskDAO.getAllTasksByState(isCompleted, isDeleted)
                }
            }
            taskList as ArrayList<Task>?
        } else {
            throwTaskDAOException()
            null
        }
    }

    fun updateTask(task: Task?) {
        if (::taskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    taskDAO.updateTask(task)
                }
            }
        } else {
            throwTaskDAOException()
        }
    }

    fun customUpdateTask(task: Task?, oldText: String) {
        if (::taskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    taskDAO.customUpdateTask(task?.task_string,
                            task?.date, task?.time,
                            task?.isCompleted, task?.isNotificationShowed,
                            task?.isDeleted, task?.isHasReference,
                            oldText)
                }
            }
        } else {
            throwTaskDAOException()
        }
    }

    fun addTask(task: Task) {
        if (::taskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    taskDAO.addTask(task)
                }
            }
        } else {
            throwTaskDAOException()
        }
    }

    fun addSubTask(subTask: SubTask?) {
        if (::subTaskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    subTaskDAO.addSubTask(subTask)
                }
            }
        } else {
            throwSubTaskDAOException()
        }
    }

    fun updateSubTask(subTask: SubTask?) {
        if (::subTaskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    subTaskDAO.updateSubTask(subTask)
                }
            }
        } else {
            throwSubTaskDAOException()
        }
    }

    fun deleteSubTask(subTask: SubTask?) {
        if (::subTaskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    subTaskDAO.deleteSubTask(subTask)
                }
            }
        } else {
            throwSubTaskDAOException()
        }
    }

    fun deleteTask(task: Task?) {
        if (::taskDAO.isInitialized) {
            runBlocking {
                withContext(IO) {
                    taskDAO.deleteTask(task)
                }
            }
        } else {
            throwTaskDAOException()
        }
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun getSubTasksByReference(reference: String): ArrayList<SubTask>? {
        return if (::subTaskDAO.isInitialized) {
                runBlocking {
                    subTaskList = withContext(IO) {
                        subTaskDAO.getSubTasksByReference(reference)
                    }
            }
            subTaskList as ArrayList<SubTask>?
        } else {
            throwSubTaskDAOException()
            null
        }
    }

    fun getAllTasksByDate(date: Long): MutableList<Task?>? {
        return if (::taskDAO.isInitialized) {
            runBlocking {
                taskList = withContext(IO) {
                    taskDAO.getAllTasksByDate(date)
                }
            }
            taskList as? MutableList<Task?>
        } else {
            throwTaskDAOException()
            null
        }
    }

    fun destroyInstance() {

    }

    private fun throwTaskDAOException() {
        Toast.makeText(context, "TaskDAO is not initialized in DataManager", LENGTH_SHORT).show()
        throw NullPointerException("TaskDAO is not initialized in DataManager")
    }

    private fun throwSubTaskDAOException() {
        Toast.makeText(context, "SubTaskDAO is not initialized in DataManager", LENGTH_SHORT).show()
        throw NullPointerException("SubTaskDAO is not initialized in DataManager")
    }
}
