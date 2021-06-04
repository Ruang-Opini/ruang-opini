package id.ruangopini.ui.post.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.load
import coil.transform.CircleCropTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import id.ruangopini.R
import id.ruangopini.data.model.Policy
import id.ruangopini.data.model.PolicyFirebase
import id.ruangopini.data.model.Post
import id.ruangopini.data.model.forFirebase
import id.ruangopini.databinding.ActivityCreatePostBinding
import id.ruangopini.ui.base.reference.ReferenceActivity
import id.ruangopini.ui.policy.detail.adapter.PolicyDocumentAdapter
import id.ruangopini.ui.post.fragment.CloseListener
import id.ruangopini.ui.post.fragment.ImagePostAdapter
import id.ruangopini.utils.Helpers.afterTextChanged
import id.ruangopini.utils.Helpers.getPlainText
import id.ruangopini.utils.Helpers.handleImagePicker
import id.ruangopini.utils.Helpers.hideView
import id.ruangopini.utils.Helpers.showView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class CreatePostActivity : AppCompatActivity(), CloseListener {

    private lateinit var binding: ActivityCreatePostBinding
    private val model: CreatePostViewModel by viewModel()
    private var currentReference: PolicyFirebase? = null
    private var discussionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_close)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        model.getUserData()
        intent.extras?.getString(EXTRA_DISCUSSION)?.let { discussionId = it }

        with(binding) {

            lifecycleScope.launch {
                edtContent.afterTextChanged { validateContent(it) }
            }
            model.photoUrl.observe(this@CreatePostActivity, {
                if (it != null) ivAvatar.load(it) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                else setDefaultPhoto()
            })

            btnAddImage.setOnClickListener {
                ImagePicker.with(this@CreatePostActivity)
                    .crop()
                    .createIntent { activityForResult.launch(it) }
            }
            btnAddReference.setOnClickListener {
                refResult.launch(Intent(this@CreatePostActivity, ReferenceActivity::class.java))
            }
            btnCloseRef.setOnClickListener {
                currentReference = null
                btnAddReference.showView()
                layoutReference.hideView()
            }
            btnCreatePost.setOnClickListener {
                val post = Post(
                    edtContent.getPlainText(),
                    model.getCurrentImage(),
                    currentReference
                )
                post.discussionId = discussionId
                model.createNewPost(post, this@CreatePostActivity)
            }
        }

        model.image.observe(this, {
            binding.rvPhoto.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = ImagePostAdapter(this@CreatePostActivity, it, this@CreatePostActivity)
            }
        })
    }

    private fun validateContent(it: String) {
        binding.btnCreatePost.isEnabled = it.isNotEmpty()
    }

    private val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.handleImagePicker(applicationContext) { model.addImage(it) }
    }


    private val refResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<Policy>(ReferenceActivity.RESULT_POLICY)?.let {
                populateReference(it)
            }
        }
    }

    private fun populateReference(data: Policy) = with(binding) {
        currentReference = data.forFirebase()
        btnAddReference.hideView()
        layoutReference.apply {
            showView()
            tvReferenceTitle.text = buildString {
                append(data.policyNum).append("-").append(data.name)
            }
            rvPolicyDocs.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = PolicyDocumentAdapter(this@CreatePostActivity, data.documents.documents)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onClose(position: Int) {
        model.removeImage(position)
    }

    private fun setDefaultPhoto() {
        binding.ivAvatar.apply {
            load(R.drawable.ic_person) {
                crossfade(true)
                imageTintList =
                    ContextCompat.getColorStateList(this@CreatePostActivity, R.color.primary)
                transformations(CircleCropTransformation())
            }
        }
    }

    companion object {
        const val EXTRA_DISCUSSION = "extra_discussion"
    }
}