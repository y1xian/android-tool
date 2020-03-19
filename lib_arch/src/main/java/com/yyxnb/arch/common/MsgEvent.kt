package com.yyxnb.arch.common

import java.io.Serializable

data class MsgEvent @JvmOverloads constructor(
        var code: Int = 0,
        var msg: String = "",
        var obj: Any? = null
) : Serializable