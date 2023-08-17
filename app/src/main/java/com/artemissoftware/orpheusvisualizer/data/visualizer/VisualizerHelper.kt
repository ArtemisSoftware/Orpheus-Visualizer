package com.artemissoftware.orpheusvisualizer.data.visualizer

import android.media.audiofx.Visualizer
import com.artemissoftware.orpheusvisualizer.domain.visualizer.VisualizerData

/**
 * Class to capture the raw wave form in real time
 */
class VisualizerHelper {

    private var visualizer: Visualizer? = null

    private fun visualizerCallback(onData: (VisualizerData) -> Unit) =
        object : Visualizer.OnDataCaptureListener {

            var lastDataTimestamp: Long? = null

            override fun onWaveFormDataCapture(visualizer: Visualizer, waveform: ByteArray, samplingRate: Int) {
                val now = System.currentTimeMillis()
                val durationSinceLastData = lastDataTimestamp?.let { now - it } ?: 0

                if (lastDataTimestamp == null || durationSinceLastData > SAMPLING_INTERVAL) {
                    onData(VisualizerData(rawWaveform = waveform.clone(), captureSize = CAPTURE_SIZE))
                    lastDataTimestamp = now
                }
            }

            override fun onFftDataCapture(visualizer: Visualizer, fft: ByteArray, samplingRate: Int) = Unit
        }

    /**
     * Method to start the visualization of the wave form
     */
    fun start(audioSessionId: Int = 0, onData: (VisualizerData) -> Unit) {
        stop()
        visualizer = Visualizer(audioSessionId).apply {
            enabled = false
            captureSize = CAPTURE_SIZE
            setDataCaptureListener(
                visualizerCallback(onData),
                Visualizer.getMaxCaptureRate(),
                true,
                true,
            )
            enabled = true
        }
    }

    /**
     * Method to stop the visualization of the wave form
     */
    fun stop() {
        visualizer?.release()
        visualizer = null
    }

    companion object {

        /**
         * The lenght of the data that must be represented. It has to have the shape or form of the power of 2
         */
        val CAPTURE_SIZE = Visualizer.getCaptureSizeRange()[1] // [1] because it represents the minimum captured size

        /**
         * Duration (in miliseconds) of the animation presented in the composable
         */
        const val SAMPLING_INTERVAL = 100
    }
}
