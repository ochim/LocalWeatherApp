package com.example.localweatherapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.localweatherapp.databinding.ActivityContributeBinding

private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_VIDEO_CAPTURE = 2

class ContributeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContributeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContributeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPhoto.setOnClickListener {
            try {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } catch (ex: Exception) {
            }
        }

        binding.buttonVideo.setOnClickListener {
            try {
                Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                }
            } catch (ex: Exception) {
            }
        }

        binding.buttonContribute.setOnClickListener{
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        binding.imageView.visibility = View.INVISIBLE
        binding.videoView.visibility = View.INVISIBLE


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = intent?.extras?.get("data") as? Bitmap
            imageBitmap ?: return
            binding.imageView.visibility = View.VISIBLE
            binding.imageView.setImageBitmap(imageBitmap)
            return
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = intent?.data
            videoUri ?: return
            binding.videoView.visibility = View.VISIBLE
            binding.videoView.setVideoURI(videoUri)
            binding.videoView.start()
            return
        }
    }

}