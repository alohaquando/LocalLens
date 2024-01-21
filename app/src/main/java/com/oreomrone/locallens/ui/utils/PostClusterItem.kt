package com.oreomrone.locallens.ui.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.oreomrone.locallens.domain.Post

data class PostClusterItem(
  val itemPosition: LatLng,
  val itemTitle: String,
  val itemSnippet: String,
  val itemZIndex: Float,
  val post: Post? = null
) : ClusterItem {
  override fun getPosition(): LatLng = itemPosition

  override fun getTitle(): String = itemTitle

  override fun getSnippet(): String = itemSnippet

  override fun getZIndex(): Float = itemZIndex
}