package example.com.diary.data.yandextranslator.apikey

object TranslationApiKey {
    // correct key
    // val KEY: String = "trnsl.1.1.20191209T143456Z.56a973192e35f7d3.b6ba9ccf1817f034de83080b4497176dac77335b"
    // stub false key, use GetApiKey.getApiKey()
    const val KEY: String = "tspvp37597<;=F?A@JfDHHJLNsHPR~WVSR[U09T143456Z.56a973192e35f7d3.b6ba9ccf1817f034de83080b4497176dac77335b"
    // parts of correct key
    private const val KEY1: String = "tspvp37597<;=F?A@JfDHHJLNsHPR~WVSR[U"
    private const val KEY2: String = "\u0089X[\u008D_\u008D]Y\u008Ec\u0090\u0090i\u0094\u0095\u0099emgn\u009E"
    private const val KEY3: String = "imo ¢vrpyr¥xy\u007F~y\u0080\u0080¯\u00AD°\u0085\u0086\u0083\u0084\u0087µ"

    fun getTranslateApiKey(): String {
        val builder = StringBuilder()
        builder.append(KEY1)
        builder.append(KEY2)
        builder.append(KEY3)
        for (i in builder.indices) {
            builder.setCharAt(i, builder[i] - i)
        }

        return builder.toString()
    }
}