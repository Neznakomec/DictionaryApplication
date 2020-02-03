package example.com.diary.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultSchedulers : SchedulerProvider {

    override val single: Scheduler = Schedulers.single()

    override val io: Scheduler = Schedulers.io()

    override val computation: Scheduler = Schedulers.computation()

    override val mainThread: Scheduler = AndroidSchedulers.mainThread()

}