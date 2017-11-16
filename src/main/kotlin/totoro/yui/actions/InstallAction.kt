package totoro.yui.actions

import totoro.yui.client.Command
import totoro.yui.client.IRCClient

private val tutor_pages = mapOf(
        "debian" to "https://www.debian.org/releases/stable/amd64/",
        "gentoo" to "https://wiki.gentoo.org/wiki/Handbook:AMD64#Installing_Gentoo",
        "arch" to "https://wiki.archlinux.org/index.php/installation_guide",
        "archlinux" to "https://wiki.archlinux.org/index.php/installation_guide",
        "fedora" to "https://docs.fedoraproject.org/installation-guide/",
        "suse" to "https://doc.opensuse.org/documentation/leap/startup/html/book.opensuse.startup/art.opensuse.installquick.html",
        "opensuse" to "https://doc.opensuse.org/documentation/leap/startup/html/book.opensuse.startup/art.opensuse.installquick.html",
        "ubuntu" to "https://tutorials.ubuntu.com/tutorial/tutorial-install-ubuntu-desktop",
        "lfs" to "http://www.linuxfromscratch.org/lfs/view/stable/"
)

class InstallAction : SensitivityAction("install") {
    override fun handle(client: IRCClient, command: Command): Boolean {
        if (command.args.isEmpty()) return false
        val os = command.args.first()
        val message = tutor_pages.getOrDefault(os, "try \"~search install $os\"")
        client.send(command.chan, message)
        return true
    }
}
