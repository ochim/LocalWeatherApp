package com.example.localweatherapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.localweatherapp.databinding.ActivityContributeBinding

class ContributeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContributeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContributeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val takePictureForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? ->
            binding.imageView.visibility = View.INVISIBLE
            binding.videoView.visibility = View.INVISIBLE

            if (result?.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val imageBitmap = intent.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        binding.imageView.apply {
                            visibility = View.VISIBLE
                            setImageBitmap(it)
                        }
                    }
                }
            }
        }

        val takeVideoForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? ->
            binding.imageView.visibility = View.INVISIBLE
            binding.videoView.visibility = View.INVISIBLE

            if (result?.resultCode == Activity.RESULT_OK) {
                val videoUri: Uri? = result.data?.data
                videoUri?.let {
                    binding.videoView.apply {
                        visibility = View.VISIBLE
                        setVideoURI(videoUri)
                        start()
                    }
                }
            }
        }

        binding.buttonPhoto.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                takePictureForResult.launch(takePictureIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonVideo.setOnClickListener {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                takeVideoIntent.resolveActivity(packageManager)?.also {
                    takeVideoForResult.launch(takeVideoIntent)
                }
            }
        }

        binding.buttonContribute.setOnClickListener {
            if (binding.imageView.visibility == View.VISIBLE ||
                binding.videoView.visibility == View.VISIBLE
            ) {
                //ダミーメッセージ
                Toast.makeText(this, "投稿しました", Toast.LENGTH_LONG).show()
            }
        }

    }
}