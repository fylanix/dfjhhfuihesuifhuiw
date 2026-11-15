package com.example.satuhati.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.satuhati.domain.model.AncSchedule
import com.example.satuhati.domain.model.HusbandChecklistItem
import com.example.satuhati.domain.model.VisitType
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "schedules")
data class AncScheduleEntity(
    @PrimaryKey
    val id: String,
    val coupleId: String,
    val visitType: String,
    val title: String,
    val dateEpochMillis: Long,
    val location: String,
    val doctorOrMidwife: String,
    val notes: String,
    val isCompleted: Boolean,
    val husbandChecklistJson: String,
    val createdAtEpochMillis: Long
) {
    fun toDomain(): AncSchedule {
        val checklistList = mutableListOf<HusbandChecklistItem>()
        try {
            if (husbandChecklistJson.isNotBlank()) {
                val array = JSONArray(husbandChecklistJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    checklistList.add(
                        HusbandChecklistItem(
                            id = obj.optString("id"),
                            title = obj.optString("title"),
                            isChecked = obj.optBoolean("isChecked", false)
                        )
                    )
                }
            }
        } catch (_: Exception) { }

        val type = try {
            VisitType.valueOf(visitType)
        } catch (_: Exception) {
            VisitType.K1
        }

        return AncSchedule(
            id = id,
            coupleId = coupleId,
            visitType = type,
            title = title,
            dateEpochMillis = dateEpochMillis,
            location = location,
            doctorOrMidwife = doctorOrMidwife,
            notes = notes,
            isCompleted = isCompleted,
            husbandChecklist = checklistList,
            createdAtEpochMillis = createdAtEpochMillis
        )
    }

    companion object {
        fun fromDomain(domain: AncSchedule): AncScheduleEntity {
            val jsonArray = JSONArray()
            domain.husbandChecklist.forEach { item ->
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("title", item.title)
                obj.put("isChecked", item.isChecked)
                jsonArray.put(obj)
            }

            return AncScheduleEntity(
                id = domain.id,
                coupleId = domain.coupleId,
                visitType = domain.visitType.name,
                title = domain.title,
                dateEpochMillis = domain.dateEpochMillis,
                location = domain.location,
                doctorOrMidwife = domain.doctorOrMidwife,
                notes = domain.notes,
                isCompleted = domain.isCompleted,
                husbandChecklistJson = jsonArray.toString(),
                createdAtEpochMillis = domain.createdAtEpochMillis
            )
        }
    }
}
