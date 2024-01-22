package com.oreomrone.locallens.data.utils

fun BuildProfileImageUrl(fileName: String): String {
  return "https://hyjllqwtvlxqtoxcgbkf.supabase.co/storage/v1/object/public/${fileName}"
}