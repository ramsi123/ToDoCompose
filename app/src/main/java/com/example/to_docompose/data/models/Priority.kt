package com.example.to_docompose.data.models

import androidx.compose.ui.graphics.Color
import com.example.to_docompose.ui.theme.HighPriorityColor
import com.example.to_docompose.ui.theme.LowPriorityColor
import com.example.to_docompose.ui.theme.MediumPriorityColor
import com.example.to_docompose.ui.theme.NonePriorityColor

// Why enum class used for data model?
    // If you want to create a data class that distinct from each other, then you can use enum class instead of Constants (Object Class).
    // In this example we have a HIGH priority to NONE priority. All those 'variable' or priorities is distinct from each other, therefore
    // we use enum class. Why not Object Class? Because Object Class only able to contain string and primitive data type. Meanwhile in this
    // example, we are using Color data type.

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}