package project.group3tztechcorp.chefitupapp

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import project.group3tztechcorp.chefitupapp.databinding.ActivityScannerBinding
import java.lang.Exception
import java.lang.StringBuilder


class ScannerActivity : AppCompatActivity() {

    private lateinit var imageBitmap: Bitmap
    lateinit var binding: ActivityScannerBinding
    private final var REQUEST_IMAGE_CAPTURE = 1
    private final var REQUEST_PHOTO_CAPTURE = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_scanner)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scanner)

        binding.scannerBtnCamera.setOnClickListener {
            if(checkPermissions()){
                captureImage()
            }else{
                requestPermission()
            }
        }

        binding.scannerBtnPhotos.setOnClickListener {
            if(checkPermissions()) {
                var intent: Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_PHOTO_CAPTURE)
            } else{
                requestPermission()
            }
        }

        binding.scannerBtnScan.setOnClickListener {
            detectText()
        }

    }

    private fun detectText(){
        var image: InputImage = InputImage.fromBitmap(imageBitmap, 0)
        var recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result: Task<Text> = recognizer.process(image).addOnSuccessListener(object : OnSuccessListener<Text>{
            override fun onSuccess(p0: Text) {
                var results: StringBuilder = StringBuilder()
                for(block:Text.TextBlock in p0.textBlocks){
                    var blockText: String = block.text
                    var blockCornerPoint: Array<out Point>? = block.cornerPoints
                    var blockFrame: Rect = block.boundingBox!!
                    for(line: Text.Line in block.lines){
                        var lineText: String = line.text
                        var lineCornerPoint: Array<out Point>? = line.cornerPoints
                        var lineRect: Rect = line.boundingBox!!
                        for(element: Text.Element in line.elements){
                            var elementText: String = element.text
                            results.append(elementText)
                        }
                        binding.scannerText.text = blockText
                    }
                }
            }

        }).addOnFailureListener(object : OnFailureListener{
            override fun onFailure(p0: Exception) {
                Toast.makeText(this@ScannerActivity, "Failed to detect text", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkPermissions():Boolean{
        var camerPermission: Int = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
        return camerPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        var PERMISSION_CODE: Int = 200
        ActivityCompat.requestPermissions(this, arrayOf<String>(CAMERA), PERMISSION_CODE)
    }

    private fun captureImage(){
        var takePicture: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(packageManager) != null){
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size>0){
            var cameraPermission: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (cameraPermission){
                Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                captureImage()
            }else {
                Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // calling on activity result method.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // on below line we are getting
            // data from our bundles. .
            val extras = data?.extras
            imageBitmap = (extras!!["data"] as Bitmap?)!!
            // below line is to set the
            // image bitmap to our image.
            binding.scannerImage.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_PHOTO_CAPTURE && resultCode == RESULT_OK) {
            var selectedImage: Uri = data?.data!!
            binding.scannerImage.setImageURI(selectedImage)
        }
    }
}