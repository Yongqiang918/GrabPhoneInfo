package com.xx.grabphonedata

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable


/**
 *Created by Lyq
 *on 2020-10-12
 */
object PermissionUtil {

    private lateinit var rxPermissions: RxPermissions
    private lateinit var observable: Observable<Permission>

    public val IS_AGREE = 1   //同意
    public val IS_REFUSE = 2   //拒绝没有选中不在询问
    public val IS_NOT_ASK = 3   //不在询问

    public fun initRxPermissions(mProActivity: FragmentActivity): PermissionUtil {
        rxPermissions = RxPermissions(mProActivity)
        return this
    }

    @SuppressLint("CheckResult")
    fun setPermissions(vararg permiss: String): PermissionUtil {
        observable = rxPermissions.requestEach(*permiss)
        return this
    }

    @SuppressLint("CheckResult")
    public fun setPermissionsCallback(performedListener: RxPermissionLisenter) {
        observable.subscribe { permission ->
            when {
                permission.granted ->
                    performedListener.result(IS_AGREE)
                permission.shouldShowRequestPermissionRationale ->
                    performedListener.result(IS_REFUSE)
                else -> performedListener.result(IS_NOT_ASK)
            }
        }
    }

    public interface RxPermissionLisenter {
        fun result(code: Int)
    }




}