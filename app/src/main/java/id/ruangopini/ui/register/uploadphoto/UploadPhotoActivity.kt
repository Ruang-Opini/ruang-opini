package id.ruangopini.ui.register.uploadphoto

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load
import coil.transform.CircleCropTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import id.ruangopini.MainActivity
import id.ruangopini.databinding.ActivityUploadPhotoBinding
import id.ruangopini.utils.Helpers.handleImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private val model: UploadPhotoViewModel by viewModel()
    private var photoUrl = ""

// TODO: 5/24/2021 handle login with google
//  private var isLoginWithGoogle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: 5/24/2021 getLasGoogleSign and populate it

        with(binding) {
            ivPicture.setOnClickListener {
                ImagePicker.with(this@UploadPhotoActivity)
                    .cropSquare()
                    .createIntent { activityForResult.launch(it) }
            }

            button.setOnClickListener {
                if (photoUrl.isNotBlank()) {
                    model.uploadPhoto(photoUrl.toUri(), this@UploadPhotoActivity) { moveToMain() }
                } else moveToMain()
            }
        }
    }

    private val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.handleImagePicker(applicationContext) {
            photoUrl = it.toString()
            with(binding) {
                button.text = "Gunakan"
                ivProfile.load(photoUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    override fun onBackPressed() { /*do nothing here*/
    }
}