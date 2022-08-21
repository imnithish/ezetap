/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.network.model

import com.google.gson.annotations.SerializedName

data class UIResponse(
    @SerializedName("logo-url")
    val logoUrl: String,
    @SerializedName("heading-text")
    val headingText: String,
    val uidata: List<Uidata>
)