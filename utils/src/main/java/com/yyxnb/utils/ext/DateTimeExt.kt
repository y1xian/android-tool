package com.yyxnb.utils.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * Description: 时间日期相关
 */

// 日期格式
const val DATE_FORMAT = "yyyy-MM-dd"
const val TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val FORMAT_YYYY_MM = "yyyy-MM"
const val FORMAT_YYYY = "yyyy"
const val FORMAT_HH_MM = "HH:mm"
const val FORMAT_HH_MM_SS = "HH:mm:ss"
const val FORMAT_MM_SS = "mm:ss"
const val FORMAT_MM_DD_HH_MM = "MM-dd HH:mm"
const val FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss"
const val FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
const val FORMAT_YYYY2MM2DD = "yyyy.MM.dd"
const val FORMAT_YYYY2MM2DD_HH_MM = "yyyy.MM.dd HH:mm"
const val FORMAT_MMCDD_HH_MM = "MM月dd日 HH:mm"
const val FORMAT_MMCDD = "MM月dd日"
const val FORMAT_YYYYCMMCDD = "yyyy年MM月dd日"

const val ONE_DAY = 1000 * 60 * 60 * 24.toLong()

/**
 * 时间戳转换成字符窜
 *  字符串日期格式（比如：2018-4-6)转为毫秒
 *  @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun String.toDateMills(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).parse(this).time


/**
 * 时间戳转换成字符窜
 * Long类型时间戳转为字符串的日期格式
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

/**
 * 获取系统时间戳
 */
fun getCurTimeLong(): Long = System.currentTimeMillis()

/**
 * 获取当前时间
 */
fun getCurDate(format: String = "yyyy-MM-dd HH:mm:ss"): String = SimpleDateFormat(format, Locale.getDefault()).format(Date())

/**
 * 获取当前时间是星期几
 */
fun getWeekOfDate(dt: Date): String {
    val weekDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    val cal = Calendar.getInstance()
    cal.time = dt
    var w = cal[Calendar.DAY_OF_WEEK] - 1
    if (w < 0) w = 0
    return weekDays[w]
}

/**
 * 时间转换 时 分 秒
 */
fun getDurationInString(time: Long): String {
    var time = time
    var durStr = ""
    if (time == 0L) {
        return "0秒"
    }
    time /= 1000
    val hour = time / (60 * 60)
    time -= 60 * 60 * hour
    val min = time / 60
    time -= 60 * min
    val sec = time
    durStr = when {
        hour != 0L -> {
            hour.toString() + "时" + min + "分" + sec + "秒"
        }
        min != 0L -> {
            min.toString() + "分" + sec + "秒"
        }
        else -> {
            sec.toString() + "秒"
        }
    }
    return durStr
}

/**
 * 获取两个时间相差的天数
 *
 * @return time1 - time2相差的天数
 */
fun getDayOffset(time1: Long, time2: Long): Int { // 将小的时间置为当天的0点
    val offsetTime: Long
    if (time1 > time2) {
        offsetTime = time1 - getDayStartTime(getCalendar(time2)).getTimeInMillis()
    } else {
        offsetTime = getDayStartTime(getCalendar(time1)).getTimeInMillis() - time2
    }
    return (offsetTime / ONE_DAY).toInt()
}

/**
 * 当前毫秒
 */
fun getCalendar(time: Long): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return calendar
}

fun getDayStartTime(calendar: Calendar): Calendar {
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar
}