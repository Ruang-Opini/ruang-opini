package id.ruangopini.ui.editprofile

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import id.ruangopini.R
import id.ruangopini.data.model.User
import id.ruangopini.databinding.ActivityEditProfileBinding
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.handleImagePicker
import id.ruangopini.utils.Helpers.showError
import id.ruangopini.utils.Helpers.showView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val model: EditProfileViewModel by viewModel()
    private var avaUrl = ""
    private var bannerUrl = ""
    private var reqImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Edit Profile"
            setDisplayHomeAsUpEnabled(true)
        }

        intent.extras?.getParcelable<User>(EXTRA_USER)?.let { populateData(it) }
        model.photoUrl.observe(this) { populateAva(it) }
        model.headerUrl.observe(this) { populateBanner(it) }
        with(binding) {
            lifecycleScope.launch {
                edtName.afterTextChanged {
                    val valid = it.isNotBlank()
                    btnEditProfile.isEnabled = valid
                    if (!valid) tilName.showError()
                    else Helpers.validateError(tilName)
                }
            }
            ivPicture.setOnClickListener {
                ImagePicker.with(this@EditProfileActivity)
                    .cropSquare()
                    .createIntent {
                        reqImage = REQ_AVA
                        activityForResult.launch(it)
                    }
            }
            btnEditBanner.setOnClickListener {
                ImagePicker.with(this@EditProfileActivity)
                    .crop(16f, 9f)
                    .createIntent {
                        reqImage = REQ_BANNER
                        activityForResult.launch(it)
                    }
            }
        }
    }

    private fun populateBanner(it: Uri?) = with(binding) {
        if (it != null) ivBanner.load(it) { crossfade(true) }
    }

    private fun populateData(user: User) = with(binding) {
        user.name?.let { edtName.setText(it) }
        user.bio?.let { edtBio.setText(it) }
        model.loadAva(user)
        model.loadBanner(user)

        btnEditProfile.setOnClickListener {
            user.apply {
                name = edtName.getPlainText()
                bio = edtBio.getPlainText().let { bi -> if (bi.isNotBlank()) bi else null }
                photoUrl = avaUrl.let { ava -> if (ava.isNotBlank()) ava else user.photoUrl }
                headerUrl = bannerUrl.let { hea -> if (hea.isNotBlank()) hea else user.headerUrl }
            }
            val isAvaUpdate = avaUrl.isNotBlank()
            val isHeaderUpdate = bannerUrl.isNotBlank()
            model.updateUser(user, isAvaUpdate, isHeaderUpdate, this@EditProfileActivity)
        }
    }

    private fun populateAva(it: Uri?) = with(binding) {
        if (it != null) ivProfile.load(it) {
            crossfade(true)
            transformations(CircleCropTransformation())
            error(R.drawable.ic_person)
            placeholder(R.drawable.ic_person)
        } else ivProfile.apply {
            load(R.drawable.ic_person) {
                crossfade(true)
                imageTintList = ContextCompat.getColorStateList(applicationContext, R.color.primary)
                transformations(CircleCropTransformation())
            }
        }
        ivBlackBackground.showView()
    }

    private val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.handleImagePicker(applicationContext) {
            with(binding) {
                if (reqImage == REQ_AVA) {
                    avaUrl = it.toString()
                    ivProfile.load(avaUrl) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                    ivBlackBackground.showView()
                } else {
                    bannerUrl = it.toString()
                    ivBanner.load(bannerUrl) { crossfade(true) }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private const val REQ_AVA = "req_ava"
        private const val REQ_BANNER = "req_banner"
    }
}