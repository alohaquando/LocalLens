package com.oreomrone.locallens.data.utils

fun String.cleanQueryString(): String {
  return this.trimIndent().replace(
      "\n",
      ""
    ).replace(
      " ",
      ""
    )
}