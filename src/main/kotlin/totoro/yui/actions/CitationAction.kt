package totoro.yui.actions

import totoro.yui.Yui
import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import java.io.File
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class CitationAction : SensitivityAction("cite", "citation", "excerption", "snippet", "quotation") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val citation = getCitation()
        if (citation == null)
            client.send(command.chan, "${F.Gray}nothing to cite here${F.Reset}")
        else
            citation.split('\n').map { client.send(command.chan, "${F.Yellow}$it${F.Reset}") }
        return true
    }

    private fun getCitation(): String? {
        val booksDir = File("./books")
        if (booksDir.exists()) {
            val files = booksDir.listFiles()
            if (files.isNotEmpty()) {
                val file = files[Yui.Random.nextInt(files.size)]
                val position = Math.abs(Yui.Random.nextLong()) % file.length()
                val stream = FileInputStream(file)
                val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
                reader.skip(position)
                while (reader.ready()) {
                    val char = reader.read().toChar()
                    if (char == '.' || char == '\n') break
                }
                val builder = StringBuilder()
                while (reader.ready()) {
                    val char = reader.read().toChar()
                    if (char.isLetter()) {
                        builder.append(char)
                        break
                    }
                }
                while (reader.ready()) {
                    val char = reader.read().toChar()
                    if (char != '\n') builder.append(char)
                    if ((char == '.' && !builder.endsWith("Mr")) || char == '\n') break
                }
                reader.close()
                if (builder.isNotEmpty()) return builder.toString()
            }
        }
        return null
    }
}
