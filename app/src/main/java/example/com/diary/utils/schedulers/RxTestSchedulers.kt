package example.com.diary.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class RxTestSchedulers : SchedulerProvider {

    override val single: Scheduler = Schedulers.trampoline()

    override val io: Scheduler = Schedulers.trampoline()

    override val computation: Scheduler = Schedulers.trampoline()

    override val mainThread: Scheduler = Schedulers.trampoline()

}