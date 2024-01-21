package com.oreomrone.locallens.ui.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun getBytes(inputStream: InputStream): ByteArray {
  val byteBuffer = ByteArrayOutputStream()
  val bufferSize = 1024
  val buffer = ByteArray(bufferSize)
  var len = 0
  while (inputStream.read(buffer).also { len = it } != -1) {
    byteBuffer.write(buffer, 0, len)
  }
  return byteBuffer.toByteArray()
}


fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray {
  if (uri == Uri.EMPTY) {
    return byteArrayOf()
  }
  val inputStream = contentResolver.openInputStream(uri)
  if (inputStream != null) {
    return getBytes(inputStream)
  }
  return byteArrayOf()
}