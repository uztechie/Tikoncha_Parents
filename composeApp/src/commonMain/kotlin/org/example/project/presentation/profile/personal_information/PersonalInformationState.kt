package org.example.project.presentation.profile.personal_information

import org.example.project.presentation.domain.model.Child

data class PersonalInformationState(
    val profileImage: String = "",
    val fullName: String = "Shuxratov Saidburxon",
    val phoneNumber: String = "(93) 348 23 65",
    val relativity: String = "Otasi",
    val passportNumber: String = "AD 5455544",
    val childrenData: List<Child> = listOf(
        Child(
            fullName = "Shuxratov Saidburkhon Dilmurod o’g’li",
            age = 16,
            gender = "Erkak",
            phoneNumber = "(99) 123 34 23",
            school = "Andijon shahar, 13-maktab",
            className = "11-B",
            shift = "2-smena"
        ),
        Child(
            fullName = "Sattorov Bobur Botirjon o’g’li",
            age = 10,
            gender = "Erkak",
            phoneNumber = "(91) 269 48 09",
            school = "Andijon shahar, 13-maktab",
            className = "10-B",
            shift = "1-smena"
        )
    )
)