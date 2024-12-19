package com.example.bantalapp

import java.util.Date

data class Journal(val NoteContent:String = "",
                   val NoteTitle:String = "",
                   val timestamp: Date
)
