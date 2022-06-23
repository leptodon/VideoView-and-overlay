package ru.cactus.videointeractive

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.graphics.drawable.AnimationDrawable
import android.media.MediaCodec
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var buttonAnimation: AnimationDrawable
    private val timeCount = MutableLiveData<Int>()
    lateinit var playerView: StyledPlayerView

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val timeCountTextViewL = findViewById<TextView>(R.id.leftCount)
        val timeCountTextViewR = findViewById<TextView>(R.id.rightCount)
        val timeLeft = findViewById<ImageView>(R.id.leftShadow)
        val timeRight = findViewById<ImageView>(R.id.rightShadow)
        val player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.player)

        playerView.player = player

        val mediaItem: MediaItem = MediaItem.fromUri(
            Uri.parse(
                "android.resource://"
                        + packageName + "/" + R.raw.tunnel
            )
        )

        player.setMediaItem(mediaItem)
//        player.repeatMode = REPEAT_MODE_ALL
        player.prepare()
        player.play()

        lifecycleScope.launch{
            var isChange = false
            for (i in 100 downTo 0) {
                timeCountTextViewL.text = i.toString()
                timeCountTextViewR.text = i.toString()
                delay(100)
                if (i < 30 && !isChange) {
                    isChange = true
                    timeLeft.setImageResource(R.drawable.time_left_red)
                    timeRight.setImageResource(R.drawable.time_right_red)
                    timeCountTextViewL.setTextColor(resources.getColor(R.color.red))
                    timeCountTextViewR.setTextColor(resources.getColor(R.color.red))
                }
            }
        }

        val buttonImg = findViewById<ImageView>(R.id.image_button).apply {
            setBackgroundResource(R.drawable.button_animation)
            buttonAnimation = background as AnimationDrawable
        }
        buttonAnimation.start()
        buttonImg.setOnClickListener { Toast.makeText(this, "NEXT ACTION", Toast.LENGTH_SHORT).show() }

        val translationX = (resources.displayMetrics.widthPixels / 2 - 50).toFloat()
        val translationY = (resources.displayMetrics.heightPixels / 2 - 100).toFloat()
        val path = Path()

        path.addCircle(translationX, translationY, 200f, Path.Direction.CW)
        val animator = ObjectAnimator.ofFloat(buttonImg, View.X, View.Y, path).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            repeatMode = ObjectAnimator.RESTART
            start()
        }

    }
}