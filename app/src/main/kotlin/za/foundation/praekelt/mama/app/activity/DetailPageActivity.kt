package za.foundation.praekelt.mama.app.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.TypedValue
import kotlinx.android.synthetic.activity_detail_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dimen
import org.jetbrains.anko.info
import org.jetbrains.anko.maxLines
import za.foundation.praekelt.mama.R
import za.foundation.praekelt.mama.app.viewmodel.DetailPageActivityViewModel
import za.foundation.praekelt.mama.databinding.ActivityDetailPageBinding
import za.foundation.praekelt.mama.inject.component.DaggerDetailPageActivityComponent
import za.foundation.praekelt.mama.inject.component.DetailPageActivityComponent
import za.foundation.praekelt.mama.inject.module.DetailPageActivityModule
import javax.inject.Inject

/**
 * Created by eduardokolomajr on 2015/09/14.
 */
public class DetailPageActivity() : AppCompatActivity(), Animator.AnimatorListener, AnkoLogger {
    companion object {
        const val TAG: String = "DetailPAgeActivity"
    }

    object argsKeys {
        const val uuidKey = "pageUuid"
    }

    val activityComp: DetailPageActivityComponent by lazy { getDetailPageActivityComponent() }
    lateinit var pageUuid: String
        @Inject set
    lateinit var viewModel: DetailPageActivityViewModel
        @Inject set

    val animSet: AnimatorSet = AnimatorSet()
    private val textSizeAnimator: ValueAnimator by lazy { createTitleTextSizeAnimator() }
    private val textPaddingAnimator: ValueAnimator by lazy { createTitleTextPaddingAnimator() }
    val maxTitleHeight: Int by lazy { this.tv_collapsing_title.height }
    var expandedPaddingStartSize: Int = 0
    var collapsedPaddingStartSize: Int = 0
    var expandedTextSize: Float = 0.0f
    var collapsedTextSize: Float = 0.0f
    var diffSize: Float = 0.0f
    var collapsed:Boolean = false
    var increasing:Boolean = true
    var animEnded: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComp.inject(this)
        val binding: ActivityDetailPageBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_detail_page)
        binding.page = viewModel
        binding.executePendingBindings()

        expandedPaddingStartSize = dimen(R.dimen.collapseTitlePaddingStartExpanded)
        collapsedPaddingStartSize = dimen(R.dimen.collapseTitlePaddingStartCollapsed)
        expandedTextSize = dimen(R.dimen.abc_text_size_display_1_material).toFloat()
        collapsedTextSize = dimen(R.dimen.abc_text_size_title_material_toolbar).toFloat()
        diffSize = (expandedTextSize - collapsedTextSize);
        info("extended size = ${expandedTextSize}")
        info("collapsed size = ${collapsedTextSize}")
        animSet.playTogether(textSizeAnimator, textPaddingAnimator)
        animSet.setDuration(50)
        animSet.addListener(this)
        this.appbar.addOnOffsetChangedListener { appBarLayout, i ->
            //collapsing
            if(i+maxTitleHeight < 0 && !collapsed && increasing){
                stopAnimationIfRunning()
                textSizeAnimator.setFloatValues(this.tv_collapsing_title.textSize, collapsedTextSize.toFloat())
                textPaddingAnimator.setIntValues(this.tv_collapsing_title.paddingLeft, collapsedPaddingStartSize)
                increasing = false
                animSet.start()
                info("called start")
//                textSizeAnimator.start()
            }else if(i+maxTitleHeight > 0 && collapsed && !increasing){
                stopAnimationIfRunning()
                textSizeAnimator.setFloatValues(this.tv_collapsing_title.textSize, expandedTextSize.toFloat())
                textPaddingAnimator.setIntValues(this.tv_collapsing_title.paddingLeft, expandedPaddingStartSize)
                increasing = true
                animSet.start()
//                textSizeAnimator.start()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        maxTitleHeight //lazy initilises value when layout is ready
    }

    private fun getDetailPageActivityComponent(): DetailPageActivityComponent {
        return DaggerDetailPageActivityComponent.builder()
                .detailPageActivityModule(DetailPageActivityModule(this))
                .build()
    }

    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator) {
        info("Animation ended")
        animEnded = true;
        if (!increasing) {
            this.tv_collapsing_title.ellipsize = TextUtils.TruncateAt.END
            this.tv_collapsing_title.maxLines = 1
            collapsed = true
        }else {
            collapsed = false
            this.tv_collapsing_title.maxLines = 3
        }
    }

    override fun onAnimationCancel(animation: Animator?) { info("oncancel"); animEnded = true }

    override fun onAnimationStart(animation: Animator?) { info("onstart"); animEnded = false }

    /**
     * Animator for title text view size.
     * Make sure to set the values before calling start
     */
    private fun createTitleTextSizeAnimator(): ValueAnimator{
        val animator: ValueAnimator = ValueAnimator.ofFloat()
        animator.addUpdateListener{valueAnimator ->
            this.tv_collapsing_title
                    .setTextSize(TypedValue.COMPLEX_UNIT_PX, (valueAnimator.animatedValue as Float))
        }
        return animator
    }

    /**
     * Animator for title text view padding.
     * Make sure to set the values before calling start
     */
    private fun createTitleTextPaddingAnimator(): ValueAnimator{
        val animator: ValueAnimator = ValueAnimator.ofFloat()
        animator.addUpdateListener{valueAnimator ->
            this.tv_collapsing_title.setPadding((valueAnimator.animatedValue as Int),
                    this.tv_collapsing_title.paddingTop,
                    this.tv_collapsing_title.paddingRight,
                    this.tv_collapsing_title.paddingBottom)
        }
        return animator
    }

    private fun stopAnimationIfRunning(): Unit{
        if(!animEnded) {
            info("cacelling anim")
            animSet.cancel()
        }
    }
}
