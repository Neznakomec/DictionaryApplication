package example.com.diary.data.yandextranslator.apikey

import org.junit.Test
import java.lang.StringBuilder

class YandexTranslationApiKeyTest {

    val codedKey = "tspvp37597<;=F?A@JfDHHJLNsHPR~WVSR[U\u0089X[\u008D_\u008D]Y\u008Ec\u0090\u0090i\u0094\u0095\u0099emgn\u009Eimo ¢vrpyr¥xy\u007F~y\u0080\u0080¯\u00AD°\u0085\u0086\u0083\u0084\u0087µ";
    val originalKey = "trnsl.1.1.20191209T143456Z.56a973192e35f7d3.b6ba9ccf1817f034de83080b4497176dac77335b"

    fun `transform original key to coded key`() {
        val key = originalKey
        val sb = StringBuilder()

        for (i in key.indices) {
            sb.append(key[i] + i)
        }

        System.out.println(sb.toString())
    }

    @Test
    fun testEncodeKeyBack() {
        val sb = StringBuilder()
        for (i in 0 until codedKey.length) {
            sb.append(codedKey[i] - i)
        }

        print(sb.toString())
        assert(sb.toString().equals(originalKey))
    }

    @Test
    fun testEncodeMethod() {
        val testedKey = TranslationApiKey.getTranslateApiKey()
        val rightKey = originalKey
        assert(testedKey == rightKey)
    }
}