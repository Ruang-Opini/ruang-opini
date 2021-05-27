package id.ruangopini.ui.policy.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import id.ruangopini.R
import id.ruangopini.data.model.PolicyCategory
import id.ruangopini.databinding.ActivityDetailPolicyBinding
import id.ruangopini.ui.policy.detail.adapter.DetailPolicyAdapter
import id.ruangopini.utils.Helpers.getUrlPath
import id.ruangopini.utils.Helpers.initSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPolicyBinding
    private lateinit var skeleton: Skeleton
    private val model: DetailPolicyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        skeleton = binding.rvDetailPolicy
            .applySkeleton(R.layout.item_reference, 20)
            .apply { initSkeleton(this@DetailPolicyActivity) }

        intent.extras?.getParcelable<PolicyCategory>(EXTRA_CATEGORY)?.let {
            supportActionBar?.apply {
                title = it.name
                setDisplayHomeAsUpEnabled(true)
            }
            val type = intent.extras?.getInt(EXTRA_TYPE, 1)
            if (type == 1) model.getPolicyByType(it.url.getUrlPath())
            else model.getPolicyByCategory(it.url.getUrlPath())

        }

        model.listPolicy.observe(this, {
            binding.rvDetailPolicy.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = DetailPolicyAdapter(this@DetailPolicyActivity, it)
            }
        })

        model.isLoading.observe(this, { populateLoading(it) })

    }

    private fun populateLoading(it: Boolean) {
        if (it) skeleton.showSkeleton()
        else skeleton.showOriginal()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_TYPE = "extra_type"
    }
}