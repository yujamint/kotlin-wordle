package wordle.domain

class Answer(val value: String) {

    init {
        require(value.length == WORD_LENGTH) { "정답은 " + WORD_LENGTH + "글자여야 합니다." }
        require(!value.contains(BLANK)) { "정답에 공백이 들어갈 수 없습니다." }
    }

    fun judge(input: String): List<String> {
        val indexMarker: MutableMap<Char, MutableList<Int>> = HashMap()
        val result = mutableListOf("회색", "회색", "회색", "회색", "회색")

        markAnswerCharacterIndex(indexMarker)

        checkGreen(input, indexMarker, result)
        checkYellow(input, indexMarker, result)

        return result.toList()
    }

    private fun markAnswerCharacterIndex(map: MutableMap<Char, MutableList<Int>>) {
        value.forEachIndexed { index, char ->
            if (!map.containsKey(char)) map[char] = mutableListOf()

            map[char]!!.add(index)
        }
    }

    private fun checkGreen(
        input: String,
        indexMarker: MutableMap<Char, MutableList<Int>>,
        result: MutableList<String>,
    ) {
        input.forEachIndexed { index, char ->
            if (isGreenCondition(indexMarker, char, index)) {
                result[index] = "초록"
                indexMarker[char]!!.remove(index)
            }
        }
    }

    private fun isGreenCondition(
        indexMarker: MutableMap<Char, MutableList<Int>>,
        char: Char,
        index: Int,
    ) = indexMarker.containsKey(char) && indexMarker[char]!!.contains(index)

    private fun checkYellow(
        input: String,
        indexMarker: MutableMap<Char, MutableList<Int>>,
        result: MutableList<String>,
    ) {
        input.forEachIndexed { index, char ->
            if (isYellowCondition(indexMarker, char, result, index)) {
                result[index] = "노랑"
                indexMarker[char]!!.removeAt(0)
            }
        }
    }

    private fun isYellowCondition(
        indexMarker: MutableMap<Char, MutableList<Int>>,
        char: Char,
        result: MutableList<String>,
        index: Int,
    ) = indexMarker.containsKey(char) && indexMarker[char]!!.isNotEmpty() && result[index] != "초록"

    companion object {
        private const val WORD_LENGTH = 5
        private const val BLANK = " "
    }
}