package totoro.yui.actions

import totoro.yui.client.IRCClient
import totoro.yui.util.Dict

class InstallAction: Action {
    private val dict = Dict.of(
            "https://www.debian.org/releases/stable/amd64/",
            "https://wiki.gentoo.org/wiki/Handbook:AMD64#Installing_Gentoo",
            "https://wiki.archlinux.org/index.php/installation_guide",
            "https://docs.fedoraproject.org/installation-guide/",
            "https://doc.opensuse.org/documentation/leap/startup/html/book.opensuse.startup/art.opensuse.installquick.html",
            "https://tutorials.ubuntu.com/tutorial/tutorial-install-ubuntu-desktop",
            "http://www.linuxfromscratch.org/lfs/view/stable/"
    )

    override fun process(client: IRCClient, command: Command): Command? {
        if (command.words.isNotEmpty()) {
            when (command.words.first()) {
                "install" -> {
                    if (command.words.size > 1) {
                        when (command.words[1]) {
                            "debian", "gentoo", "arch", "archlinux",
                            "fedora", "suse", "opensuse", "ubuntu",
                            "lfs" -> client.send(command.chan, dict())
                            else -> client.send(command.chan, "try \"~search install ${command.words[1]}\"")
                        }
                        return null
                    }
                }
            }
        }
        return command
    }
}
