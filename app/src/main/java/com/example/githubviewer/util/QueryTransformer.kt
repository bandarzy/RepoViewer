package com.example.githubviewer.util

val SPLITTER = Regex("[,]+[ ]*")
const val QUERY_CONNECTOR = "+"

/**
 * Method to translate comma separates keywords to query accepted by Github API.
 */
fun formatQuery(text: String) = text.replace(SPLITTER, QUERY_CONNECTOR)