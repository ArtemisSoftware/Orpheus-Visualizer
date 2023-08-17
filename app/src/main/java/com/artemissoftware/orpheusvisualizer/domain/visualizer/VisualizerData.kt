package com.artemissoftware.orpheusvisualizer.domain.visualizer

import kotlin.math.abs
import kotlin.math.min

/**
 * Class that allows to take some samples from an audio file and convert the byte array into an integer array
 */
class VisualizerData(
    private val rawWaveform: ByteArray = ByteArray(0),
    private val captureSize: Int = 0,
) {

    fun resample(resolution: Int): IntArray {
        if (captureSize == 0) {
            return IntArray(0)
        }

        val processed = IntArray(resolution)
        val groupSize = captureSize / resolution

        for (i in 0 until resolution) {
            processed[i] = rawWaveform.map { abs(it.toInt()) }
                .subList(i * groupSize, min((i + 1) * groupSize, rawWaveform.size))
                .average().toInt()
        }
        return processed
    }

    companion object {

        fun getMaxProcessed(resolution: Int): IntArray {
            val processed = IntArray(resolution)
            for (i in 0 until resolution) {
                processed[i] = resolution * 4
            }
            return processed
        }

        fun emptyVisualizerData(): VisualizerData {
            return VisualizerData()
        }
    }
}
