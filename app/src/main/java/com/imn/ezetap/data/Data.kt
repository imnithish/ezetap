/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.data

@kotlinx.serialization.Serializable
data class Data(
    val logoUrl: String,
    val headingText: String,
    val uiData: List<UIData>
)