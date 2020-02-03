package example.com.diary.di

import com.vanniktech.rxpermission.RxPermission
import example.com.diary.DiaryApplication
import example.com.diary.data.persistence.IDatabaseProvider
import example.com.diary.utils.schedulers.SchedulerProvider

interface AppApi {
    fun app(): DiaryApplication
    fun db(): IDatabaseProvider
    fun rxPermissions(): RxPermission
    fun schedulers(): SchedulerProvider
}