package com.example.synth

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

/** A synthesizer that plays sin waves for Android. */
class Synth : Runnable {
    private lateinit var mThread: Thread
    private var mRunning = false
    private var mFreq = 440.0
    private var mAmp = 0.0
    private var mNumKeysDown = 0

    fun start(): Unit {
        this.mThread = Thread(this)
        this.mRunning = true
        this.mThread.start()
    }

    fun stop(): Unit {
        this.mRunning = false
    }

    fun keyDown(key: Int): Int {
        this.mFreq = Math.pow(1.0594630, (key - 69).toDouble()) * 440.0
        this.mAmp = 1.0
        this.mNumKeysDown += 1
        return mNumKeysDown
    }

    fun keyUp(key: Int): Int {
        this.mAmp = 0.0
        this.mNumKeysDown -= 1
        return mNumKeysDown
    }

    public override fun run() {
        val sampleRate = 44100
        val bufferSize = 1024
        val buffer = ShortArray(bufferSize)
        val audioTrack = AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM
        )
        val fSampleRate = sampleRate.toDouble()
        val pi2 = 2.0 * Math.PI
        var counter = 0.0

        audioTrack.play()

        while (mRunning){
            val tau = (pi2 * mFreq) / fSampleRate
            val maxValue = java.lang.Short.MAX_VALUE * mAmp

            for (i in 0 until bufferSize){
                buffer[i] = (Math.sin(tau * counter) * maxValue).toShort()
                counter += 1.0
            }

            audioTrack.write(buffer, 0, bufferSize)
        }

        audioTrack.stop()
        audioTrack.release()
    }
}
