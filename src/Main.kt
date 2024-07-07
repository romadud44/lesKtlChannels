import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

suspend fun main() {
    /**
     * В классе с функцией main необходимо написать функцию, которая преобразует строку в список строк.
     */
    fun getStringList(inputString: String): List<String> {
        return inputString.trim().splitToSequence(' ').filter { it.isNotEmpty() }.toList()
    }

    /**
     * Написать функцию getList(text: String), которая отправляет данные этого списка в канал с задержкой 10L.
     */
    suspend fun CoroutineScope.getList(text: String): ReceiveChannel<String> = produce {
        val textList = getStringList(text)
        for (i in textList) {
            delay(10L)
            send(i)
        }
        channel.close()
    }


    /**
     * В функции main необходимо получить эти данные и вернуть их в виде исходного текста, сохранить результат в
     * переменную stringResult. Посчитать время, затраченное на получение данных в main. Вывести в консоль полученный
     * результат в stringResult.
     */

    val resultList = mutableListOf<String>()
    val time = measureTimeMillis {
        coroutineScope {
            val channelOne = getList(Storage().text)

            channelOne.consumeEach {
                println("Полученные данные: $it")
                resultList += it
            }
        }
    }
    val stringResult = resultList.joinToString(
        separator = " "
    )
    println(stringResult)
    println("Затраченное время: $time мс.")
}

/**
 *   Создайте класс Storage – это хранилище, в котором находится текстовая переменная text, в ней хранится басня
 *   Крылова «Мартышка и очки».
 */
class Storage() {
    val text = "Мартышка и очки"
}

