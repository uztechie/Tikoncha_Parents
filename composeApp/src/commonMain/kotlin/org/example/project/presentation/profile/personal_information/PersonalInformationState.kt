package org.example.project.presentation.profile.personal_information

import org.example.project.presentation.domain.model.Child

data class PersonalInformationState(
    val profileImage: String = "",
    val fullName: String = "Shuxratov Saidburxon",
    val phoneNumber: String = "+998 93 348 23 65",
    val relativity: String = "Otasi",
    val passportNumber: String = "AD 2346534235",
    val childrenData: List<Child> = listOf(
        Child(
            fullName = "Ergashev Ergashtir Abdukarim o'g'li",
            age = 16,
            gender = "Erkak",
            phoneNumber = "+998 99 123 34 23",
            school = "Andijon shahar, 13-maktab",
            className = "10-A",
            shift = "2-smena"
        ),
        Child(
            fullName = "Kettiyev Keldi Abdukarim o'g'li",
            age = 16,
            gender = "Erkak",
            phoneNumber = "+998 91 269 48 09",
            school = "Andijon shahar, 9-maktab",
            className = "10-W",
            shift = "5-smena"
        )
    )
)