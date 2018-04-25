package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient
import totoro.yui.util.F
import totoro.yui.util.api.Imgur

class ImgurAction : SensitivityAction("i", "imgur", "picture", "image") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        val picture = Imgur.random()
        if (picture != null) {
            client.send(command.chan, F.Yellow + picture.title + F.Reset + " / " + picture.url)
        } else {
            client.send(command.chan, F.Gray + "enough of pictures for today" + F.Reset)
        }
        return true
    }
}
