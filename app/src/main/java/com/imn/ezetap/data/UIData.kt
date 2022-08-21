/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.data

@kotlinx.serialization.Serializable
data class UIData(
    val hint: String?,
    val key: String?,
    val uitype: String?,
    val value: String?
)