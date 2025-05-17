package com.tecknobit.refy.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ExpandAll: ImageVector
    get() {
        if (_Expand_all != null) {
            return _Expand_all!!
        }
        _Expand_all = ImageVector.Builder(
            name = "ExpandAll",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 880f)
                lineTo(240f, 640f)
                lineToRelative(57f, -57f)
                lineToRelative(183f, 183f)
                lineToRelative(183f, -183f)
                lineToRelative(57f, 57f)
                close()
                moveTo(298f, 376f)
                lineToRelative(-58f, -56f)
                lineToRelative(240f, -240f)
                lineToRelative(240f, 240f)
                lineToRelative(-58f, 56f)
                lineToRelative(-182f, -182f)
                close()
            }
        }.build()
        return _Expand_all!!
    }

private var _Expand_all: ImageVector? = null
