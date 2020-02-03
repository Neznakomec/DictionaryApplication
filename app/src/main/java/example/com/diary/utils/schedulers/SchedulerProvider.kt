package example.com.diary.utils.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {

    val single: Scheduler

    val io: Scheduler

    val computation: Scheduler

    val mainThread: Scheduler

}