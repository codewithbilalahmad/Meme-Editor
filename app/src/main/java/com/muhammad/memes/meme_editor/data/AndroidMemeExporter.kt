package com.muhammad.memes.meme_editor.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.unit.IntSize
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import com.muhammad.memes.R
import com.muhammad.memes.meme_editor.domain.MemeExporter
import com.muhammad.memes.meme_editor.domain.SaveToStorageStrategy
import com.muhammad.memes.meme_editor.presentation.MemeText
import com.muhammad.memes.meme_editor.presentation.util.MemeRenderCalculator
import com.muhammad.memes.meme_editor.presentation.util.ScaledMemeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AndroidMemeExporter(
    private val context: Context,
) : MemeExporter {
    private val memeRenderCalculator =
        MemeRenderCalculator(context.resources.displayMetrics.density)

    override suspend fun exportMeme(
        backgroundImageBytes: ByteArray,
        memeTexts: List<MemeText>,
        templateSize: IntSize,
        saveToStorageStrategy: SaveToStorageStrategy,
        fileName: String,
    ) = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        var outputBitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeByteArray(backgroundImageBytes, 0, backgroundImageBytes.size)
            outputBitmap = renderMeme(
                background = bitmap, memeTexts = memeTexts, templateSize = templateSize
            )
            val filePath = saveToStorageStrategy.getFilePath(fileName)
            val file = File(filePath)
            FileOutputStream(file).use { outputStream ->
                outputBitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream)
            }
            Result.success(file.absolutePath)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Result.failure(e)
        } finally {
            bitmap?.recycle()
            outputBitmap?.recycle()
        }
    }

    private suspend fun renderMeme(
        background: Bitmap,
        memeTexts: List<MemeText>,
        templateSize: IntSize,
    ): Bitmap = withContext(Dispatchers.Default) {
        val output = background.copy(
            Bitmap.Config.ARGB_8888,
            true
        )
        val canvas = Canvas(output)
        val scaleFactors = memeRenderCalculator.calculateScaleFactors(
            bitmapWidth = background.width,
            bitmapHeight = background.height,
            templateSize = templateSize
        )
        val scaledMemeTexts = memeTexts.map { memeText ->
            memeRenderCalculator.calculateScaledMemeText(
                memeText = memeText,
                scaleFactors = scaleFactors,
                templateSize = templateSize
            )
        }
        scaledMemeTexts.forEach { scaledMemeText ->
            drawText(canvas, scaledMemeText)
        }
        output
    }

    private fun drawText(canvas: Canvas, scaledMemeText: ScaledMemeText) {
        val impactTypeface = ResourcesCompat.getFont(
            context, R.font.impact
        ) ?: Typeface.DEFAULT_BOLD
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = scaledMemeText.strokeWidth
            textSize = scaledMemeText.scaledFontSizePx
            typeface = impactTypeface
            color = Color.BLACK
        }
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = scaledMemeText.scaledFontSizePx
            typeface = impactTypeface
            color = Color.WHITE
        }
        val strokeLayout = StaticLayout.Builder.obtain(
            scaledMemeText.text,
            0, scaledMemeText.text.length,
            TextPaint(strokePaint),
            scaledMemeText.constraintsWidth
        ).setAlignment(Layout.Alignment.ALIGN_CENTER).setIncludePad(false).build()
        val fillLayout = StaticLayout.Builder.obtain(
            scaledMemeText.text,
            0, scaledMemeText.text.length,
            TextPaint(fillPaint),
            scaledMemeText.constraintsWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_CENTER).setIncludePad(false).build()
        val textHeight = strokeLayout.height.toFloat()
        val textWidth = (0 until strokeLayout.lineCount)
            .maxOfOrNull { strokeLayout.getLineWidth(it) }
            ?: 0f
        val boxWidth = textWidth + scaledMemeText.textPaddingX * 2
        val boxHeight = textHeight + scaledMemeText.textPaddingY * 2

        val centerX = scaledMemeText.scaledOffset.x + boxWidth /2f
        val centerY = scaledMemeText.scaledOffset.y + boxHeight /2f

        canvas.withTranslation(centerX, centerY){
            scale(scaledMemeText.scale, scaledMemeText.scale)
            rotate(scaledMemeText.rotation)

            val textCenteringOffset = (scaledMemeText.constraintsWidth - textWidth) /2f

            translate(
                -boxWidth  / 2f + scaledMemeText.textPaddingX - textCenteringOffset,
                -boxHeight / 2f + scaledMemeText.textPaddingY
            )
            strokeLayout.draw(this)
            fillLayout.draw(this)
        }
    }
}