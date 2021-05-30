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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import id.ruangopini.MainActivity
import id.ruangopini.databinding.ActivityUploadPhotoBinding
import id.ruangopini.utils.Helpers.handleImagePicker
import id.ruangopini.utils.Helpers.showView
import org.koin.androidx.viewmodel.ext.android.viewModel

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding
    private val model: UploadPhotoViewModel by viewModel()
    private var isLoginWithGoogle = false
    private var photoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GoogleSignIn.getLastSignedInAccount(this)?.let { populateData(it) }

        with(binding) {
            ivPicture.setOnClickListener {
                ImagePicker.with(this@UploadPhotoActivity)
                    .cropSquare()
                    .createIntent { activityForResult.launch(it) }
            }

            button.setOnClickListener {
                if (photoUrl.isNotBlank()) {
                    model.uploadPhoto(
                        photoUrl.toUri(),
                        isLoginWithGoogle,
                        this@UploadPhotoActivity
                    ) {
                        moveToMain()
                    }
                } else moveToMain()
            }
        }
    }

    private fun populateData(it: GoogleSignInAccount) = with(binding) {
        isLoginWithGoogle = true
        val img = it.photoUrl.toString().split("=")
        photoUrl = if (img.size == 2) img[0] else it.photoUrl.toString()
        button.text = "Gunakan"
        ivProfile.load(photoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        ivBlackBackground.showView()
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
                ivBlackBackground.showView()
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