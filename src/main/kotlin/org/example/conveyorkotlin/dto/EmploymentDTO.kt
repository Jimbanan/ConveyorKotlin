package org.example.conveyorkotlin.dto

import org.example.conveyorkotlin.enums.EmploymentStatus
import org.example.conveyorkotlin.enums.Position
import java.math.BigDecimal

data class EmploymentDTO(
    var employmentStatus: EmploymentStatus,
    var employerINN: String,
    var salary: BigDecimal,
    var position: Position,
    var workExperienceTotal: Int,
    var workExperienceCurrent: Int
)