package com.blinkslabs.blinkist.android.challenge.common.ext

import android.content.Context
import android.view.MenuItem
import com.blinkslabs.blinkist.android.challenge.R
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

const val MENU_ITEM_THROTTLE_SECONDS = 2L

fun MenuItem.configureBehaviour(
    context: Context,
    otherMenu: MenuItem?,
    function: () -> Unit
): Disposable {
    this.setUnselectedColor(context)
    return Observable.create<MenuItem> { emitter ->
        this.setOnMenuItemClickListener {
            emitter.onNext(it)
            true
        }
        emitter.setCancellable {
            this.setOnMenuItemClickListener(null)
        }
    }.throttleFirst(MENU_ITEM_THROTTLE_SECONDS, TimeUnit.SECONDS).subscribe {
        otherMenu?.setUnselectedColor(context)
        this.setSelectedColor(context)
        function()
    }

}

private fun MenuItem.setUnselectedColor(context: Context) {
    this.setColorTint(context, R.color.colorText)
}

private fun MenuItem.setColorTint(context: Context, colorId: Int) {
    this.icon?.setTint(context.getColor(colorId))
}

fun MenuItem.setSelectedColor(context: Context) {
    this.setColorTint(context, R.color.colorAccent)
}
