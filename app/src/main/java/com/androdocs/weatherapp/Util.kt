package com.androdocs.weatherapp

import kotlin.math.roundToInt



fun String.replaceAndCapitalize() = this.replace("\"", "").split(" ").joinToString(" ") { it.capitalize() }.trimEnd()

