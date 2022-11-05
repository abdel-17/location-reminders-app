package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.runBlocking

/**
 * A fake data source that acts as a test double to LocalDataSource.
 */
internal class FakeDataSource : ReminderDataSource {
    
    /**
     * Set this value to `true` if you want [getReminders]
     * and [getReminder] to return [Result.Error].
     */
    var shouldReturnError = false
    
    private val reminders = LinkedHashMap<String, ReminderDTO>()
    
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (shouldReturnError) {
            Result.Error(UNKNOWN_ERROR_MESSAGE)
        } else {
            Result.Success(reminders.values.toList())
        }
    }
    
    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders[reminder.id] = reminder
    }
    
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        val reminder = reminders[id]
        return if (shouldReturnError) {
            Result.Error(UNKNOWN_ERROR_MESSAGE)
        } else if (reminder == null) {
            Result.Error("Could not find a reminder with id $id")
        } else {
            Result.Success(reminder)
        }
    }
    
    override suspend fun deleteReminder(id: String) {
        reminders.remove(id)
    }
    
    override suspend fun deleteAllReminders() {
        reminders.clear()
    }
    
    /**
     * Resets this data source to its initial value.
     */
    fun reset() = runBlocking {
        shouldReturnError = false
        deleteAllReminders()
    }
    
    companion object {
        const val UNKNOWN_ERROR_MESSAGE = "An unknown error has occurred"
    }
}